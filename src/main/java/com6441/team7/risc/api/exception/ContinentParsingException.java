package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in parsing continent
 */
public class ContinentParsingException extends RiscGameException{

    /**
     * constructor of ContinentParsingException
     * @param rootCause Exception
     */
    public ContinentParsingException(Exception rootCause) {
        super(rootCause);
    }

    /**
     * constructor of ContinentParsingException
     * @param message String
     * @param rootCause Exception
     */
    public ContinentParsingException(String message, Exception rootCause) {
        super(message, rootCause);
    }

    /**
     * constructor of ContinentParsingException
     * @param message String
     */
    public ContinentParsingException(String message) {
        super(message);
    }
}
