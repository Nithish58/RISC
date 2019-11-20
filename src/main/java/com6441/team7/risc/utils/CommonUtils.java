package com6441.team7.risc.utils;

import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.view.GameView;

/**
 * a util to end the game
 */
public class CommonUtils {
	
	/**
	 * end the game
	 * called when exit command explicitly entered or when only 1 player is present in the game
	 * @param view passed by gamecontroller.
	 * Cannot use this when player wins because decision is made in player.class
	 * player.class does not have any reference to views else what is the point of using MVC.
	 * @param view reference...passed by a controller
	 */
	public static void endGame(GameView view) {

	    	view.displayMessage("Game Ends");
	    	System.exit(0);;
	    
	}
	
	/**
	 * end the game
	 * Called when only 1 player is present as he/she automatically wins.
	 * Also called when exit command entered or when a player wins.
	 * @param playerService reference
	 */
	public static void endGame(PlayerService playerService) {

			playerService.evaluateWorldDomination();
	    	playerService.notifyPlayerServiceObservers("Game Ends");
	    	System.exit(0);
	    
	}
	
}
