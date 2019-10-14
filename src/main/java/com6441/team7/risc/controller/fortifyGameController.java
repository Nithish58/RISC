package com6441.team7.risc.controller;




import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;

/**
 * <h1>The fortify phase</>
 * This class implements fortification phase of the game
 * It takes two commands:
 * <code>fortify none</code>
 * <code>fortify fromcountry tocountry num</code>
 */
public class fortifyGameController {
	
	MapService mapService;
	Player currentPlayer;
	
	public fortifyGameController(Player currentPlayer, MapService mapService) {
		
		this.currentPlayer=currentPlayer;
		this.mapService=mapService;
		
		
		System.out.println("Fortification Skipped for "+this.currentPlayer.getName());
		System.out.println();
		System.out.println();
		
		this.mapService.setState(GameState.REINFORCE);
	}


	//DONT FORGET TO CHANGE STATE TO REINFORCEMENT AGAIN IN MAPSERVICE AFTER END OF LAST METHOD

}



/*

MapService mapService;
Player currentPlayer;


public fortifyGameController(Player currentPlayer, MapService mapService) {
	
	this.currentPlayer=currentPlayer;
	this.mapService=mapService;
	
	
	System.out.println("Fortification:"+this.currentPlayer.getName());
	System.out.println();
	System.out.println();
	
	this.mapService.setState(GameState.REINFORCE);
}


//DONT FORGET TO CHANGE STATE TO REINFORCEMENT AGAIN IN MAPSERVICE AFTER END OF LAST METHOD
//DONT FORGET TO CHANGE CURRENT PLAYER TO NEXT PLAYER IN ARRAYLIST and CHECK IF END OF ARRAYLIST,
//CURRENT PLAYER GOES BACK TO BEING FIRST PLAYER (Circular)
*/