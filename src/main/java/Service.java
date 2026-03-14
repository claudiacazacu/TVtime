import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class Service {
    private List<User> users;
    private List<Media> mediaLibrary;
    private TreeSet<String> genres;

    public Service() {
        this.users = new ArrayList<>();
        this.mediaLibrary = new ArrayList<>();
        this.genres = new TreeSet<>();
    }

    public void addUser(User user) {
        if (user != null) {
            users.add(user);
        }
    }

    public void addMedia(Media media) {
        if (media != null) {
            mediaLibrary.add(media);
            genres.add(media.getGenre());
        }
    }

    public void showAllUsers() {
        for (User user : users) {
            System.out.println(user.getUsername());
        }
    }

    public void showAllMedia() {
        mediaLibrary.sort(Comparator.comparing(Media::getTitle));

        for (Media media : mediaLibrary) {
            System.out.println(media.getTitle());
        }
    }

    public void showAllGenres() {
        for (String genre : genres) {
            System.out.println(genre);
        }
    }
}