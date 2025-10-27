package register.register.common.exception;

public class CourseCapacityFullException extends RuntimeException {
    public CourseCapacityFullException() {
        super();
    }

    public CourseCapacityFullException(String message) {
        super(message);
    }

    public CourseCapacityFullException(String message, Throwable cause) {
        super(message, cause);
    }

    public CourseCapacityFullException(Throwable cause) {
        super(cause);
    }
}
