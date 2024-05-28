package com.tecnoinf.gestedu.exceptions;

public class TramiteNotFoundException extends RuntimeException {
    public TramiteNotFoundException(String message) {
        super(message);
    }
}