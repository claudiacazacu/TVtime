import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;

public class Service {
    private List<User> users;
    private List<Media> mediaLibrary;
    private List<WatchEntry> watchEntries;
    private TreeSet<String> genres;

    public Service() {
        this.users = new ArrayList<>();
        this.mediaLibrary = new ArrayList<>();
        this.watchEntries = new ArrayList<>();
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

    public void addWatchEntry(WatchEntry watchEntry) {
        if (watchEntry != null) {
            watchEntries.add(watchEntry);
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

    public List<Media> searchMediaByTitle(String titleQuery) {
        List<Media> results = new ArrayList<>();
        if (titleQuery == null || titleQuery.trim().isEmpty()) {
            return results;
        }

        String normalizedQuery = titleQuery.trim().toLowerCase(Locale.ROOT);
        for (Media media : mediaLibrary) {
            if (media.getTitle().toLowerCase(Locale.ROOT).contains(normalizedQuery)) {
                results.add(media);
            }
        }

        results.sort(Comparator.comparing(Media::getTitle));
        return results;
    }

    public void showMovies() {
        mediaLibrary.stream()
                .filter(media -> media instanceof Movie)
                .sorted(Comparator.comparing(Media::getTitle))
                .forEach(System.out::println);
    }

    public void showSeries() {
        mediaLibrary.stream()
                .filter(media -> media instanceof Series)
                .sorted(Comparator.comparing(Media::getTitle))
                .forEach(System.out::println);
    }

    public List<Media> filterMediaByGenre(String genre) {
        List<Media> filteredMedia = new ArrayList<>();
        if (genre == null || genre.trim().isEmpty()) {
            return filteredMedia;
        }

        String normalizedGenre = genre.trim().toLowerCase(Locale.ROOT);
        for (Media media : mediaLibrary) {
            if (media.getGenre().toLowerCase(Locale.ROOT).equals(normalizedGenre)) {
                filteredMedia.add(media);
            }
        }

        filteredMedia.sort(Comparator.comparing(Media::getTitle));
        return filteredMedia;
    }

    public void showCastForMedia(String mediaTitle) {
        Media media = findMediaByExactTitle(mediaTitle);
        if (media == null) {
            System.out.println("Media not found.");
            return;
        }

        if (media.getCast().isEmpty()) {
            System.out.println("No cast available for " + media.getTitle() + ".");
            return;
        }

        for (Map.Entry<String, Character> castEntry : media.getCast().entrySet()) {
            System.out.println(castEntry.getKey() + " -> " + castEntry.getValue());
        }
    }

    public boolean addEpisodeToSeries(String seriesTitle, Episode episode) {
        if (episode == null) {
            return false;
        }

        Media media = findMediaByExactTitle(seriesTitle);
        if (!(media instanceof Series)) {
            return false;
        }

        ((Series) media).addEpisode(episode);
        return true;
    }

    public boolean addReviewToWatchEntry(WatchEntry watchEntry, double rating, Comment comment) {
        if (watchEntry == null || rating < 0.0 || rating > 10.0) {
            return false;
        }

        watchEntry.setRating(rating);
        if (comment != null) {
            watchEntry.setComment(comment);
        }

        return true;
    }

    public void showCommentsForMedia(String title) {
        Media media = findMediaByExactTitle(title);
        if (media == null) {
            System.out.println("Productia nu exista.");
            return;
        }

        boolean hasComments = false;
        System.out.println("Comentarii pentru " + media.getTitle() + ":");
        for (WatchEntry watchEntry : watchEntries) {
            if (watchEntry.getMedia().getTitle().equalsIgnoreCase(media.getTitle())
                    && watchEntry.getComment() != null) {
                hasComments = true;
                System.out.println("Utilizator: " + watchEntry.getUser().getUsername());
                System.out.println("Rating: " + watchEntry.getRating());
                System.out.println("Comentariu: " + watchEntry.getComment().getText());
                System.out.println();
            }
        }

        if (!hasComments) {
            System.out.println("Nu exista comentarii pentru aceasta productie.");
        }
    }

    public void showUserProfile(String username) {
        User user = findUserByUsername(username);
        if (user == null) {
            System.out.println("Utilizatorul nu exista.");
            return;
        }

        boolean hasWatchEntries = false;
        System.out.println("Profil utilizator: " + user.getUsername());
        for (WatchEntry watchEntry : watchEntries) {
            if (watchEntry.getUser().getUsername().equalsIgnoreCase(user.getUsername())) {
                hasWatchEntries = true;
                System.out.println("Titlu media: " + watchEntry.getMedia().getTitle());
                if (watchEntry.getEpisode() != null) {
                    System.out.println("Episod: " + watchEntry.getEpisode().getTitle());
                } else {
                    System.out.println("Episod: -");
                }
                System.out.println("Data vizionarii: " + watchEntry.getWatchedDate());
                System.out.println("Rating: " + watchEntry.getRating());
                if (watchEntry.getComment() != null) {
                    System.out.println("Comentariu: " + watchEntry.getComment().getText());
                } else {
                    System.out.println("Comentariu: -");
                }
                if (watchEntry.getFavCharacter() != null) {
                    System.out.println("Personaj favorit: " + watchEntry.getFavCharacter().getName());
                } else {
                    System.out.println("Personaj favorit: -");
                }
                System.out.println();
            }
        }

        if (!hasWatchEntries) {
            System.out.println("Utilizatorul nu are vizionari inregistrate.");
        }
    }

    public User findUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        String normalizedUsername = username.trim();
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(normalizedUsername)) {
                return user;
            }
        }

        return null;
    }

    public Media findMediaByExactTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return null;
        }

        String normalizedTitle = title.trim();
        for (Media media : mediaLibrary) {
            if (media.getTitle().equalsIgnoreCase(normalizedTitle)) {
                return media;
            }
        }

        return null;
    }
}
