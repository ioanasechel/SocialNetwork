package socialnetwork.domain.validators;

public class AbsentObjectException extends Exception {
    public AbsentObjectException() {
    }

    public AbsentObjectException(String message) {
        super(message);
    }
}
