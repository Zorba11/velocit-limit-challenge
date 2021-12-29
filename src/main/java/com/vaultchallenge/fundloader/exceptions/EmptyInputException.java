package com.vaultchallenge.fundloader.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmptyInputException extends RuntimeException {
        private String errorCode;
        private String message;
}
