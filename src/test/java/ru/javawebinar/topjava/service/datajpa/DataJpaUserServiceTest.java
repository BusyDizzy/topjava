package ru.javawebinar.topjava.service.datajpa;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractServiceTest;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.transaction.Transactional;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.HIBERNATE_USER_MATCHER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    @Transactional
    public void getUserWithMeals() {
        User user = service.getWithMeals(USER_ID);
        HIBERNATE_USER_MATCHER.assertMatch(user, UserTestData.user);
        MEAL_MATCHER.assertMatch(user.getMeals(), meals);
    }

    @Test
    public void getWithMealsNotFound() {
        assertThrows(NotFoundException.class,
                () -> service.getWithMeals(1));
    }

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
}
