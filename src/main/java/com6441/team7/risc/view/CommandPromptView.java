package com6441.team7.risc.view;

import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.controller.GameController;
import com6441.team7.risc.controller.MapLoaderController;
import java.nio.charset.StandardCharsets;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * This class receives user input and call different controller according to the game state
 */
public class CommandPromptView implements Observer {
    private Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());
    private GameState gameState;
    private MapLoaderController mapLoaderController;
    private GameController gameController;

    public CommandPromptView(MapLoaderController mapLoaderController, GameController gameController) {
        this.mapLoaderController = mapLoaderController;
        this.gameController = gameController;
        System.out.println("welcome to domination game");
    }


    /**
     * receive user input. send the user command to different controller according to game state
     */
    public void receiveCommand() {
        while (true) {
            try {
                String command = scanner.nextLine();
                switch (gameState) {
                    case LOAD_MAP:
                        mapLoaderController.readCommand(command);
                        break;
                    case START_UP:
                        gameController.startUp(command);
                        break;
                    case FORTIFY:
                        //gameController.fortify(command);
                    case REINFORCE:
                        //gameController.reinforce(command);
                }
            } catch (Exception e) {
                displayMessage(e.getMessage());
            }
        }

    }

    /**
     * display message to users
     * @param string
     */
    public void displayMessage(String string) {
        System.out.println(string);
    }

    /**
     * update the game state
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof GameState) {
            gameState = (GameState) arg;
            System.out.println("game state being changed to " + gameState.getName());
        }
    }
}
