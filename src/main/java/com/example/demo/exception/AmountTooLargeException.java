package com.example.demo.exception;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
public class AmountTooLargeException extends RuntimeException {

    public AmountTooLargeException(){super();}

    public AmountTooLargeException(String message) {super(message);}

    public AmountTooLargeException(String message, Throwable cause) {super(message, cause);}

}
