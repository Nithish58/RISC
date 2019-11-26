package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if missing information when reading or writing map
 */
public class MissingInfoException extends RiscGameException {

    /**
     * constructor of MissingInfoException
     * @param rootCause MissingInfoException occurs when there is 
     * insufficient information is available to carry out the operation
     */
    public MissingInfoException(Exception rootCause) {
        super(rootCause);
    }


    /**
     * constructor of MissingInfoException
     * @param message to be passed/displayed in string format 
     * @param rootCause MissingInfoException occurs when there is 
     * insufficient information is available to carry out the operation
     */
    public MissingInfoException(String message, Exception rootCause) {
        super(message, rootCause);
    }

    /**
     * constructor of MissingInfoException
     * @param message to be passed/displayed in string format 
     */
    public MissingInfoException(String message) {
        super(message);
    }
}
