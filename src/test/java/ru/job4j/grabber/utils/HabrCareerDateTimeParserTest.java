package ru.job4j.grabber.utils;

import grabber.utils.DateTimeParser;
import grabber.utils.HabrCareerDateTimeParser;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import static org.assertj.core.api.Assertions.assertThat;

public class HabrCareerDateTimeParserTest {
    @Test
    public void whenParseTextToDate() {
        DateTimeParser parser = new HabrCareerDateTimeParser();
        String textDate = "2023-11-13T11:32:03+03:00";
        LocalDateTime ex = LocalDateTime.of(2023, Month.NOVEMBER, 13, 11, 32, 03);
        assertThat(ex).isEqualTo(parser.parse(textDate));
    }
}
