package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collections;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.UserTestData.getNew;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void getUserWithMeal() {
        User user = service.getWithMeals(USER_ID);
        HIBERNATE_USER_MATCHER.assertMatch(user, UserTestData.user);
        MEAL_MATCHER.assertMatch(user.getMeals(), meals);
    }

    @Test
    public void getWithMealNotFound() {
        assertThrows(NotFoundException.class,
                () -> service.getWithMeals(1));
    }

    @Test
    public void getUserWithNoMeal() {

        User created = service.create(getNew());
        int newId = created.id();
        User user = service.getWithMeals(newId);

        User newUser = getNew();
        newUser.setId(newId);

        HIBERNATE_USER_MATCHER.assertMatch(user, newUser);
        MEAL_MATCHER.assertMatch(user.getMeals(), Collections.emptyList());
    }
}
