package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {

    private static final LocalDateTime MIN_DATE = LocalDateTime.of(1, 1, 1, 0, 0, 0);
    private static final LocalDateTime MAX_DATE = LocalDateTime.of(3000, 1, 1, 0, 0, 0);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static LocalDateTime convertStartToDateInclusive(LocalDate dateTime) {
        return dateTime == null ? MIN_DATE : dateTime.atStartOfDay();
    }

    public static LocalDateTime convertStartToDateExclusive(LocalDate dateTime) {
        return dateTime == null ? MAX_DATE : dateTime.plus(1, ChronoUnit.DAYS).atStartOfDay();
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static @Nullable LocalDate parseDate(@Nullable String date) {
        return StringUtils.hasLength(date) ? LocalDate.parse(date) : null;
    }

    public static @Nullable LocalTime parseTime(@Nullable String time) {
        return StringUtils.hasLength(time) ? LocalTime.parse(time) : null;
    }
}

