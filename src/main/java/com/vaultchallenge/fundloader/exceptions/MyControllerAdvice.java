package com.vaultchallenge.fundloader.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class MyControllerAdvice {
    public ResponseEntity<String> handleEmptyInput(EmptyInputException fileNotFound) {
        return new ResponseEntity<String>("No input file was found", HttpStatus.EXPECTATION_FAILED);
    }
}
