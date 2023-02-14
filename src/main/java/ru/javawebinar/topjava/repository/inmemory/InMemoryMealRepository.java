package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private final Map<Integer, Meal> mealRepository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        for (Meal meal : MealsUtil.meals) {
            save(meal, meal.getUserId());
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            mealRepository.put(meal.getId(), meal);
            return meal;
        }
        if (get(meal.getId(), userId) != null) {
            meal.setUserId(userId);
        }

        // handle case: update, but not present in storage
        return meal.getUserId() == null ? null : mealRepository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal currentMeal = mealRepository.get(id);
        return currentMeal != null && currentMeal.getUserId() == userId && mealRepository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal currentMeal = mealRepository.get(id);
        return currentMeal != null && currentMeal.getUserId() == userId ? currentMeal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealRepository.values()
                .stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(
                        Meal::getDate, (m1, m2) -> {
                            if (m1.isBefore(m2)) {
                                return 1;
                            }
                            if (m1.isAfter(m2)) {
                                return -1;
                            } else return 0;
                        }
                ).thenComparing(Meal::getTime, (m1, m2) -> {
                    if (m1.isBefore(m2)) {
                        return 1;
                    }
                    if (m1.isAfter(m2)) {
                        return -1;
                    } else return 0;
                }))
                .collect(Collectors.toList());
    }

    public List<Meal> getAllFiltered(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return mealRepository.values()
                .stream()
                .filter(meal -> meal.getUserId() == userId)
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), startDate, endDate, startTime, endTime))
                .collect(Collectors.toList());
    }
}

