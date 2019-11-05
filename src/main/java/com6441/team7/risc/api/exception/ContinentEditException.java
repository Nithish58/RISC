package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in editcontinent
 */
public class ContinentEditException extends RiscGameException{

    /**
     * constructor of ContinentEditException
     * @param rootCause Exception
     */
    public ContinentEditException(Exception rootCause) {
        super(rootCause);
    }

    /**
     * constructor of ContinentEditException
     * @param message String
     * @param rootCause Exception
     */
    public ContinentEditException(String message, Exception rootCause) {
        super(message, rootCause);
    }

    /**
     * constructor of ContinentEditException
     * @param message String
     */
    public ContinentEditException(String message) {
        super(message);
    }
}
