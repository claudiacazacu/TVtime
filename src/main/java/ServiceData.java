import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class ServiceData {
    private final List<User> users;
    private final List<Media> mediaLibrary;
    private final List<WatchEntry> watchEntries;
    private final TreeSet<String> genres;
    private final List<Post> posts;

    public ServiceData() {
        this.users = new ArrayList<>();
        this.mediaLibrary = new ArrayList<>();
        this.watchEntries = new ArrayList<>();
        this.genres = new TreeSet<>();
        this.posts = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Media> getMediaLibrary() {
        return mediaLibrary;
    }

    public List<WatchEntry> getWatchEntries() {
        return watchEntries;
    }

    public TreeSet<String> getGenres() {
        return genres;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
