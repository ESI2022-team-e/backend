package esi.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    @Override
    public String getMessage() {
        return message;
    }

    private final String message;
    public UnauthorizedException(String message) {
        this.message=message;
    }
}
