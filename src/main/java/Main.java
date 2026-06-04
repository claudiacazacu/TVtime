import config.DatabaseConnection;
import exception.*;
import model.Admin;
import model.Comment;
import model.Episode;
import model.Media;
import model.Movie;
import model.Series;
import model.User;
import model.WatchEntry;
import repository.EpisodeRepository;
import repository.MediaRepository;
import repository.UserRepository;
import repository.WatchEntryRepository;
import service.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            System.out.println("Conectat la PostgreSQL: " + !conn.isClosed());
        } catch (SQLException e) {
            System.out.println("Eroare conexiune BD: " + e.getMessage());
        }

        FileService fs = new FileService();
        Scanner scanner = new Scanner(System.in);

        ArrayList<User> users = fs.readUsers("data/users.txt");
        ArrayList<Series> series = fs.readSeries("data/series.txt");
        ArrayList<Movie> movies = fs.readMovies("data/movies.txt");

        AuditService audit = AuditService.getInstance();

        ServiceData serviceData = new ServiceData();
        UserService userService = new UserService(serviceData);
        AdminService adminService = new AdminService(serviceData);
        Admin admin = new Admin("admin", 30, "admin@tvtime.com");

        for (User user : users) {
            userService.addUser(user);
        }
        for (Series s : series) {
            try {
                adminService.addMedia(admin, s);
            } catch (UnauthorizedAccessException | DuplicateMediaException e) {
                System.out.println("Eroare la incarcare date: " + e.getMessage());
            }
        }
        for (Movie m : movies) {
            try {
                adminService.addMedia(admin, m);
            } catch (UnauthorizedAccessException | DuplicateMediaException e) {
                System.out.println("Eroare la incarcare date: " + e.getMessage());
            }
        }

        int optiune;
        do {
            System.out.println("\n--- TV Time ---");
            System.out.println("1 - Afisare utilizatori");
            System.out.println("2 - Afisare media");
            System.out.println("3 - Afisare genuri");
            System.out.println("4 - Adauga utilizator");
            System.out.println("5 - Adauga film");
            System.out.println("6 - Adauga serial");
            System.out.println("7 - Cauta media dupa titlu");
            System.out.println("8 - Afiseaza doar filme");
            System.out.println("9 - Afiseaza doar seriale");
            System.out.println("10 - Filtreaza media dupa gen");
            System.out.println("11 - Afiseaza cast pentru o productie");
            System.out.println("12 - Adauga episod la un serial");
            System.out.println("13 - Adauga rating/comment la un watch entry");
            System.out.println("14 - Creare watch entry");
            System.out.println("15 - Afiseaza comentarii pentru o productie");
            System.out.println("16 - Afiseaza profil utilizator");
            System.out.println("17 - Top saptamana (cele mai populare titluri)");
            System.out.println("18 - Recomandari personalizate pentru un utilizator");
            System.out.println("19 - Adauga la watchlist");
            System.out.println("20 - Afiseaza watchlist utilizator");
            System.out.println("21 - Sterge din watchlist");
            System.out.println("22 - Iesire");
            System.out.print("\nOptiunea aleasa este ... ");
            optiune = scanner.nextInt();
            scanner.nextLine();

            switch (optiune) {
                case 1:
                    audit.log("afisare_utilizatori");
                    userService.showAllUsers();
                    break;

                case 2:
                    audit.log("afisare_media");
                    userService.showAllMedia();
                    break;

                case 3:
                    audit.log("afisare_genuri");
                    userService.showAllGenres();
                    break;

                case 4:
                    audit.log("adauga_utilizator");
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    User newUser = new User(username, age, email);
                    userService.addUser(newUser);
                    try {
                        UserRepository.getInstance().create(newUser);
                    } catch (java.sql.SQLException e) {
                        System.out.println("[DB] Eroare la salvare user: " + e.getMessage());
                    }
                    System.out.println("Utilizator adaugat cu succes.");
                    break;

                case 5:
                    audit.log("adauga_film");
                    System.out.print("Titlu film: ");
                    String movieTitle = scanner.nextLine();
                    System.out.print("Data lansarii: ");
                    String movieReleaseDate = scanner.nextLine();
                    System.out.print("Gen: ");
                    String movieGenre = scanner.nextLine();
                    System.out.print("Descriere: ");
                    String movieDescription = scanner.nextLine();
                    System.out.print("Director: ");
                    String movieDirector = scanner.nextLine();
                    System.out.print("Companie: ");
                    String movieCompany = scanner.nextLine();
                    Movie newMovie = new Movie(movieTitle, movieReleaseDate, movieGenre, movieDescription, movieDirector, movieCompany);
                    try {
                        adminService.addMedia(admin, newMovie);
                        MediaRepository.getInstance().create(newMovie);
                        System.out.println("Filmul a fost adaugat cu succes.");
                    } catch (DuplicateMediaException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    } catch (UnauthorizedAccessException e) {
                        System.out.println("Acces interzis: " + e.getMessage());
                    } catch (java.sql.SQLException e) {
                        System.out.println("[DB] Eroare la salvare film: " + e.getMessage());
                    }
                    break;

                case 6:
                    audit.log("adauga_serial");
                    System.out.print("Titlu serial: ");
                    String seriesTitle = scanner.nextLine();
                    System.out.print("Data lansarii: ");
                    String seriesReleaseDate = scanner.nextLine();
                    System.out.print("Gen: ");
                    String seriesGenre = scanner.nextLine();
                    System.out.print("Descriere: ");
                    String seriesDescription = scanner.nextLine();
                    System.out.print("Director: ");
                    String seriesDirector = scanner.nextLine();
                    System.out.print("Companie: ");
                    String seriesCompany = scanner.nextLine();
                    Series newSeries = new Series(seriesTitle, seriesReleaseDate, seriesGenre, seriesDescription, seriesDirector, seriesCompany);
                    try {
                        adminService.addMedia(admin, newSeries);
                        MediaRepository.getInstance().create(newSeries);
                        System.out.println("Serialul a fost adaugat cu succes.");
                    } catch (DuplicateMediaException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    } catch (UnauthorizedAccessException e) {
                        System.out.println("Acces interzis: " + e.getMessage());
                    } catch (java.sql.SQLException e) {
                        System.out.println("[DB] Eroare la salvare serial: " + e.getMessage());
                    }
                    break;

                case 7:
                    audit.log("cauta_media_dupa_titlu");
                    System.out.print("Introdu titlul cautat: ");
                    String titleQuery = scanner.nextLine();
                    List<Media> rezultate = userService.searchMediaByTitle(titleQuery);
                    if (rezultate.isEmpty()) {
                        System.out.println("Nu s-a gasit nicio productie.");
                    } else {
                        System.out.println("Rezultatele cautarii sunt:");
                        for (Media media : rezultate) System.out.println(media);
                    }
                    break;

                case 8:
                    audit.log("afisare_filme");
                    userService.showMovies();
                    break;

                case 9:
                    audit.log("afisare_seriale");
                    userService.showSeries();
                    break;

                case 10:
                    audit.log("filtrare_media_dupa_gen");
                    System.out.print("Introdu genul dorit: ");
                    String genreQuery = scanner.nextLine();
                    List<Media> mediaFiltrata = userService.filterMediaByGenre(genreQuery);
                    if (mediaFiltrata.isEmpty()) {
                        System.out.println("Nu exista productii pentru genul introdus.");
                    } else {
                        System.out.println("Productiile din genul ales sunt:");
                        for (Media media : mediaFiltrata) System.out.println(media);
                    }
                    break;

                case 11:
                    audit.log("afisare_cast");
                    System.out.print("Introdu titlul productiei: ");
                    String mediaTitleForCast = scanner.nextLine();
                    userService.showCastForMedia(mediaTitleForCast);
                    break;

                case 12:
                    audit.log("adauga_episod");
                    System.out.print("Titlul serialului: ");
                    String targetSeries = scanner.nextLine();
                    System.out.print("Titlul episodului: ");
                    String episodeTitle = scanner.nextLine();
                    System.out.print("Data lansarii episodului: ");
                    String episodeReleaseDate = scanner.nextLine();
                    System.out.print("Numarul episodului: ");
                    int episodeNumber = scanner.nextInt();
                    System.out.print("Numarul sezonului: ");
                    int seasonNumber = scanner.nextInt();
                    System.out.print("Durata episodului (minute): ");
                    int duration = scanner.nextInt();
                    scanner.nextLine();
                    Episode newEpisode = new Episode(episodeTitle, episodeReleaseDate, episodeNumber, seasonNumber, duration);
                    try {
                        adminService.addEpisode(admin, targetSeries, newEpisode);
                        int seriesId = MediaRepository.getInstance().findIdByTitle(targetSeries);
                        if (seriesId != -1) {
                            EpisodeRepository.getInstance().create(newEpisode, seriesId);
                        }
                        System.out.println("Episod adaugat cu succes.");
                    } catch (MediaNotFoundException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    } catch (UnauthorizedAccessException e) {
                        System.out.println("Acces interzis: " + e.getMessage());
                    } catch (java.sql.SQLException e) {
                        System.out.println("[DB] Eroare la salvare episod: " + e.getMessage());
                    }
                    break;

                case 13:
                    audit.log("adauga_rating_comment");
                    System.out.println("Creare watch entry pentru review.");
                    System.out.print("Username: ");
                    String reviewUsername = scanner.nextLine();
                    System.out.print("Titlu media: ");
                    String reviewMediaTitle = scanner.nextLine();
                    System.out.print("Titlu episod (sau lasa gol daca e film): ");
                    String reviewEpisodeTitle = scanner.nextLine();
                    System.out.print("Data vizionarii: ");
                    scanner.nextLine();
                    System.out.print("Rating (0-10): ");
                    double rating = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Autor comentariu: ");
                    String commentAuthor = scanner.nextLine();
                    System.out.print("Text comentariu: ");
                    String commentText = scanner.nextLine();
                    User foundUser = userService.findUserByUsername(reviewUsername);
                    Media foundMedia = userService.findMediaByExactTitle(reviewMediaTitle);
                    if (foundUser == null) { System.out.println("Utilizatorul nu exista."); break; }
                    if (foundMedia == null) { System.out.println("Media nu exista."); break; }
                    Episode foundEpisode = null;
                    if (foundMedia instanceof Series && !reviewEpisodeTitle.trim().isEmpty()) {
                        foundEpisode = userService.findEpisodeByTitle((Series) foundMedia, reviewEpisodeTitle);
                    }
                    WatchEntry watchEntry = new WatchEntry(foundUser, foundMedia, foundEpisode, LocalDate.now());
                    try {
                        userService.addRating(watchEntry, rating);
                        userService.addComment(watchEntry, new Comment(commentAuthor, commentText));
                        userService.addWatchEntry(watchEntry);
                        int mediaId = MediaRepository.getInstance().findIdByTitle(foundMedia.getTitle());
                        if (mediaId != -1) {
                            WatchEntryRepository.getInstance().create(watchEntry, mediaId, null, null);
                        }
                        System.out.println("Review adaugat cu succes:\n" + watchEntry);
                    } catch (InvalidRatingException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    } catch (java.sql.SQLException e) {
                        System.out.println("[DB] Eroare la salvare review: " + e.getMessage());
                    }
                    break;

                case 14:
                    audit.log("creare_watch_entry");
                    System.out.println("Creare watch entry.");
                    System.out.print("Username: ");
                    String watchUsername = scanner.nextLine();
                    System.out.print("Titlu media: ");
                    String watchMediaTitle = scanner.nextLine();
                    System.out.print("Titlu episod (sau lasa gol daca nu exista): ");
                    String watchEpisodeTitle = scanner.nextLine();
                    System.out.print("Data vizionarii: ");
                    scanner.nextLine();
                    System.out.print("Rating (0-10): ");
                    double watchRating = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Text comentariu (sau lasa gol): ");
                    String watchCommentText = scanner.nextLine();
                    System.out.print("Personaj favorit (sau lasa gol): ");
                    String favoriteCharacterName = scanner.nextLine();
                    User watchUser = userService.findUserByUsername(watchUsername);
                    if (watchUser == null) { System.out.println("Utilizatorul nu exista."); break; }
                    Media watchMedia = userService.findMediaByExactTitle(watchMediaTitle);
                    if (watchMedia == null) { System.out.println("Media nu exista."); break; }
                    Episode watchEpisode = null;
                    if (watchMedia instanceof Series && !watchEpisodeTitle.trim().isEmpty()) {
                        watchEpisode = userService.findEpisodeByTitle((Series) watchMedia, watchEpisodeTitle);
                        if (watchEpisode == null) { System.out.println("Episodul nu exista."); break; }
                    }
                    WatchEntry newWatchEntry = new WatchEntry(watchUser, watchMedia, watchEpisode, LocalDate.now());
                    try {
                        userService.addRating(newWatchEntry, watchRating);
                        if (!watchCommentText.trim().isEmpty()) {
                            userService.addComment(newWatchEntry, new Comment(watchUser.getUsername(), watchCommentText));
                        }
                        if (!favoriteCharacterName.trim().isEmpty()) {
                            for (model.Character character : watchMedia.getCast().values()) {
                                if (character.getName().equalsIgnoreCase(favoriteCharacterName)) {
                                    newWatchEntry.setFavCharacter(character);
                                    break;
                                }
                            }
                        }
                        userService.addWatchEntry(newWatchEntry);
                        int wMediaId = MediaRepository.getInstance().findIdByTitle(watchMedia.getTitle());
                        if (wMediaId != -1) {
                            WatchEntryRepository.getInstance().create(newWatchEntry, wMediaId, null, null);
                        }
                        System.out.println("Watch entry adaugat cu succes.");
                    } catch (InvalidRatingException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    } catch (java.sql.SQLException e) {
                        System.out.println("[DB] Eroare la salvare watch entry: " + e.getMessage());
                    }
                    break;

                case 15:
                    audit.log("afisare_comentarii");
                    System.out.print("Introdu titlul productiei: ");
                    String mediaTitleForComments = scanner.nextLine();
                    try {
                        userService.showCommentsForMedia(mediaTitleForComments);
                    } catch (MediaNotFoundException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 16:
                    audit.log("afisare_profil_utilizator");
                    System.out.print("Introdu username-ul: ");
                    String profileUsername = scanner.nextLine();
                    try {
                        userService.showUserProfile(profileUsername);
                    } catch (UserNotFoundException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 17:
                    audit.log("top_saptamana");
                    userService.topWeek(userService.getData().getWatchEntries());
                    break;

                case 18:
                    audit.log("recomandari_personalizate");
                    System.out.print("Username pentru recomandari: ");
                    String recUsername = scanner.nextLine();
                    System.out.print("Cate recomandari doresti? ");
                    int recCount = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        userService.showRecommendationsForUser(recUsername, recCount);
                    } catch (UserNotFoundException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 19:
                    audit.log("adauga_in_watchlist");
                    System.out.print("Username: ");
                    String wlAddUser = scanner.nextLine();
                    System.out.print("Titlu media: ");
                    String wlAddTitle = scanner.nextLine();
                    if (userService.addToWatchlist(wlAddUser, wlAddTitle)) {
                        System.out.println("Adaugat in watchlist cu succes.");
                    } else {
                        System.out.println("Nu s-a putut adauga. Verifica username-ul si titlul.");
                    }
                    break;

                case 20:
                    audit.log("afisare_watchlist");
                    System.out.print("Username: ");
                    String wlShowUser = scanner.nextLine();
                    try {
                        userService.showWatchlist(wlShowUser);
                    } catch (UserNotFoundException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case 21:
                    audit.log("sterge_din_watchlist");
                    System.out.print("Username: ");
                    String wlRemUser = scanner.nextLine();
                    System.out.print("Titlu media: ");
                    String wlRemTitle = scanner.nextLine();
                    if (userService.removeFromWatchlist(wlRemUser, wlRemTitle)) {
                        System.out.println("Titlul a fost sters din watchlist.");
                    } else {
                        System.out.println("Nu s-a putut sterge. Verifica username-ul si titlul.");
                    }
                    break;

                case 22:
                    System.out.println("iesim");
                    break;

                default:
                    System.out.println("optiune invalida");
            }
        } while (optiune != 22);
    }
}
