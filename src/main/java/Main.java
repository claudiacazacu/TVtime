public class Main {

    public static void main(String[] args) {
        Service service = new Service();

        User user = new User("Ana", 20, "ana@mail.com");
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
        service.addMedia(movie);

        service.showAllUsers();
        service.showAllMedia();
        service.showAllGenres();
    }
}
