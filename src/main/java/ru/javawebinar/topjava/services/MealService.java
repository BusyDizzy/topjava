package ru.javawebinar.topjava.services;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repositories.MealsRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;


public class MealService implements MealsRepository {
    private static int MEAL_ID;
    public static final int caloriesPerDay = 2000;

    public static final List<Meal> meals = new CopyOnWriteArrayList<>();

    public MealService() {
    }

    static {
        meals.add(new Meal(++MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 400));
        meals.add(new Meal(++MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.add(new Meal(++MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.add(new Meal(++MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.add(new Meal(++MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.add(new Meal(++MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.add(new Meal(++MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    private static final Object lock = new Object();

    @Override
    public List<Meal> getAllMeal() {
        return meals;
    }

    @Override
    public Meal getMealById(Integer id) {
        return meals.stream().filter(meal -> Objects.equals(meal.getId(), id)).findAny().orElse(null);
    }

    @Override
    public void removeById(Integer id) {
        meals.removeIf(meal -> Objects.equals(meal.getId(), id));
    }

    @Override
    public void update(Integer id, Meal meal) {
        synchronized (lock) {
            Meal mealToBeUpdated = getMealById(id);
            if (mealToBeUpdated != null) {
                mealToBeUpdated.setDateTime(meal.getDateTime());
                mealToBeUpdated.setDescription(meal.getDescription());
                mealToBeUpdated.setCalories(meal.getCalories());
            }
        }
    }

    @Override
    public void addMeal(Meal meal) {
        synchronized (lock) {
            meal.setId(++MEAL_ID);
            meals.add(meal);
        }
    }
}
