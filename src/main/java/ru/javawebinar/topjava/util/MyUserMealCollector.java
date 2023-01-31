package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MyUserMealCollector implements Collector<UserMeal, Map.Entry<List<UserMeal>, Map<LocalDate, Integer>>, List<UserMealWithExcess>> {
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int caloriesPerDay;
    public MyUserMealCollector(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.caloriesPerDay = caloriesPerDay;
    }

    @Override
    public Supplier<Map.Entry<List<UserMeal>, Map<LocalDate, Integer>>> supplier() {
        return () -> new AbstractMap.SimpleEntry<>(new ArrayList<>(), new HashMap<>());
    }

    @Override
    public BiConsumer<Map.Entry<List<UserMeal>, Map<LocalDate, Integer>>, UserMeal> accumulator() {
        return (listMapEntry, userMeal) -> {
            listMapEntry.getKey().add(userMeal);
            listMapEntry.getValue().merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
        };
    }

    @Override
    public BinaryOperator<Map.Entry<List<UserMeal>, Map<LocalDate, Integer>>> combiner() {
        return (l, r) -> {
            l.getKey().addAll(r.getKey());
            l.getValue().putAll(r.getValue());
            return l;
        };
    }
    @Override
    public Function<Map.Entry<List<UserMeal>, Map<LocalDate, Integer>>, List<UserMealWithExcess>> finisher() {
        return  mapEntry -> mapEntry.getKey()
                .stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        mapEntry.getValue().get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.CONCURRENT);
    }
}
