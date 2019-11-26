package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in parsing continent
 */
public class ContinentParsingException extends RiscGameException{

    /**
     * constructor of ContinentParsingException
     * @param rootCause ContinentParsingException occurred when trying to parse continents from map file
     */
    public ContinentParsingException(Exception rootCause) {
        super(rootCause);
    }

    /**
     * constructor of ContinentParsingException
     * @param message to be passed/displayed in string format 
     * @param rootCause ContinentParsingException occurred when trying to parse continents from map file
     */
    public ContinentParsingException(String message, Exception rootCause) {
        super(message, rootCause);
    }

    /**
     * constructor of ContinentParsingException
     * @param message to be passed/displayed in string format 
     */
    public ContinentParsingException(String message) {
        super(message);
    }
}
