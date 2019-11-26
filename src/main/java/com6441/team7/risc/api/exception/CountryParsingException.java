package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in parsing country
 */
public class CountryParsingException extends RiscGameException{

    /**
     * constructor of CountryParsingException
     * @param rootCause CountryParsingException occurred when trying to parse countries from map file
     */
    public CountryParsingException(Exception rootCause) {
        super(rootCause);
    }

    /**
     * constructor of CountryParsingException
     * @param message to be passed/displayed in string format 
     * @param rootCause CountryParsingException occurred when trying to parse countries from map file
     */
    public CountryParsingException(String message, Exception rootCause) {
        super(message, rootCause);
    }

    /**
     * constructor of CountryParsingException
     * @param message to be passed/displayed in string format 
     */
    public CountryParsingException(String message) {
        super(message);
    }
}
