package com6441.team7.risc.api.exception;

public class CountryEditException extends RiscGameException {
    public CountryEditException(String message, Exception e) {
        super(message, e);
    }

    public CountryEditException(String message) {
        super(message);
    }

    public CountryEditException(Exception e) {
        super(e);
    }
}
