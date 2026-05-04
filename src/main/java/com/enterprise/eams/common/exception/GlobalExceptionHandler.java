package com.enterprise.eams.common.exception;

import com.enterprise.eams.alertmodule.exception.AlertAlreadyAcknowledgedException;
import com.enterprise.eams.alertmodule.exception.AlertNotFoundException;
import com.enterprise.eams.alertmodule.exception.AlertAlreadyResolvedException;
import com.enterprise.eams.assetmodule.exception.AssetNotFoundException;
import com.enterprise.eams.assetmodule.exception.DuplicateAssetException;
import com.enterprise.eams.assetmodule.exception.InvalidAssetAssignmentException;
import com.enterprise.eams.usermodule.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
            errors.put(error.getField(),error.getDefaultMessage()));

        ErrorResponse response=new ErrorResponse(
                400,
                "Validation Failed",
                errors,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRole(HttpMessageNotReadableException e) {
        ErrorResponse response=new ErrorResponse(
                400,
                "Invalid value for role. Allowed values: MANAGER, OPERATOR",
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        ErrorResponse response=new ErrorResponse(
                404,
                e.getMessage(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException e) {
        ErrorResponse response=new ErrorResponse(
                401,
                e.getMessage(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidOtpOrSessionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOtpOrSessionException(InvalidOtpOrSessionException e) {
        ErrorResponse response=new ErrorResponse(
                401,
                e.getMessage(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        ErrorResponse response=new ErrorResponse(
                409,
                e.getMessage(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserRoleSameException.class)
    public ResponseEntity<ErrorResponse> handleUserRoleSameException(UserRoleSameException e) {
        ErrorResponse response=new ErrorResponse(
                409,
                e.getMessage(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AssetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAssetNotFoundException(AssetNotFoundException e) {
        ErrorResponse response=new ErrorResponse(
                404,
                e.getMessage(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAssetAssignmentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAssetAssignmentException(InvalidAssetAssignmentException e) {
        ErrorResponse response=new ErrorResponse(
                400,
                e.getMessage(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateAssetException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateAssetException(DuplicateAssetException e) {
        ErrorResponse response=new ErrorResponse(
                400,
                e.getMessage(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlertNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAlertNotFoundException(AlertNotFoundException e) {
        ErrorResponse response=new ErrorResponse(
                404,
                e.getMessage(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlertAlreadyResolvedException.class)
    public ResponseEntity<ErrorResponse> handleAlertAlreadyResolvedException(AlertAlreadyResolvedException e) {
        ErrorResponse response=new ErrorResponse(
                404,
                e.getMessage(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlertAlreadyAcknowledgedException.class)
    public ResponseEntity<ErrorResponse> handleAlertAlreadyAcknowledgedException(AlertAlreadyAcknowledgedException e) {
        ErrorResponse response=new ErrorResponse(
                404,
                e.getMessage(),
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


}
