package com.example.exception;

public class AccountAlreadyExistsException extends AccountException {
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}
