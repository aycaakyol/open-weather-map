package co.mobileaction.openweathermap.advice;

import co.mobileaction.openweathermap.exception.QueryOutOfRangeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(QueryOutOfRangeException.class)
    public ResponseEntity<String> handleQueryOutOfRangeException(QueryOutOfRangeException ex) {
        String errorMessage = ex.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
