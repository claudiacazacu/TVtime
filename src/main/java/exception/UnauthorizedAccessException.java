package exception;

public class UnauthorizedAccessException extends Exception {

    public UnauthorizedAccessException(String username) {
        super("Utilizatorul '" + username + "' nu are permisiuni de administrator.");
    }
}
