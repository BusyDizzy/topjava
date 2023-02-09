package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;
import java.util.List;

public interface MealsRepository {
    List<Meal> getAll();

    Meal getById(int id);

    boolean removeById(int id);

    Meal add(Meal meal);
}
