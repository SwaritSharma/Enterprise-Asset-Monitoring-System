package com.enterprise.eams.alertmodule.exception;

public class AlertAlreadyResolvedException extends RuntimeException {
    public AlertAlreadyResolvedException(String message) {
        super(message);
    }
}
