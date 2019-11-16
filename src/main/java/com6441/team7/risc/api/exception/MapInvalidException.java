package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in when map is invalid
 */
public class MapInvalidException extends RiscGameException {

    /**
     * constructor of MapInvalidException
     * @param message String
     * @param e Exception
     */
    public MapInvalidException(String message, Exception e) {
        super(message, e);
    }

    /**
     * constructor of MapInvalidException
     * @param message String
     */
    public MapInvalidException(String message) {
        super(message);
    }

    /**
     * constructor of MapInvalidException
     * @param e Exception
     */
    public MapInvalidException(Exception e) {
        super(e);
    }
}
