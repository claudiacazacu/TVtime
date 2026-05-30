import java.util.HashSet;
import java.util.Set;

public class User {
    private String username;
    private int age;
    private String email;
    private Set<Media> watchlist;

    public User(String username, int age, String email) {
        this.username = username;
        this.age = age;
        this.email = email;
        this.watchlist = new HashSet<>();
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Media> getWatchlist() {
        return watchlist;
    }

    public boolean addToWatchlist(Media media) {
        return watchlist.add(media);
    }

    public boolean removeFromWatchlist(Media media) {
        return watchlist.remove(media);
    }

    public boolean isInWatchlist(Media media) {
        return watchlist.contains(media);
    }

    public boolean isAdmin() {
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }
}
