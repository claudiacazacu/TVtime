import java.util.ArrayList;
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
            System.out.println(" --- TV Time --- ");
            System.out.println("1 - Afisare utilizatori ; ");
            System.out.println("2 - Afisare Media ; ");
            System.out.println("3 - Afisare Genuri ; ");
            System.out.println("4 - Setari avansate . ");
            System.out.println("5 - iesire!");
            System.out.println("\nOptiunea aleasa este ... ");
            optiune = scanner.nextInt();

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
                    int optiune2;
                    do{
                        System.out.println("---Setari Admin---");
                        System.out.println("1 - Adauga utilizator");
                        System.out.println("2 - Adauga film");
                        System.out.println("3 - Adauga serial");
                        //System.out.println("4 - Adauga gen");
                        System.out.println("5 - Inapoi");
                        System.out.println("optiunea aleasa...");
                        optiune2 = scanner.nextInt();
                        scanner.nextLine();
                        switch(optiune2){
                            case 1:
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

                            case 2:
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

                            case 3:
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

                            case 5:
                                System.out.println("revenim la primul meniu");
                                break;

                            default:
                                System.out.println("optiune invalida");
                        }
                    } while (optiune2 != 5);
                    break;

                case 5:
                    System.out.println("iesim");
                    break;

                default:
                    System.out.println("optiune invalida");

            }
        }while(optiune!=5);

        /*service.showAllUsers();
        service.showAllMedia();
        service.showAllGenres();*/
    }
}
