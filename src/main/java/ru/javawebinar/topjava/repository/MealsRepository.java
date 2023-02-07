package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Set;

public interface MealsRepository {
    Set<Meal> getAll();
    Meal getById(int id);
    boolean removeById(int id);
    Meal update(Meal meal);
    Meal addMeal(Meal meal);
}
