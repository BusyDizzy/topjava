package ru.javawebinar.topjava;

import org.junit.AssumptionViolatedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class CustomJUnitStopWatch extends Stopwatch {

    private static long nanoTime;

    private static final Logger log = LoggerFactory.getLogger(CustomJUnitStopWatch.class);

    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        if (status.equals("succeeded")) {
            log.info("Test {} {}, spent {} microseconds", testName, status, TimeUnit.NANOSECONDS.toMicros(nanos));
            nanoTime += TimeUnit.NANOSECONDS.toMicros(nanos);
        }
        if (testName.equals("duplicateDateTimeCreate") && status.equals("succeeded")) {
            log.info("Class {} successfully finished all tests in {} microseconds", description.getTestClass().getSimpleName(), nanoTime);
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