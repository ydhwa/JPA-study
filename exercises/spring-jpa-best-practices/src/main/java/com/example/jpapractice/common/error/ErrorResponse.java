package com.example.jpapractice.common.error;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String message;
    private String code;
    private int status;
    private List<FieldError> errors = new ArrayList<>();

    @Builder
    public ErrorResponse(String message, String code, int status, List<FieldError> errors) {
        this.message = message;
        this.code = code;
        this.status = status;
        this.errors = initErrors(errors);
    }

    private List<FieldError> initErrors(List<FieldError> errors) {
        return (errors == null) ? new ArrayList<>() : errors;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        @Builder
        public FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
    }
}
