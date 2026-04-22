import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        FileService fs = new FileService();
        Scanner scanner = new Scanner(System.in);
        /*fs.readUsers("data/users.txt");
        fs.readSeries("data/series.txt");
        fs.readMovies("data/movies.txt");*/
        ArrayList<User> users = fs.readUsers("data/users.txt");
        ArrayList<Series> series = fs.readSeries("data/series.txt");
        ArrayList<Movie> movies = fs.readMovies("data/movies.txt");

        Service service = new Service();

        for (User user: users) {
            service.addUser(user);
        }

        for (Series s : series) {
            service.addMedia(s);
        }

        for (Movie m : movies) {
            service.addMedia(m);
        }

        /*User user = new User("Ana", 20, "ana@mail.com");
        service.addUser(user);

        Series series = new Series(
                "Desperate Housewives",
                "2004",
                "Drama",
                "A suburban mystery-drama series.",
                "Marc Cherry",
                "ABC Studios"
        );

        Movie movie = new Movie(
                "Interstellar",
                "2014",
                "Sci-Fi",
                "Space exploration movie",
                "Christopher Nolan",
                "Warner Bros"
        );

        service.addMedia(series);
        service.addMedia(movie);*/

        int optiune;
        do{
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
            System.out.println("14 - Iesire");
            System.out.print("\nOptiunea aleasa este ... ");
            optiune = scanner.nextInt();
            scanner.nextLine();

            switch (optiune){
                case 1:
                    service.showAllUsers();
                    break;

                case 2:
                    service.showAllMedia();
                    break;

                case 3:
                    service.showAllGenres();
                    break;

                case 4:
                    System.out.print("Username: ");
                    String username = scanner.nextLine();

                    System.out.print("Age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Email: ");
                    String email = scanner.nextLine();

                    User newUser = new User(username, age, email);
                    service.addUser(newUser);

                    System.out.println("Utilizator adaugat cu succes.");
                    break;

                case 5:
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

                    Movie newMovie = new Movie(
                            movieTitle,
                            movieReleaseDate,
                            movieGenre,
                            movieDescription,
                            movieDirector,
                            movieCompany
                    );

                    service.addMedia(newMovie);
                    System.out.println("Filmul a fost adaugat cu succes.");
                    break;

                case 6:
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

                    Series newSeries = new Series(
                            seriesTitle,
                            seriesReleaseDate,
                            seriesGenre,
                            seriesDescription,
                            seriesDirector,
                            seriesCompany
                    );

                    service.addMedia(newSeries);
                    System.out.println("Serialul a fost adaugat cu succes.");
                    break;

                case 7:
                    System.out.print("Introdu titlul cautat: ");
                    String titleQuery = scanner.nextLine();

                    List<Media> rezultate = service.searchMediaByTitle(titleQuery);

                    if (rezultate.isEmpty()) {
                        System.out.println("Nu s-a gasit nicio productie.");
                    } else {
                        System.out.println("Rezultatele cautarii sunt:");
                        for (Media media : rezultate) {
                            System.out.println(media);
                        }
                    }
                    break;

                case 8:
                    service.showMovies();
                    break;

                case 9:
                    service.showSeries();
                    break;

                case 10:
                    System.out.print("Introdu genul dorit: ");
                    String genreQuery = scanner.nextLine();

                    List<Media> mediaFiltrata = service.filterMediaByGenre(genreQuery);

                    if (mediaFiltrata.isEmpty()) {
                        System.out.println("Nu exista productii pentru genul introdus.");
                    } else {
                        System.out.println("Productiile din genul ales sunt:");
                        for (Media media : mediaFiltrata) {
                            System.out.println(media);
                        }
                    }
                    break;

                case 11:
                    System.out.print("Introdu titlul productiei: ");
                    String mediaTitleForCast = scanner.nextLine();
                    service.showCastForMedia(mediaTitleForCast);
                    break;

                case 12:
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

                    Episode episode = new Episode(
                            episodeTitle,
                            episodeReleaseDate,
                            episodeNumber,
                            seasonNumber,
                            duration
                    );

                    boolean addedEpisode = service.addEpisodeToSeries(targetSeries, episode);

                    if (addedEpisode) {
                        System.out.println("Episod adaugat cu succes.");
                    } else {
                        System.out.println("Nu s-a putut adauga episodul. Verifica titlul serialului.");
                    }
                    break;

                case 13:
                    System.out.println("Creare watch entry pentru review.");

                    System.out.print("Username: ");
                    String reviewUsername = scanner.nextLine();

                    System.out.print("Titlu media: ");
                    String reviewMediaTitle = scanner.nextLine();

                    System.out.print("Titlu episod (sau lasa gol daca e film): ");
                    String reviewEpisodeTitle = scanner.nextLine();

                    System.out.print("Data vizionarii: ");
                    String watchedDate = scanner.nextLine();

                    System.out.print("Rating (0-10): ");
                    double rating = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.print("Autor comentariu: ");
                    String commentAuthor = scanner.nextLine();

                    System.out.print("Text comentariu: ");
                    String commentText = scanner.nextLine();

                    User foundUser = null;
                    for (User user : users) {
                        if (user.getUsername().equalsIgnoreCase(reviewUsername)) {
                            foundUser = user;
                            break;
                        }
                    }

                    Media foundMedia = null;
                    List<Media> allMatches = service.searchMediaByTitle(reviewMediaTitle);
                    for (Media media : allMatches) {
                        if (media.getTitle().equalsIgnoreCase(reviewMediaTitle)) {
                            foundMedia = media;
                            break;
                        }
                    }

                    Episode foundEpisode = null;
                    if (foundMedia instanceof Series && !reviewEpisodeTitle.trim().isEmpty()) {
                        Series foundSeries = (Series) foundMedia;
                        for (Episode ep : foundSeries.getEpisodes()) {
                            if (ep.getTitle().equalsIgnoreCase(reviewEpisodeTitle)) {
                                foundEpisode = ep;
                                break;
                            }
                        }
                    }

                    if (foundUser == null) {
                        System.out.println("Utilizatorul nu exista.");
                        break;
                    }

                    if (foundMedia == null) {
                        System.out.println("Media nu exista.");
                        break;
                    }

                    WatchEntry watchEntry = new WatchEntry(foundUser, foundMedia, foundEpisode, watchedDate);
                    Comment comment = new Comment(commentAuthor, commentText);

                    boolean reviewAdded = service.addReviewToWatchEntry(watchEntry, rating, comment);

                    if (reviewAdded) {
                        System.out.println("Review adaugat cu succes:");
                        System.out.println(watchEntry);
                    } else {
                        System.out.println("Nu s-a putut adauga review-ul.");
                    }
                    break;

                case 14:
                    System.out.println("iesim");
                    break;

                default:
                    System.out.println("optiune invalida");

            }
        }while(optiune!=14);

        /*service.showAllUsers();
        service.showAllMedia();
        service.showAllGenres();*/
    }
}