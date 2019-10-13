package com6441.team7.risc.controller;

import java.util.ArrayList;

import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;

public class fortifyGameController {

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
	
}
