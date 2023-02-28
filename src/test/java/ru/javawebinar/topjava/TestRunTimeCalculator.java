package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestRunTimeCalculator extends Stopwatch {
    private static final Map<String, Long> testsRunTimeMap = new LinkedHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(TestRunTimeCalculator.class);

    private static void logInfo(Description description, long nanos) {
        String testName = description.getMethodName();
        testsRunTimeMap.put(testName, TimeUnit.NANOSECONDS.toMillis(nanos));
    }

    public static void printTestRunTime() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Long> pairs : testsRunTimeMap.entrySet()) {
            sb.append(pairs.getKey()).append(" - ").append(pairs.getValue()).append(" ms ").append("\n");
        }
        log.info(sb.toString().trim());
    }

    @Override
    protected void finished(long nanos, Description description) {
        logInfo(description, nanos);
    }
}