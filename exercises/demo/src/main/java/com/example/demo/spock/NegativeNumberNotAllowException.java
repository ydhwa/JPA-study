package com.example.demo.spock;

public class NegativeNumberNotAllowException extends RuntimeException {

    NegativeNumberNotAllowException(String message) {
        super(message);
    }
}
