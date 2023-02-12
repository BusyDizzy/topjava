package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;


import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private final Map<Integer, Meal> mealRepository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    // Init of meals for 2 users with 1 and 2 Ids
    {
        for (int i = 0; i < 10; i++) {
            if (i < 7) {
                save(MealsUtil.meals.get(i), 1);
            } else
                save(MealsUtil.meals.get(i), 2);
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (Objects.equals(userId, meal.getUserId())) {
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
                mealRepository.put(meal.getId(), meal);
                return meal;
            }
            // handle case: update, but not present in storage
            return mealRepository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealRepository.get(id).getUserId() == userId && mealRepository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return mealRepository.get(id).getUserId() == userId ? mealRepository.get(id) : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return mealRepository.values()
                .stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted()
                .collect(Collectors.toList());
    }
}

