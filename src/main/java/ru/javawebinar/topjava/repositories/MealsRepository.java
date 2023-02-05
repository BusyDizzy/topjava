package ru.javawebinar.topjava.repositories;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealsRepository {
    List<Meal> getAllMeal();

    Meal getMealById(Integer id);

    void removeById(Integer id);

    void update(Integer id, Meal meal);

    void addMeal(Meal meal);

}
