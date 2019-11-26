package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in parsing neighboring information
 */
public class NeighborParsingException extends RiscGameException {

    /**
     * constructor of NeighborParsingException
     * @param rootCause NeighborParsingException occurs when parsing neighbors
     *  from editing neighbors or from reading a map file.
     */
    public NeighborParsingException(Exception rootCause){
        super(rootCause);
    }

    /**
     * constructor of NeighborParsingException
     * @param message to be passed/displayed in string format 
     * @param rootCause NeighborParsingException occurs when parsing neighbors
     *  from editing neighbors or from reading a map file.
     */
    public NeighborParsingException(String message, Exception rootCause){
        super(message, rootCause);
    }

    /**
     * constructor of NeighborParsingException
     * @param message to be passed/displayed in string format 
     */
    public NeighborParsingException(String message){
        super(message);
    }
}
