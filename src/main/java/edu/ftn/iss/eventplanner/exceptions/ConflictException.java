package edu.ftn.iss.eventplanner.exceptions;

public class ConflictException extends RuntimeException {

    public ConflictException() {
        super();
    }

    public ConflictException(String msg) {
        super(msg);
    }

    public ConflictException(Throwable cause) {
        super(cause);
    }

    public ConflictException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
