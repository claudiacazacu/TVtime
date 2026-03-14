import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        FileService fs = new FileService();
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

        service.showAllUsers();
        service.showAllMedia();
        service.showAllGenres();
    }
}
