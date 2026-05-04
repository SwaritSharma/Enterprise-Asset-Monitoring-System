package com.enterprise.eams.alertmodule.exception;

public class AlertNotFoundException extends RuntimeException{
    public AlertNotFoundException(String message) {
        super(message);
    }
}
