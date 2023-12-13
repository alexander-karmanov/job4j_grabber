package grabber;

import grabber.utils.DateTimeParser;
import grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {
    private Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("driver-class-name"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            cnn = DriverManager.getConnection(
                    cfg.getProperty("url"),
                    cfg.getProperty("username"),
                    cfg.getProperty("password")
            );
        } catch (SQLException e) {
           e.printStackTrace();
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement =
                     cnn.prepareStatement("INSERT INTO post(name, text, link, created) VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING",
                             Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> allPosts = new ArrayList<>();
        try {
            PreparedStatement statement = cnn.prepareStatement("SELECT * FROM post;");
            ResultSet resultSet = statement.executeQuery();
             while (resultSet.next()) {
                allPosts.add(new Post(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("text"),
                        resultSet.getString("link"),
                        resultSet.getTimestamp("created").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allPosts;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try {
            PreparedStatement statement = cnn.prepareStatement("SELECT * FROM post WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                post = new Post(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("text"),
                        resultSet.getString("link"),
                        resultSet.getTimestamp("created").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) {
        Properties properties = new Properties();
        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("app.properties")) {
            properties.load(in);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PsqlStore psqlStore = new PsqlStore(properties);
        HabrCareerDateTimeParser dateTimeParser = new HabrCareerDateTimeParser();
        Post post = new Post(1, "Middle Java developer (приложение Инвестиции)",
                "https://career.habr.com/vacancies/1000120919",
                "description",
                dateTimeParser.parse("2023-12-11T11:27:12+03:00"));
        psqlStore.save(post);
        List<Post> allPosts = psqlStore.getAll();
        System.out.println("Все данные:");
        for (Post postOut: allPosts) {
            System.out.println("post = " + postOut);
        }
        Post postFound = psqlStore.findById(1);
        System.out.println("Найденная по id запись = " + postFound);
    }
}
