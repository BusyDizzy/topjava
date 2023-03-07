package ru.javawebinar.topjava.service.datajpa;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import ru.javawebinar.topjava.service.AbstractServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.transaction.Transactional;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;


@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
    protected static final StringBuilder results = new StringBuilder();

    @Rule
    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public final Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result);
            log.info(result + " ms\n");
        }
    };

    @AfterClass
    public static void printResult() {
        AbstractServiceTest.log.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");
    }

    @Test
    @Transactional
    public void getMealsWithUser() {
        Meal adminMeal = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(adminMeal, adminMeal1);
        HIBERNATE_USER_MATCHER.assertMatch(adminMeal.getUser(), admin);
    }

    @Test
    public void getMealsWithUserNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithUser(MEAL1_ID, 10));
    }
}
