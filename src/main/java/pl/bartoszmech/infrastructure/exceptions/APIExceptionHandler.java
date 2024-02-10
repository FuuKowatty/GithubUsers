package pl.bartoszmech.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler(ExternalAPIException.class)
    @ResponseBody
    public ResponseEntity<ExternalAPIException> handleFetchFromExternalAPIException(ExternalAPIException e) {
        return ResponseEntity.status(e.statusCode).body(e);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ExternalAPIException> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ExternalAPIException(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

}
