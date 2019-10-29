package com6441.team7.risc.controller;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.view.DominationView;
import com6441.team7.risc.view.GameView;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.view.PhaseView;

/**
 * 
 * This class implements the different StartUpPhase Functions of the game.
 * It also focuses on ensuring that commands are run in the order they are intended too.
 * Consequently, it carries out many checks to enforce order when users enter commands.
 * For example, it does not allow countries to be populated before a player has been added.
 * Another example is that it does not allow more players to be added/removed after countries have been populated.
 * 
 * Some important functions this class implements are:
 * <ul>
 * <li>Allowing the loading of an existing game map to play.</li>
 * <li>Adding/Removing Game Players</li>
 * <li>Enforcing checks on player limits. Maximum of 9 players allowed for this game.</li>
 * <li>Determining number of initial armies allocated to each player.</li>
 * <li>Randomly assigning countries to players initially</li>
 * <li>Allowing players to place armies on their countries in a round-robin fashion.</li>
 * <li>Providing a placeall function to speedup the army placement process</li>
 * </ul>
 * 
 * @author Keshav
 *
 */

//TODO: FILL IN THE EXISTING CODE IN THIS PART
//TODO: move the logic relating to player to PlayerService.class
public class StartupGameController implements Controller{

    private Controller mapLoaderController;

     // as playerService class has a reference for mapService,
     // we don't need to have a attribute here. But in coding efficiency, I provide here.
     // the value of mapService will come from playerService.getMapService();
     // I don't have domination view because, it is the model to update domination view,
     // so i don't give reference here.
	private MapService mapService;
	private PlayerService playerService;
	private GameView phaseView;


	public StartupGameController(Controller mapController, PlayerService playerService) {
		this.mapLoaderController=mapController;

		this.playerService = playerService;
		this.mapService = playerService.getMapService();

	}

	public void setView(GameView view){
	    this.phaseView = view;
    }

    //TODO: read command from phaseView and validate command here
    //TODO: if the command is valid, call corresponding method in playerService
    //TODO: if the command is not valid, call phaseView.displayMessage() to show the error message

    @Override
    public void readCommand(String command) throws Exception {

    }


    

	
    
}
