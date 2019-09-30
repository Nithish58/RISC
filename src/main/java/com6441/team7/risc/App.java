package com6441.team7.risc;

import com6441.team7.risc.api.model.*;
import com6441.team7.risc.controller.StateContext;
import com6441.team7.risc.view.CommandPromptView;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        CommandPromptView view = new CommandPromptView();
        String map = view.readCommand();
        view.displayMessage(map);



    }
}
