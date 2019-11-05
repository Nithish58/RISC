package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in the game
 */
public class RiscGameException extends RuntimeException {

    /**
     * constructor of RiscGameException
     * @param message String
     * @param e Exception
     */
    public RiscGameException(String message, Exception e) {
        super(message, e);
    }

    /**
     * constructor of RiscGameException
     * @param message String
     */
    public RiscGameException(String message) {
        super(message);
    }

    /**
     * constructor of RiscGameException
     * @param e Exception
     */
    public RiscGameException(Exception e) {
        super(e);
    }

}
