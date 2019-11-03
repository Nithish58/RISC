package com6441.team7.risc.controller;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.api.model.RiscCommand;
import com6441.team7.risc.api.wrapperview.PlayerAttackWrapper;
import com6441.team7.risc.view.GameView;

public class AttackGameController implements Controller {

    private PlayerService playerService;
    private MapService mapService;
    private GameView phaseView;

    /**
     * Used to send multiple parameters to player class for attack
     */
    private PlayerAttackWrapper playerAttackWrapper;
    
    /**
     * Used to control gameflow. WHen it is set to true, only defend <numdice> command will be valid
     */
    private boolean boolDefenderDiceRequired;
    
    
    public AttackGameController(PlayerService playerService){

        this.playerService = playerService;
        this.mapService=playerService.getMapService();
    }

    public void setView(GameView view){
        this.phaseView = view;
    }


    //TODO: receive command from phaseView and validate the command
    //TODO: if the command is valid, call corresponding method in PlayerService.class
    //TODO: if the command is not valid, call phaseView.displayMessage() to display error message
    @Override
    public void readCommand(String command) throws Exception {

    	//this.playerService.getMapService().setState(GameState.FORTIFY);
    	
    	RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);
    	
        String[] commands = {};
    	
		/*
		 * if(command.toLowerCase(Locale.CANADA).contains("-allout")){
		 * 
		 * command = StringUtils.substringAfter(command, "-"); commands =
		 * command.split("\\s-"); }
		 */
        
        commands = command.split("\\s");
        
        if(boolDefenderDiceRequired) {
        	validateDefendCommand(commands);
        }
        
    	
    	switch(commandType) {
    	
    	case ATTACK:
    		
    		validateAttackCommand(commands);
    		
    		break;
    	
    	case DEFEND:
    		
    		validateDefendCommand(commands);
    		
    		break;
    		
    	case ATTACKMOVE:
    		break;
    	
    	default:
            throw new IllegalArgumentException("cannot recognize this command");
    	
    	}
    	  	
    }
    
    private void validateDefendCommand(String[] arrCommand) {
    	
    	if(arrCommand.length!=2) {
    		phaseView.displayMessage("Invalid Defend command.");
    		return;
    	}
    	
    	if(!boolDefenderDiceRequired) {
    		phaseView.displayMessage("Defender numDice is not required right now");
    		return;
    	}
    	
    	int defenderNumDice=0;
    	
    	try {
    		defenderNumDice=Integer.parseInt(arrCommand[1]);
    	}
    	catch(Exception e) {
    		phaseView.displayMessage("Invalid numDice Entered. Try again");
    		return;
    	}
    	
    	//Assuming defender dice is required and number is valid after all validations:
    	
    	phaseView.displayMessage("Defender has chosen "+defenderNumDice+" dice.");
    	
    	playerAttackWrapper.setNumDiceDefender(defenderNumDice);
    	
    	//Reset to false so that other commands can go through
    	this.boolDefenderDiceRequired=false;
    	
    	//Launch attack
    	playerService.getCurrentPlayer().attack(playerService, playerAttackWrapper);    	

    }
    	
    private void validateAttackCommand(String[] arrCommand) {
    	
    	if(!(arrCommand.length==2 || arrCommand.length==4)) {
    		phaseView.displayMessage("Invalid Attack Command");
    		return;
    	}
    	
    	//Validates attack -noattack command and ends phase
    	if(arrCommand.length==2 && arrCommand[1].equalsIgnoreCase("-noattack")) {
    		endAttackPhase();
    		return;
    	}
    	
    	//Validates -allout or normal attack command
    	if(arrCommand.length==4) {
    		
    		String fromCountryName=arrCommand[1];
    		String toCountryName=arrCommand[2];
    		
    		//Check if fromCountry and toCountry are present in map
    		if(!(mapService.getCountryByName(fromCountryName).isPresent() ||
    				mapService.getContinentByName(toCountryName).isPresent())) {
    			phaseView.displayMessage("Invalid fromCountry or toCountry");
    			return;
    		}
    		
    		//Extracting info from commands
    		Country fromCountry=mapService.getCountryByName(fromCountryName).get();
    		Country toCountry=mapService.getCountryByName(toCountryName).get();
    		
    		playerAttackWrapper=new PlayerAttackWrapper(fromCountry,toCountry);
    		
    		//Check if 4th parameter is the -allout command
    		
    		if(arrCommand[3].equalsIgnoreCase("-allout")) {
    			
    			playerAttackWrapper.setBooleanAllOut();
    			
    			//Notify Observers and Launch Attack
    			playerService.notifyPlayerServiceObservers(playerAttackWrapper);
    			
    			//Launch attack
    			playerService.getCurrentPlayer().attack(playerService,playerAttackWrapper);
    			    			
    			return;
    		}
    		
    		
    		//Try checking if 4th param is an integer...if not invalidate command 
    		
    		int numDice=0;
    		
    		try {
    			 numDice=Integer.parseInt(arrCommand[3]);
    		}
    		catch(Exception e) {
    			phaseView.displayMessage("Invalid numDice entered.");
    			System.out.println("Before return");
    			return;
    		}
    		System.out.println("After return");
    		//If integer valid, notify observers and launch attack
    		playerAttackWrapper.setNumDiceAttacker(numDice);
    		
    		playerService.notifyPlayerServiceObservers(playerAttackWrapper);
    		
    		phaseView.displayMessage("Please choose number of dices Defender:");
    		//Set boolDefenderDiceRequired to true
    		this.boolDefenderDiceRequired=true;
    		
    		//Will wait for defender to enter numDice, then will launch attack in validateDefend Method
    		return;
    	} // End of If
    	
    }  //End of validate attack method
    
    private void endAttackPhase() {
    	mapService.setState(GameState.FORTIFY);
    }
}
