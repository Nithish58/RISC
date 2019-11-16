package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in editcountry
 */
public class CountryEditException extends RiscGameException {

    /**
     * constructor of CountryEditException
     * @param message String
     * @param e Exception
     */
    public CountryEditException(String message, Exception e) {
        super(message, e);
    }

    /**
     * constructor of CountryEditException
     * @param message String
     */
    public CountryEditException(String message) {
        super(message);
    }

    /**
     * constructor of CountryEditException
     * @param e Exception
     */
    public CountryEditException(Exception e) {
        super(e);
    }
}
