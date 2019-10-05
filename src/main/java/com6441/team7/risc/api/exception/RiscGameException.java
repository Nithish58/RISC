package com6441.team7.risc.api.exception;

public class RiscGameException extends RuntimeException {
    public RiscGameException(String message, Exception e) {
        super(message, e);
    }

    public RiscGameException(String message) {
        super(message);
    }

    public RiscGameException(Exception e) {
        super(e);
    }

}
