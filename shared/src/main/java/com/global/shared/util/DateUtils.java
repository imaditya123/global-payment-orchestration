package com.global.shared.util;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private DateUtils() { }

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static Instant nowUtc() {
        return Instant.now();
    }

    public static String toIsoString(Instant instant) {
        return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC).format(ISO_FORMATTER);
    }

    public static Instant parseIso(String iso) {
        return OffsetDateTime.parse(iso).toInstant();
    }
}
