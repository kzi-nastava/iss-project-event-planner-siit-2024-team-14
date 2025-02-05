package edu.ftn.iss.eventplanner.exceptions;

public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super();
    }

    public BadRequestException(String msg) {
        super(msg);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

    public BadRequestException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
