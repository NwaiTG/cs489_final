package com.nwai.dentalsys.exception;

public class DataAlreadyExistedException extends RuntimeException {
    public DataAlreadyExistedException(String message) {
        super(message);
    }
}
