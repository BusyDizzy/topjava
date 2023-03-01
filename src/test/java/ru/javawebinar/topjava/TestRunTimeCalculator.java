package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TestRunTimeCalculator extends Stopwatch {
    private static final Logger log = LoggerFactory.getLogger(TestRunTimeCalculator.class);
    private static final StringBuilder sb = new StringBuilder();

    public static void printTestRunTime() {
        log.info("\n" + sb.toString().trim());
    }

    @Override
    protected void finished(long nanos, Description description) {
        String testName = description.getMethodName();
        Long testTime = TimeUnit.NANOSECONDS.toMillis(nanos);
        log.info("{} - {} ms", testName, testTime);
        sb.append(String.format("%-30s %d ms", testName, testTime)).append("\n");
    }
}