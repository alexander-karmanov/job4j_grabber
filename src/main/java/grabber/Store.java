package grabber;

import java.util.List;

public interface Store extends AutoCloseable {
    void save(Post post);

    List<Post> getAll();

}
