package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in parsing country
 */
public class CountryParsingException extends RiscGameException{

    /**
     * constructor of CountryParsingException
     * @param rootCause Exception
     */
    public CountryParsingException(Exception rootCause) {
        super(rootCause);
    }

    /**
     * constructor of CountryParsingException
     * @param message String
     * @param rootCause Exception
     */
    public CountryParsingException(String message, Exception rootCause) {
        super(message, rootCause);
    }

    /**
     * constructor of CountryParsingException
     * @param message String
     */
    public CountryParsingException(String message) {
        super(message);
    }
}
