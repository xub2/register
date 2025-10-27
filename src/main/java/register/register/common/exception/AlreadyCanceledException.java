package register.register.common.exception;

public class AlreadyCanceledException extends RuntimeException {
    public AlreadyCanceledException() {
        super();
    }

    public AlreadyCanceledException(String message) {
        super(message);
    }

    public AlreadyCanceledException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyCanceledException(Throwable cause) {
        super(cause);
    }
}
