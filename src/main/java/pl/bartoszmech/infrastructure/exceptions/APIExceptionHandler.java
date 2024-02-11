package pl.bartoszmech.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.bartoszmech.application.response.ErrorResponseExternalAPI;

@ControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler(ExternalAPIException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseExternalAPI> handleFetchFromExternalAPIException(ExternalAPIException e) {
        return ResponseEntity.status(e.statusCode).body(new ErrorResponseExternalAPI(e.statusCode, e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseExternalAPI> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponseExternalAPI(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

}
