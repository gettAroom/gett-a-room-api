package fedex.poc.roomer.utils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Created by TG on 21/07/2016.
 */
public class DateUtils {
    public static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    public static ZonedDateTime parse(String date) {
        DateTimeFormatter aDateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT);
        return ZonedDateTime.parse(date, aDateTimeFormatter);
    }

    public static ZonedDateTime toUTC(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
    }

    public static boolean isValidZonedDateTime(String zonedDateTimeString) {
        try {
            ZonedDateTime.parse(zonedDateTimeString);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
