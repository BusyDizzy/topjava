package ru.javawebinar.topjava.service.jdbc;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractServiceTest;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.util.concurrent.TimeUnit;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
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
