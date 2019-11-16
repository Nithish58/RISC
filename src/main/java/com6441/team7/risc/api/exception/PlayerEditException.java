package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in add or remove player
 */
public class PlayerEditException extends RiscGameException{

    /**
     * constructor of PlayerEditException
     * @param rootCause Exception
     */
    public PlayerEditException(Exception rootCause) {
        super(rootCause);
    }

    /**
     * constructor of PlayerEditException
     * @param message String
     * @param rootCause Exception
     */
    public PlayerEditException(String message, Exception rootCause) {
        super(message, rootCause);
    }

    /**
     * constructor of PlayerEditException
     * @param message String
     */
    public PlayerEditException(String message) {
        super(message);
    }

}
