package com.enterprise.eams.usermodule.exception;

public class InvalidOtpOrSessionException extends RuntimeException {
    public InvalidOtpOrSessionException(String message) {
        super(message);
    }
}
