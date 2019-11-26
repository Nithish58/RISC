package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in editcountry
 */
public class CountryEditException extends RiscGameException {

    /**
     * constructor of CountryEditException
     * @param message to be passed/displayed in string format 
     * @param e CountryEditException happened when trying to edit countries in a map
     */
    public CountryEditException(String message, Exception e) {
        super(message, e);
    }

    /**
     * constructor of CountryEditException
     * @param message to be passed/displayed in string format 
     */
    public CountryEditException(String message) {
        super(message);
    }

    /**
     * constructor of CountryEditException
     * @param e CountryEditException happened when trying to edit countries in a map
     */
    public CountryEditException(Exception e) {
        super(e);
    }
}
