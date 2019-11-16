package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if missing information when reading or writing map
 */
public class MissingInfoException extends RiscGameException {

    /**
     * constructor of MissingInfoException
     * @param rootCause Exception
     */
    public MissingInfoException(Exception rootCause) {
        super(rootCause);
    }


    /**
     * constructor of MissingInfoException
     * @param message String
     * @param rootCause Exception
     */
    public MissingInfoException(String message, Exception rootCause) {
        super(message, rootCause);
    }

    /**
     * constructor of MissingInfoException
     * @param message String
     */
    public MissingInfoException(String message) {
        super(message);
    }
}
