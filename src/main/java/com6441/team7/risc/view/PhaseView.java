package com6441.team7.risc.view;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import static com6441.team7.risc.api.RiscConstants.PHASE_VIEW_STRING;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.api.wrapperview.PlayerChangeWrapper;
import com6441.team7.risc.api.wrapperview.PlayerEditWrapper;
import com6441.team7.risc.api.wrapperview.PlayerFortificationWrapper;
import com6441.team7.risc.api.wrapperview.PlayerInitialArmyWrapper;
import com6441.team7.risc.api.wrapperview.PlayerInitialCountryAssignmentWrapper;
import com6441.team7.risc.api.wrapperview.PlayerPlaceArmyWrapper;
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
    
    private Controller mapLoaderController;
    private Controller startUpGameController;
    private Controller reinforceGameController;
    private Controller fortifyGameController;
    private Controller attackController;
    
    private GameState gameState;
    private Player currentPlayer=null;
    

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
        System.out.println(PHASE_VIEW_STRING + string);
    }

    @Override
    public void update(Observable o, Object arg) {

        if(o instanceof PlayerService){
            
        	//When player is changed
        	if(arg instanceof PlayerChangeWrapper) {       		
        		playerSwitchStatus(arg);
        		return;
            }
        	
        	//When player is added or removed
        	if(arg instanceof PlayerEditWrapper) {
        		playerEditStatus(arg);    
        		return;
        	}
        	
        	//When initial army is assigned to a player
        	if(arg instanceof PlayerInitialArmyWrapper) {
        		playerInitialArmyStatus(arg);
        		return;
        	}
        	
        	//When countries are randomly assigned to players
        	if(arg instanceof PlayerInitialCountryAssignmentWrapper) {
        		playerInitialCountryAssignmentStatus(arg);
        		return;
        	}
        	
        	//When placeArmy of placeAll method called
        	if(arg instanceof PlayerPlaceArmyWrapper) {
        		playerPlaceArmyStatus(arg);
        		return;
        	}
        	
        	//During actual fortification in fortification phase
        	if(arg instanceof PlayerFortificationWrapper) {
        		playerFortificationStatus(arg);
        		return;
        	}
        	        	
        	return;
        }  //End of PlayerService.class observable 
        
        
        //When GameState is changed
        if (arg instanceof GameState) {            
            displayMessage("clear screen!");           
            gamestateAndPlayerStatus(arg);
            return;
        }

        
    }  //End of Update Method
    

	/**
     * Extracts information about a newly added player or a removed player and displays it
     * @param arg as PlayerEditWrapper.class object
     */
    private void playerEditStatus(Object arg) {
		PlayerEditWrapper playerEditWrapper=((PlayerEditWrapper) arg);
		
		Player addedPlayer=playerEditWrapper.getAddedPlayer();
		Player removedPlayer=playerEditWrapper.getRemovedPlayer();
		
		if(addedPlayer!=null) displayMessage("Player Added: "+addedPlayer.getName());
		
		else displayMessage("Player Removed: "+removedPlayer.getName());
		
	}
    
    /**
     * Displays information when a player is changed/switched
     * @param arg as PlayerChangeWrapper.class object
     */
    private void playerSwitchStatus(Object arg) {
    	
    	PlayerChangeWrapper playerChangeWrapper=((PlayerChangeWrapper) arg);
    	
    	this.currentPlayer=playerChangeWrapper.getCurrentPlayer();
    	
    	displayMessage("It's now the turn of "+currentPlayer.getName());
    	
    }
    
    /**
     * Displays information about GameState and current player.
     * @param arg as GameState
     */
    private void gamestateAndPlayerStatus(Object arg) {
    	
        this.gameState = (GameState) arg;
    	
        if(this.currentPlayer==null)
        	displayMessage("Game State Changed To " + gameState.getName());
        
        else {
        	displayMessage(this.currentPlayer.getName()+" is in "+this.gameState+" state");       
        }
    }
    
    /**
     * Displays information about the number of initial armies assigned to a player before populating a country
     * @param arg as PlayerInitialArmyWrapper.class object
     */
    private void playerInitialArmyStatus(Object arg) {
    	
    	PlayerInitialArmyWrapper playerInitialArmyWrapper=((PlayerInitialArmyWrapper)arg);
    	Player player=playerInitialArmyWrapper.getPlayer();
    	
    	displayMessage(player.getName()+" has been assigned "+player.getArmies()+"\n");
    	
    }
    
    /**
     * Displays information about random country assignment to players when countries are being populated.
     * @param arg PlayerInitialCountryAssignment.class object
     */
    private void playerInitialCountryAssignmentStatus(Object arg) {
    	PlayerInitialCountryAssignmentWrapper playerInitialCountryAssignment
    	= ((PlayerInitialCountryAssignmentWrapper) arg);
    	Player player=playerInitialCountryAssignment.getPlayer();
    	Country country=playerInitialCountryAssignment.getCountry();
    	
    	displayMessage(player.getName()+" has been assigned country "+country.getCountryName());
    	displayMessage("Country "+country.getCountryName()+" now has "+country.getSoldiers()+" soldier.");
    	displayMessage(player.getName()+" now has "+player.getArmies()+" armies.\n");
    			
    }
    
    /**
     * Displays information about num armies of players and num soldiers of countries after placearmy method called successfully.
     * @param arg PlayerPlaceArmyWrapper.class object
     */
    private void playerPlaceArmyStatus(Object arg) {
    	PlayerPlaceArmyWrapper playerPlaceArmyWrapper=((PlayerPlaceArmyWrapper)arg);
    	Player p=playerPlaceArmyWrapper.getPlayer();
    	Country c=playerPlaceArmyWrapper.getCountry();
    	
    	displayMessage("Army Placed. "+p.getName()+" has "+p.getArmies()+" armies left. "
    	+c.getCountryName()+" now has "+c.getSoldiers()+" soldiers.");
		
	}
    
    /**
     * Displays information about fromCountry and toCountry during fortification phase.
     * @param arg PlayerFortificationWrapper.class object
     */
    private void playerFortificationStatus(Object arg) {
    	PlayerFortificationWrapper playerFortificationWrapper
    	=((PlayerFortificationWrapper) arg);
    	Country fromCountry=playerFortificationWrapper.getCountryFrom();
    	Country toCountry=playerFortificationWrapper.getCountryTo();
    	int numSoldiers=playerFortificationWrapper.getNumSoldiers();
    	
    	displayMessage("Before Fortification, From: "+fromCountry.getCountryName()+
    			" had "+(fromCountry.getSoldiers()+numSoldiers)+" soldiers, To: "
    			+toCountry.getCountryName()+" had "+(toCountry.getSoldiers()-numSoldiers)
    			+" soldiers.");
    	
    	displayMessage("After Fortification, From: "+fromCountry.getCountryName()+
    			" now has "+fromCountry.getSoldiers()+" soldiers, To: "
    			+toCountry.getCountryName()+" now has "+toCountry.getSoldiers()+" soldiers.\n");
    	
    }
    
    //TRIAL METHOD...NOT YET USED...FOUND ON NET
    public  void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
}
