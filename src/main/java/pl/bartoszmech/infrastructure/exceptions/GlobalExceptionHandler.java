package pl.bartoszmech.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.bartoszmech.application.response.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExternalAPIException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleFetchFromExternalAPIException(ExternalAPIException e) {
        return ResponseEntity.status(e.statusCode).body(new ErrorResponse(e.statusCode, e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(UsernameValidationException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleUsernameValidationException(UsernameValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

}
