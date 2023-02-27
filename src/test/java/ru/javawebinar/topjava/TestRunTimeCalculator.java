package ru.javawebinar.topjava;

import org.junit.AssumptionViolatedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class TestRunTimeCalculator extends Stopwatch {
    private static final Map<String, Long> testsRunTimeMap = new TreeMap<>();
    private static final Logger log = LoggerFactory.getLogger(TestRunTimeCalculator.class);


    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        log.info("Test {} {}, spent {} milliseconds", testName, status, TimeUnit.NANOSECONDS.toMillis(nanos));
        String message = testName + " finished with status " + status + " within";
        testsRunTimeMap.put(message, TimeUnit.NANOSECONDS.toMillis(nanos));
    }

    public static void printResultingTimeMap() {
        for (Map.Entry<String, Long> pairs : testsRunTimeMap.entrySet()) {
            log.info(" Test {} {} milliseconds", pairs.getKey(), pairs.getValue());
        }
    }


    @Override
    protected void succeeded(long nanos, Description description) {
        logInfo(description, "succeeded", nanos);
    }

    @Override
    protected void failed(long nanos, Throwable e, Description description) {
        logInfo(description, "failed", nanos);
    }

    @Override
    protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
        logInfo(description, "skipped", nanos);
    }

    @Override
    protected void finished(long nanos, Description description) {
        logInfo(description, "finished", nanos);
    }
}