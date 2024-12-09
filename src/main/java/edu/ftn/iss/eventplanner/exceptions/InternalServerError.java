package edu.ftn.iss.eventplanner.exceptions;

public class InternalServerError extends RuntimeException {

    public InternalServerError() {
        super();
    }

    public InternalServerError(String msg) {
        super(msg);
    }

    public InternalServerError(Throwable cause) {
        super(cause);
    }

    public InternalServerError(String msg, Throwable cause) {
        super(msg, cause);
    }
}
