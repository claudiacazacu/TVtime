package exception;

public class DuplicateMediaException extends Exception {

    public DuplicateMediaException(String title) {
        super("Productia '" + title + "' exista deja in biblioteca.");
    }
}
