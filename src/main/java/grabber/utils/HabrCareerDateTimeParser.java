package grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabrCareerDateTimeParser implements DateTimeParser {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime parse(String parse) {
        return LocalDateTime.parse(parse, formatter);
    }
}
