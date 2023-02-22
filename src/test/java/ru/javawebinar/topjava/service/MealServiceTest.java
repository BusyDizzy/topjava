package ru.javawebinar.topjava.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.UserTestData.USER_ID;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-jdbc-repo.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL_ID_01, USER_ID);
        assertMatch(meal, meal1);
    }

    @Test
    public void delete() {
        service.delete(meal3.getId(), USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(meal3.getId(), USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate startDate = LocalDate.of(2020, Month.JANUARY, 31);
        LocalDate endDate = LocalDate.of(2020, Month.JANUARY, 31);
        assertMatch(service.getBetweenInclusive(startDate, endDate, USER_ID), meal7, meal6, meal5, meal4);
    }

    @Test
    public void getBetweenInclusiveNullFilter() {
        assertMatch(service.getBetweenInclusive(null, null, USER_ID), meal7, meal6, meal5, meal4, meal3, meal2, meal1);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), meal7, meal6, meal5, meal4, meal3, meal2, meal1);
    }

    @Test
    public void duplicateDateMealCreated() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(meal2.getDateTime(),
                        "Duplicate", 666), USER_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(updated.getId(), USER_ID), getUpdated());
    }

    @Test
    public void updateNotFound() {
        Meal updated = getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, NOT_FOUND));
    }

    @Test
    public void updateOtherUserMealNotFound() {
        Meal updated = new Meal(adminMeal1);
        assertThrows(NotFoundException.class, () -> service.update(updated, USER_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void getMealNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_NOT_FOUND, USER_ID));
    }

    @Test
    public void getOtherUserMealNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_MEAL_ID_01, USER_ID));
    }

    @Test
    public void deletedMealNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_NOT_FOUND, USER_ID));
    }

    @Test
    public void deletedOtherUserMealNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(ADMIN_MEAL_ID_01, USER_ID));
    }

    @Test
    public void getUserNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(meal2.getId(), NOT_FOUND));
    }

    @Test
    public void deletedUserNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(meal2.getId(), NOT_FOUND));
    }
}