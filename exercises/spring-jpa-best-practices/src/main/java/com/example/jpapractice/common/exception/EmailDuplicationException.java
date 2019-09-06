package com.example.jpapractice.common.exception;

import lombok.Getter;

@Getter
public class EmailDuplicationException extends RuntimeException {

    private String email;
    private String field;

    public EmailDuplicationException(String email) {
        this.field = "email";
        this.email = email;
    }
}
