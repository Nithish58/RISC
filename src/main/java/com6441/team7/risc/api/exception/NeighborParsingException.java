package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in parsing neighboring information
 */
public class NeighborParsingException extends RiscGameException {

    public NeighborParsingException(Exception rootCause){
        super(rootCause);
    }

    public NeighborParsingException(String message, Exception rootCause){
        super(message, rootCause);
    }

    public NeighborParsingException(String message){
        super(message);
    }
}
