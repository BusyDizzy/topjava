package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private final Map<Integer, Meal> mealRepository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealRepository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return mealRepository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealRepository.get(id).getUserId() == userId && mealRepository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal currentMeal = mealRepository.get(id);
        return currentMeal.getUserId() == userId ? currentMeal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealRepository.values()
                .stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(
                        Meal::getDate, (m1, m2) -> {
                            if (m1.isBefore(m2)) {
                                return -1;
                            }
                            if (m1.isAfter(m2)) {
                                return 1;
                            } else return 0;
                        }
                ))
                .collect(Collectors.toList());
    }
}

