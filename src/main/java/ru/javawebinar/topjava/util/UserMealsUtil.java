package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 410),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(14, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println();

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(14, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        List<UserMealWithExcess> userMealWithExcessesFilteredByTime = new ArrayList<>();
        Map<LocalDate, Integer> dailyCaloriesSum = new HashMap<>();

        for (UserMeal meal : meals) {
            LocalDate currentDate = meal.getDateTime().toLocalDate();
            if (!dailyCaloriesSum.containsKey(currentDate))
                dailyCaloriesSum.put(currentDate, meal.getCalories());
            else
                dailyCaloriesSum.merge(currentDate, meal.getCalories(), Integer::sum);
        }

        for (UserMeal meal : meals) {
            if (dailyCaloriesSum.getOrDefault(meal.getDateTime().toLocalDate(), 0) > caloriesPerDay) {
                userMealWithExcesses.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true));
            }
        }

        for (UserMealWithExcess userMealWithExcess : userMealWithExcesses) {
            if (TimeUtil.isBetweenHalfOpen(userMealWithExcess.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExcessesFilteredByTime.add(userMealWithExcess);
            }
        }


        return userMealWithExcessesFilteredByTime;
    }


    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> dailyCaloriesSum = meals.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));


        return meals.stream()
                .filter(meal -> dailyCaloriesSum.entrySet().stream()
                        .filter(entry -> entry.getValue() > caloriesPerDay)
                        .map(Map.Entry::getKey).collect(Collectors.toSet())
                        .contains(meal.getDateTime().toLocalDate()))
                .map(mealWithExcess -> new UserMealWithExcess(mealWithExcess.getDateTime(), mealWithExcess.getDescription(), mealWithExcess.getCalories(), true))
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toList());

    }
}
