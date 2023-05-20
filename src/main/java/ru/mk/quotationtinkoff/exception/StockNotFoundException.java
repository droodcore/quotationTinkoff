package ru.mk.quotationtinkoff.exception;


public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(String message) {
        super(message);
    }
}
