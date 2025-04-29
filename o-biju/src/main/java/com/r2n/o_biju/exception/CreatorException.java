package com.r2n.o_biju.exception;

public class CreatorException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    public CreatorException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
} 