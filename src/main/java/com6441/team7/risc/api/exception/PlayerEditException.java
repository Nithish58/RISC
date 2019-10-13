package com6441.team7.risc.api.exception;

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
