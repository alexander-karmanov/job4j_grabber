package grabber;

import grabber.utils.HabrCareerDateTimeParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class HabrCareerParse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";

    private String retrieveDescription(String link) throws IOException {
        final String[] description = {""};
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements rows = document.select(".faded-content__body");
        rows.forEach(row -> {
            Element titleElement = row.select(".vacancy-description__text").first();
            description[0] = titleElement.text();
            System.out.println(description[0]);
        });
        return description[0];
    }

    public static void main(String[] args) throws IOException {
         for (int pageNumber = 1; pageNumber < 6; pageNumber++) {
            String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
            Connection connection = Jsoup.connect(fullLink);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                Element dateElement = row.select(".vacancy-card__date").first();
                Element date = dateElement.child(0);
                String vacancyDate = date.attr("datetime");
                HabrCareerDateTimeParser dateTimeParser = new HabrCareerDateTimeParser();
                String parsedDate = dateTimeParser.parse(vacancyDate).toString();
                System.out.printf("%s %s %s%n", vacancyName, link, parsedDate);
            });
        }
    }
}
