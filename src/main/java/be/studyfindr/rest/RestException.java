package be.studyfindr.rest;

import org.springframework.http.HttpStatus;

/**
 * Created by anthony on 20/11/2016.
 */
public class RestException extends Exception {
    private static final long serialVersionUID = 1L;
    private String errorMessage;

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public RestException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public RestException() {
        super();
    }
}
