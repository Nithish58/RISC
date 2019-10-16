package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in editcontinent
 */
public class ContinentEditException extends RiscGameException{
    public ContinentEditException(Exception rootCause) {
        super(rootCause);
    }

    public ContinentEditException(String message, Exception rootCause) {
        super(message, rootCause);
    }

    public ContinentEditException(String message) {
        super(message);
    }
}
