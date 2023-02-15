package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
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
    private UserRepository userRepository;

    public InMemoryMealRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
        for (Meal meal : MealsUtil.meals) {
            save(meal, meal.getUserId());
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        User user = userRepository.get(userId);
        if (user == null) {
            return null;
        }

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            mealRepository.put(meal.getId(), meal);
            user.getMealList().add(meal);
            return meal;
        }

        if (get(meal.getId(), userId) == null) {
            return null;
        }
        meal.setUserId(userId);
        user.getMealList().set(user.getMealList().indexOf(mealRepository.get(meal.getId())), meal);
        // handle case: update, but not present in storage
        return mealRepository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal currentMeal = mealRepository.get(id);
        return currentMeal != null && currentMeal.getUserId() == userId && userRepository.get(userId).getMealList().remove(currentMeal) && mealRepository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal currentMeal = mealRepository.get(id);
        return currentMeal != null && currentMeal.getUserId() == userId ? currentMeal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return userRepository.get(userId).getMealList()
                .stream()
                .sorted(Comparator.comparing(
                        Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    public List<Meal> getAllFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        return applyFilter(getAll(userId), startDate, endDate);
    }

    public List<Meal> applyFilter(List<Meal> meals, LocalDate startDate, LocalDate endDate) {
        return meals.stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpenByDate(meal.getDateTime(), startDate, endDate))
                .collect(Collectors.toList());
    }
}

