package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in parsing country
 */
public class CountryParsingException extends RiscGameException{
    public CountryParsingException(Exception rootCause) {
        super(rootCause);
    }



    public CountryParsingException(String message, Exception rootCause) {
        super(message, rootCause);
    }

    public CountryParsingException(String message) {
        super(message);
    }
}
