package com6441.team7.risc.controller;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.api.model.RiscCommand;
import com6441.team7.risc.api.wrapperview.PlayerAttackWrapper;
import com6441.team7.risc.utils.CommonUtils;
import com6441.team7.risc.utils.MapDisplayUtils;
import com6441.team7.risc.view.GameView;

/**
 * The Attack phase
 * This class basically validate the commands from the players
 * and call the methods in the Player to implement attacking and defending
 *
 */
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
    //private boolean boolDefenderDiceRequired;
    private AtomicBoolean boolDefenderDiceRequired;

	/**
	 * constructor
	 * @param playerService PlayerService
	 */
	public AttackGameController(PlayerService playerService){

        this.playerService = playerService;
        this.mapService=playerService.getMapService();
        
        this.boolDefenderDiceRequired=new AtomicBoolean(false);
        
    }

	/**
	 * set the view in the attack controller
	 * @param view for GameView
	 */
	public void setView(GameView view){
        this.phaseView = view;
    }


	/**
	 * extends method from the IController
	 * check the validity of the command,
	 * if the command is valid, call corresponding method
	 * if not, display error messages to the phase view
	 * @param command Command
	 * @throws Exception on invalid
	 */
	@Override
    public void readCommand(String command) throws Exception {
    	
    	RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);
    	
        String[] commands = {};
        
        commands = command.split("\\s");
       
        
    	
    	switch(commandType) {
    	
    	case ATTACK:
    		
    		validateAttackCommand(commands);
    		
    		break;
    	
    	case DEFEND:
    		
    		validateDefendCommand(commands);
    		
    		break;
    		
    	case ATTACKMOVE:
    		validateAttackMoveCommand(commands);
    		break;
    		
    	case SHOW_PLAYER:
        	
        	MapDisplayUtils.showPlayer(mapService,playerService,phaseView);
        	break;
        	
        case SHOW_ALL_PLAYERS:

        	MapDisplayUtils.showAllPlayers(mapService,playerService,phaseView);
        	break;
        	
        case SHOW_PLAYER_ALL_COUNTRIES:

        	MapDisplayUtils.showPlayerAllCountries(mapService,playerService,phaseView);
        	break;
        
        case SHOW_PLAYER_COUNTRIES:

        	MapDisplayUtils.showPlayerCountries(mapService,playerService,phaseView);
        	break;
        	
        case SHOW_MAP:

        	MapDisplayUtils.showMapFullPopulated(mapService, phaseView);
        	break;
        	
        case EXIT:
        	CommonUtils.endGame(phaseView);
        	break;
        	
    	
    	default:
            throw new IllegalArgumentException("cannot recognize this command");
    	
    	}
    	  	
    }

	/**
	 * Validate attackmove command
	 * If command not valid, will send error message to phase view.
	 * @param commands Command
	 */
	public void validateAttackMoveCommand(String[] commands) {
		
    	if(!playerService.getCurrentPlayer().getBoolAttackMoveRequired()) {
    		phaseView.displayMessage("attackmove not required right now");
    		return;
    	}
		
    	if(commands.length!=2) {
    		phaseView.displayMessage("Invalid attackmove command.");
    		return;
    	}
		
		int numSoldierTransfer=0;
		
		try {
			numSoldierTransfer=Integer.parseInt(commands[1]);
		}
		catch(Exception e) {
    		phaseView.displayMessage("Invalid numSoldiers Entered. Try again");
    		return;
    	}
		
		playerService.getCurrentPlayer().attackMove(numSoldierTransfer);
	}

	/**
	 * validate the defend command
	 * if the command is not valid, it will display error messages to phase view
	 * if it is valid, it will call relative methods in Player class
	 * @param arrCommand
	 */
	private void validateDefendCommand(String[] arrCommand) {
    	
    	if(arrCommand.length!=2) {
    		phaseView.displayMessage("Invalid Defend command.");
    		return;
    	}
    	
    	if(playerService.getCurrentPlayer().getBoolAttackMoveRequired()) {
    		phaseView.displayMessage("attackmove command required");
    		return;
    	}
    	
    	if(!boolDefenderDiceRequired.get()) {
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
    	this.boolDefenderDiceRequired.set(false);
    	
    	//Launch attack
    	playerService.getCurrentPlayer().attack(playerService, playerAttackWrapper);    	

    }

	/**
	 * check the validity of the attack command
	 * if the command is not valid, it will display error messages to phase view
	 * if the command is valid, it will call relative methods in Player class
	 * @param arrCommand
	 */
	private void validateAttackCommand(String[] arrCommand) {
    	
        if(boolDefenderDiceRequired.get()) {
        	//validateDefendCommand(arrCommand);
        	phaseView.displayMessage("Defend command required now.");
        	return;
        }
        
        if(playerService.getCurrentPlayer().getBoolAttackMoveRequired()) {
        	phaseView.displayMessage("attackmove command required now");
        	return;
        }
    	
    	if(!(arrCommand.length==2 || arrCommand.length==4)) {
    		phaseView.displayMessage("Invalid Attack Command");
    		return;
    	}
    	
    	//Validates attack -noattack command and ends phase
    	if(arrCommand.length==2 && arrCommand[1].equalsIgnoreCase("-noattack")) {
    		//playerService.getCurrentPlayer().endAttackPhase();
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
    			return;
    		}
    		//If integer valid, notify observers and launch attack
    		playerAttackWrapper.setNumDiceAttacker(numDice);
    		
    		//Set boolDefenderDiceRequired to true
    		this.boolDefenderDiceRequired.set(true);
    		playerAttackWrapper.setBoolaDefenderDiceRequired(boolDefenderDiceRequired);
    		
    		playerService.notifyPlayerServiceObservers(playerAttackWrapper);
    		
    		phaseView.displayMessage("Please choose number of dices Defender:");

    		
    		//Will wait for defender to enter numDice, then will launch attack in validateDefend Method
    		return;
    	} // End of If
    	
    }  //End of validate attack method

	/**
	 * call mapService and set the state to fortify
	 */

	public void endAttackPhase() {

    	mapService.setState(GameState.FORTIFY);
    }
}
