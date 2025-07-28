package edu.ftn.iss.eventplanner.config;

import edu.ftn.iss.eventplanner.exceptions.BadRequestException;
import edu.ftn.iss.eventplanner.exceptions.InternalServerError;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object handleNotFound(NotFoundException ex) {
        return message(ex.getMessage());
    }

    @ExceptionHandler({InternalServerError.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleInternalServerError(Exception ex) {
        log.error("Internal Server Error", ex);
        return message("An unexpected server error has occurred.");
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleBadRequest(BadRequestException ex) {
        return message(ex.getMessage());
    }


    private static <T> Map<String, T> message(T message) {
        return Map.of("message", message);
    }

}
