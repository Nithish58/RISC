package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in when map is invalid
 */
public class MapInvalidException extends RiscGameException {

    /**
     * constructor of MapInvalidException
     * @param message to be passed/displayed in string format 
     * @param e MapInvalidException happened whenever map is tested for validation and failed
     */
    public MapInvalidException(String message, Exception e) {
        super(message, e);
    }

    /**
     * constructor of MapInvalidException
     * @param message to be passed/displayed in string format 
     */
    public MapInvalidException(String message) {
        super(message);
    }

    /**
     * constructor of MapInvalidException
     * @param e MapInvalidException happened whenever map is tested for validation and failed
     */
    public MapInvalidException(Exception e) {
        super(e);
    }
}
