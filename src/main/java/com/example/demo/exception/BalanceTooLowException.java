package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
public class BalanceTooLowException extends RuntimeException {

    public BalanceTooLowException() {super();}

    public BalanceTooLowException(String message, Throwable cause) {super(message, cause);}

    public BalanceTooLowException(String message) {super(message);}

    public BalanceTooLowException(Throwable cause) {super(cause);}
}
