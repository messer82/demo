package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
public class AmountTooLargeException extends RuntimeException {

    public AmountTooLargeException(String message) {super(message);}

}
