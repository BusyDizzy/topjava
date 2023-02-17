package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private final Map<Integer, Map <Integer, Meal>> mealRepository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);


    {
        for (Meal meal : MealsUtil.meals) {
            save(meal, meal.getUserId());
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {

        Map<Integer, Meal> meals = mealRepository.computeIfAbsent(userId, map -> new ConcurrentHashMap<>());

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return meals.computeIfPresent(userId, (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> currentMealMap = mealRepository.get(userId);
        return currentMealMap != null && currentMealMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> currentMealMap = mealRepository.get(userId);
        return currentMealMap == null ? null : currentMealMap.get(id) ;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getAllFiltered(userId, meal -> true);
    }

    public List<Meal> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return getAllFiltered(userId, meal -> Util.isBetweenHalfOpen(meal.getDateTime(), startDate, endDate));
    }

    public List<Meal> getAllFiltered(int userId, Predicate<Meal> filter){
        Map<Integer, Meal> currentMealMap = mealRepository.get(userId);
        return CollectionUtils.isEmpty(currentMealMap) ? Collections.emptyList() :
                currentMealMap.values()
                .stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }


}

