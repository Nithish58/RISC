package com6441.team7.risc.utils;

import com6441.team7.risc.view.GameView;

public class CommonUtils {
	
	/**
	 * end the game
	 * Called when only 1 player is present as he/she automatically wins.
	 * Also called when exit command entered.
	 */
	public static void endGame(GameView view) {

	    	view.displayMessage("Game Ends");
	    	System.exit(0);;
	    
	}
	
}
