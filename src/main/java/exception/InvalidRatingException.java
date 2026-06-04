package exception;

public class InvalidRatingException extends Exception {

    public InvalidRatingException(double rating) {
        super("Rating invalid: " + rating + ". Valoarea trebuie sa fie intre 0.0 si 10.0.");
    }
}
