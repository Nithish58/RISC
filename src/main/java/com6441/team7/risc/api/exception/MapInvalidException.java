package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in when map is invalid
 */
public class MapInvalidException extends RiscGameException {
    public MapInvalidException(String message, Exception e) {
        super(message, e);
    }

    public MapInvalidException(String message) {
        super(message);
    }

    public MapInvalidException(Exception e) {
        super(e);
    }
}
