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
    
    //Track for 1st Turn
    int count=1;

    public CommandPromptView(MapLoaderController mapLoaderController, GameController gameController) {
    	
        this.mapLoaderController = mapLoaderController;
        this.gameController = gameController;
        
        System.out.println("Welcome To Domination Game");
    }


    public void receiveCommand() {
        while (true) {
            try {
            	
            	String command="";
            	
                if(count==1) {
                	
                	System.out.println("Do you want to Edit A Map or Load A Map Directly?"
                			+ " Press 1 to Edit, Else Press any other key to Load");
                	
                	 command=scanner.nextLine();
                	
                	if(command.equalsIgnoreCase("1")) {
                		
                		gameState=GameState.LOAD_MAP;               		
                	}
                	
                	gameState=GameState.START_UP;
                	count=2;
                }
            	
                else {
                	
                	System.out.println("Enter next Command:");
                	 command = scanner.nextLine();
                }
                
                    switch (gameState) {
                        case LOAD_MAP:
                            mapLoaderController.readCommand(command);
                            break;
                        case START_UP:
                            gameController.readCommand(command);
                            break;
                        case FORTIFY:
                        	 gameController.readCommand(command);
                            //gameController.fortify(command);
                        case REINFORCE:
                        	 gameController.readCommand(command);
                            //gameController.reinforce(command);

                    }
                                     
            } catch (Exception e) {
                displayMessage(e.getMessage());
            }
        }

    }

    public void displayMessage(String string) {
        System.out.println(string);
        mapLoaderController.getMapService().getCountries();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof GameState) {
            gameState = (GameState) arg;
            System.out.println("game state changed to " + gameState.getName());
        }
    }
}
