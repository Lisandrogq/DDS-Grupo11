package org.grupo11.Utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Date {
    public static boolean isSameDay(long a, long b) {
        return epochToDate(a).toLocalDate().equals(epochToDate(b).toLocalDate());
    }

    public static LocalDateTime epochToDate(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);
    }

    public static long now() {
        return Instant.now().toEpochMilli();
    }

}
