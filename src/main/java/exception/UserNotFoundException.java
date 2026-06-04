package exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String username) {
        super("Utilizatorul '" + username + "' nu a fost gasit.");
    }
}
