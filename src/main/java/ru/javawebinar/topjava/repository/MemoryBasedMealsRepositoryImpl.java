package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;


public class MemoryBasedMealsRepositoryImpl implements MealsRepository {
    private AtomicInteger mealId = new AtomicInteger(0);
    private final Set<Meal> meals = new CopyOnWriteArraySet<>();

    public MemoryBasedMealsRepositoryImpl() {
    }

    List<Meal> testMeals = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    {
        testMeals.forEach(this::addMeal);
    }

    @Override
    public Set<Meal> getAll() {
        return meals;
    }

    @Override
    public Meal getById(int id) {
        return meals.stream().filter(meal -> meal.getId() == id).findAny().orElse(null);
    }

    @Override
    public boolean removeById(int id) {
        Meal meal = getById(id);
        if (meal != null) {
            meals.remove(meal);
            return true;
        }
        return false;
    }

    @Override
    public Meal update(Meal newMeal) {
        Meal oldMeal = getById(newMeal.getId());
        if (oldMeal != null) {
            meals.remove(oldMeal);
            meals.add(newMeal);
        }
        return newMeal;
    }

    @Override
    public Meal addMeal(Meal meal) {
        meal.setId(mealId.getAndIncrement());
        meals.add(meal);
        return meal;
    }
}
