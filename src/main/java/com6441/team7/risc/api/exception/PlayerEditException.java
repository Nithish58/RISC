package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in add or remove player
 */
public class PlayerEditException extends RiscGameException{
	
    public PlayerEditException(Exception rootCause) {
        super(rootCause);
    }

    public PlayerEditException(String message, Exception rootCause) {
        super(message, rootCause);
    }

    public PlayerEditException(String message) {
        super(message);
    }

}
