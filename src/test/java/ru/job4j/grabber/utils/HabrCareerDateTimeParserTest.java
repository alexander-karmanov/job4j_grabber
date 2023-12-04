package ru.job4j.grabber.utils;

import grabber.utils.HabrCareerDateTimeParser;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.assertj.core.api.Assertions.assertThat;

public class HabrCareerDateTimeParserTest {
    @Test
    public void whenParseTextToDate() {
        String textDate = "2023-09-20T12:36:39.359";
        String textOtherDate = "2023-09-15T12:36:39.359";
        LocalDateTime time = new HabrCareerDateTimeParser().parse(textDate);
        assertThat(time).isInstanceOf(LocalDateTime.class);
        assertThat(time).isEqualTo(LocalDateTime.parse(textDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(time).isAfter(LocalDateTime.parse(textOtherDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
