package ru.javawebinar.topjava.service;

import org.modelmapper.ModelMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.atStartOfDayOrMin;
import static ru.javawebinar.topjava.util.DateTimeUtil.atStartOfNextDayOrMax;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    private final ModelMapper modelMapper;

    public MealService(MealRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public List<Meal> getBetweenInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        return repository.getBetweenHalfOpen(atStartOfDayOrMin(startDate), atStartOfNextDayOrMax(endDate), userId);
    }

    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public void update(Meal meal, int userId) {
        Assert.notNull(meal, "meal must not be null");
        checkNotFoundWithId(repository.save(meal, userId), meal.id());
    }

    public Meal create(Meal meal, int userId) {
        Assert.notNull(meal, "meal must not be null");
        return repository.save(meal, userId);
    }

    public Meal getWithUser(int id, int userId) {
        return checkNotFoundWithId(repository.getWithUser(id, userId), id);
    }

    public Meal convertToMeal(MealTo mealTo) {
        return modelMapper.map(mealTo, Meal.class);
    }
}