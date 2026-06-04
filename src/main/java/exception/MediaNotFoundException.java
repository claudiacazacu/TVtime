package exception;

public class MediaNotFoundException extends Exception {

    public MediaNotFoundException(String title) {
        super("Productia cu titlul '" + title + "' nu a fost gasita.");
    }
}
