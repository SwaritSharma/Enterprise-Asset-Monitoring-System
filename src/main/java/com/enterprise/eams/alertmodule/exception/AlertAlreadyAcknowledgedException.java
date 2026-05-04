package com.enterprise.eams.alertmodule.exception;

public class AlertAlreadyAcknowledgedException extends RuntimeException {
    public AlertAlreadyAcknowledgedException(String message) {
        super(message);
    }
}
