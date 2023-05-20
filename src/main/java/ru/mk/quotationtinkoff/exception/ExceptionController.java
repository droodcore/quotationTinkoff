package ru.mk.quotationtinkoff.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
    @ExceptionHandler({StockNotFoundException.class})
    public ResponseEntity<ErrorDto> handleStockNotFoundException(StockNotFoundException e) {
        return new ResponseEntity<>(new ErrorDto(e.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }
}
