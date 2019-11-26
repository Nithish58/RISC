package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in the game
 */
public class RiscGameException extends RuntimeException {

    /**
     * constructor of RiscGameException
     * @param message to be passed/displayed in string format 
     * @param e name of exception, gives more context
     */
    public RiscGameException(String message, Exception e) {
        super(message, e);
    }

    /**
     * constructor of RiscGameException
     * @param message to be passed/displayed in string format 
     */
    public RiscGameException(String message) {
        super(message);
    }

    /**
     * constructor of RiscGameException
     * @param e name of exception, gives more context
     */
    public RiscGameException(Exception e) {
        super(e);
    }

}
