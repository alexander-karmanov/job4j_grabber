package grabber;

import grabber.utils.DateTimeParser;
import grabber.utils.HabrCareerDateTimeParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";

    public static final int PAGES = 6;

    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private String retrieveDescription(String link) throws IOException {
        final String[] description = {""};
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements rows = document.select(".faded-content__body");
        rows.forEach(row -> {
            Element titleElement = row.select(".vacancy-description__text").first();
            description[0] = titleElement.text();
        });
        return description[0];
    }

    private Post receivePost(Element row) throws IOException {
        /*  метод получения поста из html-страницы  */
        Element titleElement = row.select(".vacancy-card__title").first();
        Element linkElement = titleElement.child(0);
        String vacancyName = titleElement.text();
        String vacancylink = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
        String description = retrieveDescription(vacancylink);
        Element dateElement = row.select(".vacancy-card__date").first();
        Element date = dateElement.child(0);
        LocalDateTime vacancyDate = dateTimeParser.parse(date.attr("datetime"));
        int id = 0;
        return new Post(id, vacancyName, vacancylink, description, vacancyDate);
    }

    public static void main(String[] args) {
        String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, 1, SUFFIX);
        HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        List<Post> vacancy = habrCareerParse.list(fullLink);
        for (Post vacancies : vacancy) {
            System.out.println(vacancies);
        }
    }

    @Override
    public List<Post> list(String link) {
        /*  метод добавления готовых постов в список  */
        List<Post> vacancies = new ArrayList<>();
        try {
            for (int pageNumber = 1; pageNumber < PAGES; pageNumber++) {
                Connection connection = Jsoup.connect(link);
                Document document = connection.get();
                Elements rows = document.select(".vacancy-card__inner");
                for (Element row : rows) {
                    vacancies.add(receivePost(row));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return vacancies;
    }
}
