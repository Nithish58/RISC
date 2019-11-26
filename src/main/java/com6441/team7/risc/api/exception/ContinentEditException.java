package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in editcontinent
 */
public class ContinentEditException extends RiscGameException{

    /**
     * constructor of ContinentEditException
     * @param rootCause ContinentEditException occurred when trying to edit continent
     */
    public ContinentEditException(Exception rootCause) {
        super(rootCause);
    }

    /**
     * constructor of ContinentEditException
     * @param message to be passed/displayed in string format 
     * @param rootCause ContinentEditException occurred when trying to edit continent
     */
    public ContinentEditException(String message, Exception rootCause) {
        super(message, rootCause);
    }

    /**
     * constructor of ContinentEditException
     * @param message to be passed/displayed in string format 
     */
    public ContinentEditException(String message) {
        super(message);
    }
}
