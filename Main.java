public class Main {

    public static void main(String[] args) {

        Service service = new Service();

        User u1 = new User("Ana", 20, "ana@mail.com");
        service.addUser(u1);

        Series series = new Series(
                "Desperate Housewives",
                "Gossip Girl",
                "How I Met Your Mother",
                "Revenge",
                "And Just Like That",
                "Suits"
        );

        Movie movie = new Movie(
                "Interstellar",
                "2014",
                "Sci-Fi",
                "Space exploration movie",
                "Christopher Nolan",
                "Warner Bros"
        );

        service.addMedia(movie);

        service.showAllUsers();
        service.showAllMedia();
    }
}