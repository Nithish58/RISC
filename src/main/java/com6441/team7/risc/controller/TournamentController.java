package com6441.team7.risc.controller;

import java.util.ArrayList;

import com6441.team7.risc.api.model.AggressiveStrategy;
import com6441.team7.risc.api.model.BenevolentStrategy;
import com6441.team7.risc.api.model.CheaterStrategy;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.PlayerCategory;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.api.model.RandomStrategy;
import com6441.team7.risc.api.wrapperview.TournamentWrapper;
import com6441.team7.risc.utils.CommonUtils;

public class TournamentController {

	
	private StartupGameController startupGameController;
	
	private PlayerService playerService;
	
	private MapService mapService;
	
	private ArrayList<String> mapList;
	
	private ArrayList<String> strPlayerList;
	
	private int numGames;
	
	private int numTurns;
	
	private String[][] arrResults;
	
	private int mapIndex;
	private int gameIndex;
	 
	
	
	public TournamentController(String command, StartupGameController sgc) {
		
		this.startupGameController=sgc;
		
		this.playerService=sgc.getPlayerService();
		
		this.mapService=this.playerService.getMapService();
		
		this.mapIndex=0;
		this.gameIndex=0;
		
		if (this.mapList == null) {
		this.mapList=new ArrayList<>();
		}		
		
		//Hardcoded:
		mapList.add("ameroki.map");
		mapList.add("Aden.map");
		mapList.add("roman_empire.map");
		mapList.add("Africa.map");
		mapList.add("luca.map");
		
		playerService.setBoolTournamentMode(true);		
		
		this.playerService.setTournamentController(this);
		
		launchTournament();
		
	}
	
	public void launchTournament() {
		
		//hardcoded for test purpose
		numTurns = 3;
		//hardcoded for test purpose 
		numGames = 5;
		
		arrResults=new String[mapList.size()][numGames];
		
		for(mapIndex=0;mapIndex<mapList.size();mapIndex++) {
			
			for(gameIndex=0;gameIndex<numGames;gameIndex++) {				
				
				playerService.notifyPlayerServiceObservers("\n\n"+mapList.get(mapIndex)
				+" Match "+(gameIndex+1));
				
				checkAndResetGameStates();
				
				//Reset and Load Map Again
				startupGameController.loadMap("loadmap "+mapList.get(mapIndex));
				
				startupGameController.populateCountries();
				
				startupGameController.placeAll(); //Starts automation	
				
				playerService.removePlayer("a");								
				playerService.removePlayer("b");				
				playerService.removePlayer("c");				
				playerService.removePlayer("d");				
				playerService.removePlayer("e");
				
			} //End of Game Iteration
			
		}	//End of Map Iteration	
		
		
		playerService.notifyPlayerServiceObservers(new TournamentWrapper(arrResults,mapList));
		
		//End Game
		System.exit(0);
		
	}
	
	private void checkAndResetGameStates() {
		
		playerService.notifyPlayerServiceObservers("Resetting Game States");
		
		//Reset numTurns to 0
		playerService.setMoveCounter(0);
		
		//Reset Winner and boolWinner
		playerService.setBoolPlayerWinner(false);
		playerService.setPlayerWinner(null);
		
		//ResetPlayers
		//checkAndRemoveExistingPlayers();
		
		playerService.addPlayer("a", "benevolent");								
		playerService.addPlayer("b", "cheater");				
		playerService.addPlayer("c", "benevolent");				
		playerService.addPlayer("d", "benevolent");				
		playerService.addPlayer("e", "aggressive");		
		
		playerService.setNumTurns(numTurns);
		
		startupGameController.setBoolMapLoaded(false);
		
		startupGameController.setBoolCountriesPopulated(false);
		
		playerService.initialiseDeckCards();		
		
	}
	
	
	private void checkAndRemoveExistingPlayers() {
		
		
		
		/*
		 * if(!playerService.getPlayerList().isEmpty()) {
		 * playerService.notifyPlayerServiceObservers("Resetting Players");
		 * 
		 * for(int i=0;i<playerService.getPlayerList().size();i++) {
		 * playerService.removePlayer(playerService.getPlayerList().get(i).getCountryName()); }
		 * }
		 */
		
	}

	public void setResult(String strResult) {
		arrResults[mapIndex][gameIndex]=strResult;
	}
	
	
	
}
