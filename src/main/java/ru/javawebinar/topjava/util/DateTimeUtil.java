package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpenByTime(LocalDateTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.toLocalTime().compareTo(startTime) >= 0
                && lt.toLocalTime().compareTo(endTime) < 0;
    }

    public static boolean isBetweenHalfOpenByDate(LocalDateTime lt, LocalDate startDate, LocalDate endDate) {
        return lt.toLocalDate().compareTo(startDate) >= 0
                && lt.toLocalDate().compareTo(endDate) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

