package esi.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    @Override
    public String getMessage() {
        return message;
    }

    private final String message;

    public ResourceNotFoundException(String message) {
        this.message = message;
    }
}