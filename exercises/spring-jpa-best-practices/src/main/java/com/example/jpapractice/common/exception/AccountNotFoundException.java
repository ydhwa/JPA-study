package com.example.jpapractice.common.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(long id) {
        super(id + " is not exist.");
    }
}
