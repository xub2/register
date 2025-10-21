package register.register.exception;

public class RegisterNotStartedException extends RuntimeException {

    public RegisterNotStartedException() {
        super();
    }

    public RegisterNotStartedException(String message) {
        super(message);
    }

    public RegisterNotStartedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegisterNotStartedException(Throwable cause) {
        super(cause);
    }
}
