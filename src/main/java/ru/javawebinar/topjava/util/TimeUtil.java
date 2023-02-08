package ru.javawebinar.topjava.util;

import java.time.LocalTime;

public class TimeUtil {
    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return startTime == null && endTime == null ||
                endTime == null && lt.compareTo(startTime) >= 0 ||
                lt.compareTo(endTime) < 0 && startTime == null ||
                lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }
}
