package org.grupo11.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateUtils {
    public static boolean isSameDay(long a, long b) {
        return epochToDate(a).toLocalDate().equals(epochToDate(b).toLocalDate());
    }

    public static LocalDateTime epochToDate(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);
    }

    public static long now() {
        return Instant.now().toEpochMilli();
    }

    public static long parseDateString(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = dateFormat.parse(dateString);
        return date.getTime();
    }

    public static long getCurrentTimeInMs() {
        return System.currentTimeMillis();
    }

    public static long getAWeekAgoFrom(long currentTimeMs) {
        long oneWeekMs = 7L * 24 * 60 * 60 * 1000;
        return currentTimeMs - oneWeekMs;
    }

}
