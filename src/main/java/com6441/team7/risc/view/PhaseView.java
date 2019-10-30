package com6441.team7.risc.view;

import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.controller.*;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;

/**
 * The functionality of phaseView is just the same as CommandTerminalView
 * So I delete commandTerminalView and use phaseView replace.
 */
public class PhaseView implements GameView {
    private Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());
    private GameState gameState;
    private Controller mapLoaderController;
    private Controller startUpGameController;
    private Controller reinforceGameController;
    private Controller fortifyGameController;
    private Controller attackController;
    
    //Added By Keshav
    private PlayerService playerService;

    public void addController(List<Controller> list){
        list.forEach(controller -> {
            if(controller instanceof MapLoaderController){
                this.mapLoaderController = controller;
            }
            else if(controller instanceof StartupGameController){
                this.startUpGameController = controller;
            }
            else if(controller instanceof ReinforceGameController){
                this.reinforceGameController = controller;
            }
            else if(controller instanceof FortifyGameController){
                this.fortifyGameController = controller;
            }
            else if(controller instanceof AttackGameController){
                this.attackController = controller;
            }
        });
    }
    @Override
    public void receiveCommand() {
        while (true) {
            try {
                String command = scanner.nextLine();

                switch (gameState) {
                    case LOAD_MAP:
                        mapLoaderController.readCommand(command);
                        break;
                    case START_UP:
                        startUpGameController.readCommand(command);
                        break;
                    case REINFORCE:
                        reinforceGameController.readCommand(command);
                        break;
                    case ATTACK:
                        attackController.readCommand(command);
                        break;
                    case FORTIFY:
                        fortifyGameController.readCommand(command);
                        break;
                }

            } catch (Exception e) {
                displayMessage(e.getMessage());
            }
        }
    }

    @Override
    public void displayMessage(String string) {
        System.out.println(string);
    }

    @Override
    public void update(Observable o, Object arg) {

        if (arg instanceof GameState) {
            gameState = (GameState) arg;
            displayMessage("clear screen!");
            System.out.println("game state being changed to " + gameState.getName());
        }

        if(o instanceof PlayerService){
           // String currentPlayer = ((PlayerService) o).getCurrentPlayerName();
           // displayMessage(String.format("the current player: + %s", currentPlayer));
            
        	
        	//If integer changed, it must be currentPlayer Index...
        	//therefore we display current player + state
        	if(arg instanceof Integer) {
        		System.out.println(playerService.getCurrentPlayerName()+" is in "+gameState+" state");
            }
        	
        	//If arg is a string, it has been returned by the add player method
        	if(arg instanceof String) {
        		System.out.println("Player added successfully: "+(String) arg);
        	}
        	
        	//If arg is a player, it is the player that is removed that has been returned
        	if(arg instanceof Player) {
        		String removedPlayerName=((Player) arg).getName();
        		System.out.println("Player removed successfully: "+removedPlayerName);
        	}

        }

        
    }
    
    //TRIAL METHOD...NOT YET USED...FOUND ON NET
    public  void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    public void setPhaseViewPlayerService(PlayerService playerService) {
    	this.playerService=playerService;
    }
    
}
