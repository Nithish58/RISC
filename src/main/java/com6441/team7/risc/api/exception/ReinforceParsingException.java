package com6441.team7.risc.api.exception;

public class ReinforceParsingException extends RiscGameException {

    public ReinforceParsingException(String message, Exception e) {
        super(message, e);
    }

    public ReinforceParsingException(String message) {
        super(message);
    }

    public ReinforceParsingException(Exception e) {
        super(e);
    }
}
