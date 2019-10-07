package com6441.team7.risc.api.exception;

public class ContinentParsingException extends RiscGameException{
    public ContinentParsingException(Exception rootCause) {
        super(rootCause);
    }

    public ContinentParsingException(String message, Exception rootCause) {
        super(message, rootCause);
    }

    public ContinentParsingException(String message) {
        super(message);
    }
}
