package com.luizsolely.traingenius.exception;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
