package cl.exercise.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ResourceException extends Exception {
    private static final long serialVersionUID = 1L;

    public ResourceException(String message) {
        super(message);
    }
}
