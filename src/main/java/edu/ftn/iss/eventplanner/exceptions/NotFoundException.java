package edu.ftn.iss.eventplanner.exceptions;

public class NotFoundException extends RuntimeException {

  public NotFoundException() {
    super();
  }

  public NotFoundException(String msg) {
    super(msg);
  }

  public NotFoundException(Throwable cause) {
    super(cause);
  }

  public NotFoundException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
