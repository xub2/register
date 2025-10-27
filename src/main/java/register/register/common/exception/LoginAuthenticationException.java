package register.register.common.exception;

public class LoginAuthenticationException extends RuntimeException {
    public LoginAuthenticationException() {
        super();
    }

    public LoginAuthenticationException(String message) {
        super(message);
    }

    public LoginAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAuthenticationException(Throwable cause) {
        super(cause);
    }
}
