package grabber;

public interface Store extends AutoCloseable {
    void save(Post post);
}
