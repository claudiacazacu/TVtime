package service;

import exception.InvalidRatingException;
import exception.MediaNotFoundException;
import exception.UserNotFoundException;
import model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class UserService {
    protected final ServiceData data;

    public ServiceData getData() {
        return data;
    }

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
            // Auto-remove din watchlist cand utilizatorul marcheaza ca a vazut titlul
            watchEntry.getUser().removeFromWatchlist(watchEntry.getMedia());
        }
    }

    public boolean addComment(WatchEntry watchEntry, Comment comment) {
        if (watchEntry == null || comment == null) {
            return false;
        }

        watchEntry.setComment(comment);
        return true;
    }

    public boolean addRating(WatchEntry watchEntry, double rating) throws InvalidRatingException {
        if (rating < 0.0 || rating > 10.0) {
            throw new InvalidRatingException(rating);
        }
        if (watchEntry == null) {
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

    public void showCommentsForMedia(String title) throws MediaNotFoundException {
        Media media = findMediaByExactTitle(title);
        if (media == null) {
            throw new MediaNotFoundException(title);
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

    public void showUserProfile(String username) throws UserNotFoundException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
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

    //popularity index = numarul de vizionari ale unei media specifice/nr vizionari per total

    public int totalWatchesWeek(List<WatchEntry> entries){
        LocalDate today = LocalDate.now();
        LocalDate lastWeek = today.minusDays(7);
        int count=0;
        for (WatchEntry entry : entries ){
            LocalDate date = entry.getWatchedDate();

            if(!date.isBefore(lastWeek) && !date.isAfter(today)){
                count++;
            }
        }
        return count;
    }

    public int specificWatchesWeek(List<WatchEntry> entries, String mediaTitle){
         LocalDate today = LocalDate.now();
         LocalDate lastWeek = today.minusDays(7);
        int count =0;
        for (WatchEntry entry : entries){
            LocalDate date = entry.getWatchedDate();
            boolean isLastWeek = !date.isBefore(lastWeek)&& !date.isAfter(today);
            boolean isSameMedia = entry.getMedia().getTitle().equalsIgnoreCase(mediaTitle);
            if(isLastWeek&&isSameMedia) { count++; }
        }
        return count;
        }

    public double getPopularityScore(List<WatchEntry> entries, String mediaTitle, double rating){
        int totalWatches = totalWatchesWeek(entries);
        int mediaWatches = specificWatchesWeek(entries, mediaTitle);
        if(totalWatches==0) { return 0.0; }
        double popularityIndex=(double) mediaWatches/totalWatches;
        return popularityIndex*rating;
        }

    public double overallRating4Media(List<WatchEntry> entries, String mediaTitle){
        double sum=0.0;
        int count=0;

        for(WatchEntry entry : entries){
            if(entry.getMedia().getTitle().equalsIgnoreCase(mediaTitle) && entry.getRating()>0.0) {
                sum += entry.getRating();
                ;
                count++;
            }
        }
        if(count==0) return 0.0;
        return sum/count;
    }

    public void topWeek(List<WatchEntry> entries){
        Map<Media,Double> scores=new HashMap<>();

            for(Media media : data.getMediaLibrary()){
                String mediaTitle=media.getTitle();
                double overallSc=overallRating4Media(entries,mediaTitle);
                double popularitySc=getPopularityScore(entries, mediaTitle, overallSc);
                if(popularitySc>0.0) {scores.put(media,popularitySc); }
            }
        List<Map.Entry<Media,Double>> sortedScs = new ArrayList<>(scores.entrySet());
        sortedScs.sort(Map.Entry.<Media,Double>comparingByValue().reversed());
        System.out.println("Topul saptamanii : ");
        if(sortedScs.isEmpty()) { System.out.println("Nu exista vizionari"); return; }
        for(Map.Entry<Media,Double> entry : sortedScs) { System.out.println(entry.getKey().getTitle() + "- score: "+entry.getValue()); }
            //sortare and stuff
        }

        //if (media.getTitle().equalsIgnoreCase(normalizedTitle)) {
        //                return media;
        //            }

        //for (Media media : data.getMediaLibrary()) {
        //            System.out.println(media.getTitle());
        //        }

    public int totalWatchesMonth(List<WatchEntry> entries){
         LocalDate today = LocalDate.now();
         LocalDate lastMonth = today.minusMonths(1);
         int count= 0;
         for(WatchEntry entry : entries){
             LocalDate date = entry.getWatchedDate();
             if(!date.isBefore(lastMonth)&& !date.isAfter(today)){
                 count++;
             }
         }
         return count;
        }
        /*
        * pentru un media anume, voi lua rating urile din ultima saptamana deci LocalDate/Sysdate-RatingDate>=7
        * sa spunem ca are 70 rating uri si media ar fi 8.9
        *
        * */
        //double popularityScore=

    public List<Media> getRecommendationsForUser(String username, int topN) {
        User user = findUserByUsername(username);
        if (user == null) return new ArrayList<>();

        Set<String> watchedTitles = new HashSet<>();
        Map<String, List<Double>> genreRatings = new HashMap<>();

        for (WatchEntry entry : data.getWatchEntries()) {
            if (entry.getUser().getUsername().equalsIgnoreCase(username)) {
                watchedTitles.add(entry.getMedia().getTitle().toLowerCase(Locale.ROOT));
                if (entry.getRating() > 0.0) {
                    String genre = entry.getMedia().getGenre();
                    genreRatings.computeIfAbsent(genre, k -> new ArrayList<>()).add(entry.getRating());
                }
            }
        }

        Map<String, Double> genrePreference = new HashMap<>();
        for (Map.Entry<String, List<Double>> e : genreRatings.entrySet()) {
            double avg = e.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            genrePreference.put(e.getKey(), avg);
        }

        Map<Media, Double> scores = new HashMap<>();
        for (Media media : data.getMediaLibrary()) {
            if (watchedTitles.contains(media.getTitle().toLowerCase(Locale.ROOT))) continue;

            double genreScore = genrePreference.getOrDefault(media.getGenre(), 5.0);

            double communityRating = overallRating4Media(data.getWatchEntries(), media.getTitle());
            if (communityRating == 0.0) communityRating = 5.0;

            double popScore = getPopularityScore(data.getWatchEntries(), media.getTitle(), 10.0);

            double finalScore = (genreScore * 0.4) + (communityRating * 0.4) + (popScore * 0.2);
            scores.put(media, finalScore);
        }

        return scores.entrySet().stream()
                .sorted(Map.Entry.<Media, Double>comparingByValue().reversed())
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // ===== WATCHLIST =====

    public boolean addToWatchlist(String username, String mediaTitle) {
        User user = findUserByUsername(username);
        Media media = findMediaByExactTitle(mediaTitle);
        if (user == null || media == null) return false;
        if (user.isInWatchlist(media)) {
            System.out.println("Titlul este deja in watchlist.");
            return false;
        }
        return user.addToWatchlist(media);
    }

    public boolean removeFromWatchlist(String username, String mediaTitle) {
        User user = findUserByUsername(username);
        Media media = findMediaByExactTitle(mediaTitle);
        if (user == null || media == null) return false;
        return user.removeFromWatchlist(media);
    }

    public void showWatchlist(String username) throws UserNotFoundException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        if (user.getWatchlist().isEmpty()) {
            System.out.println("Watchlist-ul lui " + username + " este gol.");
            return;
        }
        System.out.println("\nWatchlist " + username + ":");
        user.getWatchlist().stream()
                .sorted(Comparator.comparing(Media::getTitle))
                .forEach(m -> {
                    String type = (m instanceof Movie) ? "Film" : "Serial";
                    double rating = overallRating4Media(data.getWatchEntries(), m.getTitle());
                    String ratingStr = rating > 0.0
                            ? String.format("%.1f/10", rating)
                            : "fara rating";
                    System.out.printf("  [%s] %s (%s) - Comunitate: %s%n",
                            type, m.getTitle(), m.getGenre(), ratingStr);
                });
    }

    public void showRecommendationsForUser(String username, int topN) throws UserNotFoundException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }

        List<Media> recommendations = getRecommendationsForUser(username, topN);

        if (recommendations.isEmpty()) {
            System.out.println("Nu exista recomandari disponibile.");
            return;
        }

        System.out.println("\nRecomandari personalizate pentru " + username + ":");
        System.out.println("(bazate pe preferintele tale de gen + ratingul comunitatii + popularitate)");

        int index = 1;
        for (Media media : recommendations) {
            double communityRating = overallRating4Media(data.getWatchEntries(), media.getTitle());
            String type = (media instanceof Movie) ? "Film" : "Serial";
            String ratingInfo = communityRating > 0.0
                    ? String.format("Rating comunitate: %.1f/10", communityRating)
                    : "Fara rating inca";

            System.out.printf("%d. [%s] %s (%s) - %s%n",
                    index++, type, media.getTitle(), media.getGenre(), ratingInfo);
        }
    }
}
