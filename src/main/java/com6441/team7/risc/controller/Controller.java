package com6441.team7.risc.controller;

/**
 * an interface for the controller
 */
public interface Controller {

    /**
     * read command from the controller
     * @param command
     * @throws Exception
     */
    void readCommand(String command) throws Exception;
}
