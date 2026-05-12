import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UserService {
    protected final ServiceData data;

    public UserService() {
        this(new ServiceData());
    }

    public UserService(ServiceData data) {
        this.data = data;
    }

    public void addUser(User user) {
        if (user != null) {
            data.getUsers().add(user);
        }
    }

    public void addWatchEntry(WatchEntry watchEntry) {
        if (watchEntry != null) {
            data.getWatchEntries().add(watchEntry);
        }
    }

    public boolean addComment(WatchEntry watchEntry, Comment comment) {
        if (watchEntry == null || comment == null) {
            return false;
        }

        watchEntry.setComment(comment);
        return true;
    }

    public boolean addRating(WatchEntry watchEntry, double rating) {
        if (watchEntry == null || rating < 0.0 || rating > 10.0) {
            return false;
        }

        watchEntry.setRating(rating);
        return true;
    }

    public boolean addReviewToWatchEntry(WatchEntry watchEntry, double rating, Comment comment) {
        boolean ratingAdded = addRating(watchEntry, rating);
        if (!ratingAdded) {
            return false;
        }

        if (comment != null) {
            return addComment(watchEntry, comment);
        }

        return true;
    }

    public void showAllUsers() {
        for (User user : data.getUsers()) {
            System.out.println(user.getUsername());
        }
    }

    public void showAllMedia() {
        data.getMediaLibrary().sort(Comparator.comparing(Media::getTitle));

        for (Media media : data.getMediaLibrary()) {
            System.out.println(media.getTitle());
        }
    }

    public void showAllGenres() {
        for (String genre : data.getGenres()) {
            System.out.println(genre);
        }
    }

    public List<Media> searchMediaByTitle(String titleQuery) {
        List<Media> results = new ArrayList<>();
        if (titleQuery == null || titleQuery.trim().isEmpty()) {
            return results;
        }

        String normalizedQuery = titleQuery.trim().toLowerCase(Locale.ROOT);
        for (Media media : data.getMediaLibrary()) {
            if (media.getTitle().toLowerCase(Locale.ROOT).contains(normalizedQuery)) {
                results.add(media);
            }
        }

        results.sort(Comparator.comparing(Media::getTitle));
        return results;
    }

    public void showMovies() {
        data.getMediaLibrary().stream()
                .filter(media -> media instanceof Movie)
                .sorted(Comparator.comparing(Media::getTitle))
                .forEach(System.out::println);
    }

    public void showSeries() {
        data.getMediaLibrary().stream()
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
        for (Media media : data.getMediaLibrary()) {
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

    public void showCommentsForMedia(String title) {
        Media media = findMediaByExactTitle(title);
        if (media == null) {
            System.out.println("Productia nu exista.");
            return;
        }

        boolean hasComments = false;
        System.out.println("Comentarii pentru " + media.getTitle() + ":");
        for (WatchEntry watchEntry : data.getWatchEntries()) {
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

    public List<WatchEntry> filterCommentsByRating(String mediaTitle, double minRating) {
        List<WatchEntry> filteredEntries = new ArrayList<>();
        Media media = findMediaByExactTitle(mediaTitle);
        if (media == null) {
            return filteredEntries;
        }

        for (WatchEntry watchEntry : data.getWatchEntries()) {
            if (watchEntry.getMedia().getTitle().equalsIgnoreCase(media.getTitle())
                    && watchEntry.getComment() != null
                    && watchEntry.getRating() >= minRating) {
                filteredEntries.add(watchEntry);
            }
        }

        filteredEntries.sort(Comparator.comparingDouble(WatchEntry::getRating).reversed());
        return filteredEntries;
    }

    public void showUserProfile(String username) {
        User user = findUserByUsername(username);
        if (user == null) {
            System.out.println("Utilizatorul nu exista.");
            return;
        }

        boolean hasWatchEntries = false;
        System.out.println("Profil utilizator: " + user.getUsername());
        for (WatchEntry watchEntry : data.getWatchEntries()) {
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

    public void showWatchHistory(String username) {
        User user = findUserByUsername(username);
        if (user == null) {
            System.out.println("Utilizatorul nu exista.");
            return;
        }

        boolean hasHistory = false;
        System.out.println("Istoric vizionare pentru " + user.getUsername() + ":");
        for (WatchEntry watchEntry : data.getWatchEntries()) {
            if (watchEntry.getUser().getUsername().equalsIgnoreCase(user.getUsername())) {
                hasHistory = true;
                System.out.println(watchEntry);
            }
        }

        if (!hasHistory) {
            System.out.println("Utilizatorul nu are istoric de vizionare.");
        }
    }

    public User findUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        String normalizedUsername = username.trim();
        for (User user : data.getUsers()) {
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
        for (Media media : data.getMediaLibrary()) {
            if (media.getTitle().equalsIgnoreCase(normalizedTitle)) {
                return media;
            }
        }

        return null;
    }

    public Episode findEpisodeByTitle(Series series, String episodeTitle) {
        if (series == null || episodeTitle == null || episodeTitle.trim().isEmpty()) {
            return null;
        }

        for (Episode episode : series.getEpisodes()) {
            if (episode.getTitle().equalsIgnoreCase(episodeTitle.trim())) {
                return episode;
            }
        }

        return null;
    }

    public void showCommentsByDescRating(Media media) {
        data.getWatchEntries().stream()
                .filter(entry -> entry.getMedia().equals(media))
                .filter(entry -> entry.getComment() != null)
                .sorted(Comparator.comparing(WatchEntry::getRating).reversed())
                .forEach(System.out::println);

    }
}
