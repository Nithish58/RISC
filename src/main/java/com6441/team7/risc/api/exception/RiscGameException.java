package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in the game
 */
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
