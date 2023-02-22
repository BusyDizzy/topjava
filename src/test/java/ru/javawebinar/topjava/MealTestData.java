package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_ID_01 = START_SEQ + 3;
    public static final int USER_MEAL_ID_02 = START_SEQ + 4;
    public static final int USER_MEAL_ID_03 = START_SEQ + 5;
    public static final int USER_MEAL_ID_04 = START_SEQ + 6;
    public static final int USER_MEAL_ID_05 = START_SEQ + 7;
    public static final int USER_MEAL_ID_06 = START_SEQ + 8;
    public static final int USER_MEAL_ID_07 = START_SEQ + 9;
    public static final int ADMIN_MEAL_ID_01 = START_SEQ + 10;
    public static final int MEAL_NOT_FOUND = 10;
    public static final Meal meal1 = new Meal(USER_MEAL_ID_01, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal meal2 = new Meal(USER_MEAL_ID_02, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal meal3 = new Meal(USER_MEAL_ID_03, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal meal4 = new Meal(USER_MEAL_ID_04, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal meal5 = new Meal(USER_MEAL_ID_05, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal meal6 = new Meal(USER_MEAL_ID_06, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal meal7 = new Meal(USER_MEAL_ID_07, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
    public static final Meal adminMeal1 = new Meal(ADMIN_MEAL_ID_01, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак Админа", 500);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2023, Month.FEBRUARY, 19, 21, 0), "Просто тестовая еда", 2001);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal2);
        updated.setDateTime(LocalDateTime.of(2023, Month.JANUARY, 31, 10, 0));
        updated.setDescription("Pad Thai");
        updated.setCalories(2500);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
