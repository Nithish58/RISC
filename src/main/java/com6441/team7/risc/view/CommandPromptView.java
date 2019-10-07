package com6441.team7.risc.view;

import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.controller.GameController;
import com6441.team7.risc.controller.MapLoaderController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

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


    public void receiveCommand() {
        while (true) {
            try {
                String command = scanner.nextLine();
                switch (gameState) {
                    case LOAD_MAP:
                        mapLoaderController.readCommand(command);
                    case START_UP:
                        //gameController.startUp(command);
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

    public void displayMessage(String string) {
        System.out.println(string);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof GameState) {
            gameState = (GameState) arg;
            System.out.println("game state being changed to " + gameState.getName());
        }
    }
}
