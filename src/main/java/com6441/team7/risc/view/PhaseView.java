package com6441.team7.risc.view;

import com6441.team7.risc.api.model.*;

import static com6441.team7.risc.api.RiscConstants.PHASE_VIEW_STRING;
import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

import com6441.team7.risc.api.wrapperview.*;

/*
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.api.wrapperview.PlayerAttackWrapper;
*/

import com6441.team7.risc.controller.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;

/**
 * The phase view implements GameView, and display all the information during game play
 */
public class PhaseView implements GameView {

    /**
     * scanner to receive player input
     */
    private Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());

    /**
     * a reference of mapLoaderController
     */
    private Controller mapLoaderController;

    /**
     * a reference of startUpGameController
     */
    private Controller startUpGameController;

    /**
     * a reference of reinforceGameController
     */
    private Controller reinforceGameController;

    /**
     * a reference of fortifyGameController
     */
    private Controller fortifyGameController;

    /**
     * a reference of attackController
     */
    private Controller attackController;

    /**
     * a reference of gameState
     */
    private GameState gameState;

    /**
     * a reference of current player
     */
    private Player currentPlayer=null;


    /**
     * add controller to controller lists
     * @param list
     */
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

    /**
     * extends method from GameView to receiveCommand
     */
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


    /**
     * extends method from GameView to displayMessage
     * @param string
     */
    @Override
    public void displayMessage(String string) {
        System.out.println(PHASE_VIEW_STRING + string);
    }

    /**
     * whenever users input valid or non-valid, or changes in the model,
     * the update() will call and display changes to the phase view
     * @param o
     * @param arg
     */
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
          
        	//During attack phase
        	if(arg instanceof PlayerAttackWrapper) {
        		playerAttackStatus(arg);
        		return;
        	}
        	        	
        	
        	//Any string message sent is just displayed
        	if(arg instanceof String) {
        		String displayMessage=((String) arg);
        		displayMessage(displayMessage);
        		return;
        	}

        	if(arg instanceof ReinforcedArmyWrapper){
        	    displayReinforcedArmy(arg);
                return; 
            }

            if(arg instanceof ReinforcedCardWrapper){
                displayReinforcedCard(arg);
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
     * display the message of reinforced countries with number of reinforced armies and player name
     * @param arg
     */
    private void displayReinforcedArmy(Object arg){
        displayMessage("player: " + ((ReinforcedArmyWrapper)arg).getPlayer().getName());
        displayMessage("reinforced country: " + ((ReinforcedArmyWrapper)arg).getCountryName());
        displayMessage("reinforced army number: " + ((ReinforcedArmyWrapper)arg).getNum());
    }


    private void displayReinforcedCard(Object arg){
        List<Card> list =  ((ReinforcedCardWrapper)arg).getCards();
        Player player = ((ReinforcedCardWrapper)arg).getPlayer();

        if (list.isEmpty()){
            displayMessage(player.getName() + "'s cards are empty");
            return;
        }

        int count = 1;
        displayMessage("player " + player.getName() + ": ");
        for(Card card: list){
            displayMessage(count + ":" + card.getName() + WHITESPACE);
            count ++;
        }
    }

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
    	+c.getCountryName()+" now has "+c.getSoldiers()+" soldiers.\n");
		
	}
    
    /**
     * Displays information about fromCountry and toCountry during fortification phase.
     * @param arg PlayerFortificationWrapper.class object
     */
    /*
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
    */

    /**
     *
     * @param arg
     */
    private void playerFortificationStatus(Object arg) {

    	PlayerFortificationWrapper playerFortificationWrapper
    	=((PlayerFortificationWrapper) arg);

    	//Check if boolean Fortification Set to true - output fortification over message
    	//Or check if fortification not successful, retrieve error message
    	
    	if(playerFortificationWrapper.getBooleanFortificationNone()
    	|| (!playerFortificationWrapper.getFortificationDisplayMessage().equalsIgnoreCase("success"))) {
    		
    		displayMessage(playerFortificationWrapper.getFortificationDisplayMessage());

    		return;
    	}
    	
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
    
    /**
     * Displays various information when an attack is launched
     * ALso displays end result of an attack
     * @param PlayerAttackWrapper as object
     */    
    private void playerAttackStatus(Object arg) {
    	
    	PlayerAttackWrapper playerAttackWrapper
    	=((PlayerAttackWrapper) arg);
    	
    	Country fromCountry=playerAttackWrapper.getFromCountry();
    	Country toCountry=playerAttackWrapper.getToCountry();
    	
    	Player fromPlayer=fromCountry.getPlayer();
    	Player toPlayer=toCountry.getPlayer();
    	
    	String fromCountryName=fromCountry.getCountryName();
    	String toCountryName=toCountry.getCountryName();
    	
    	String fromPlayerName=fromPlayer.getName();
    	String toPlayerName=toPlayer.getName();
    	
    	if(playerAttackWrapper.getBoolAttackOver()) {
    		//Do something
    		return;
    	}
    	
    	String strMsg="";
    	
    	if(playerAttackWrapper.getBoolAllOut()) {
    		strMsg=fromPlayerName+" decides to attack from "+fromCountryName+
    				" to "+toCountryName+" ("+toPlayerName+") ALLOUT!!!";
    	}
    	
    	else {
    		
    		int numDiceAttacker=playerAttackWrapper.getNumDiceAttacker();
    		
    		strMsg=fromPlayerName+" decides to attack from "+fromCountryName+
    				" to "+toCountryName+" ("+toPlayerName+")";
    		
    		strMsg+="\n"+fromPlayerName+" chooses "+numDiceAttacker+" dices.";
    		
    	}
    	
    	displayMessage(strMsg);
    }
    
    
    //TRIAL METHOD...NOT YET USED...FOUND ON NET
    public  void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
}
