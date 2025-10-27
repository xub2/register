package register.register.common.exception;

public class RegisterClosedException extends RuntimeException {

    public RegisterClosedException() {
        super();
    }

    public RegisterClosedException(String message) {
        super(message);
    }

    public RegisterClosedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegisterClosedException(Throwable cause) {
        super(cause);
    }
}
