package pl.bartoszmech.infrastructure.exceptions;

import lombok.Getter;

@Getter
public class UsernameValidationException extends RuntimeException {

    public UsernameValidationException(String message) {
        super(message);
    }

}
