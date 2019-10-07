package com6441.team7.risc.api.exception;

public class MissingInfoException extends RiscGameException {
    public MissingInfoException(Exception rootCause) {
        super(rootCause);
    }



    public MissingInfoException(String message, Exception rootCause) {
        super(message, rootCause);
    }

    public MissingInfoException(String message) {
        super(message);
    }
}
