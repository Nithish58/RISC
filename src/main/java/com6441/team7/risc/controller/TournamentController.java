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

public class TournamentController {

	
	private StartupGameController startupGameController;
	
	private PlayerService playerService;
	
	private MapService mapService;
	
	private ArrayList<String> mapList;
	
	private ArrayList<String> strPlayerList;
	
	private int numGames;
	
	private int numTurns;
	
	private String[][] arrResults;
	 
	
	
	public TournamentController(String command, StartupGameController sgc) {
		
		this.startupGameController=sgc;
		
		this.playerService=sgc.getPlayerService();
		
		this.mapService=this.playerService.getMapService();
		
		if (this.mapList == null) {
		this.mapList=new ArrayList<>();
		}
		
		//Hardcoded:
		mapList.add("ameroki.map");
		mapList.add("luca.map");
		mapList.add("roman_empire.map");
		mapList.add("eurasien.map");
		mapList.add("RiskEurope.map");
		
		launchTournament();
		
	}
	
	public void launchTournament() {
		
		//hardcoded for test purpose
		numTurns = 50;
		//hardcoded for test purpose 
		numGames = 5;
		
		arrResults=new String[mapList.size()][numGames];
		
		for(int i=0;i<mapList.size();i++) {
			
			MapLoaderAdapter tournamentMapAdapter = new MapLoaderAdapter(playerService.getMapService());
			
			playerService.notifyPlayerServiceObservers("Begin match on map: "+mapList.get(i));
			
			for(int j=0;j<numGames;j++) {
				
				playerService.setMoveCounter(0);
				
				playerService.addPlayer("a", "aggressive");
				
				playerService.addPlayer("b", "benevolent");
				
				playerService.addPlayer("c", "random");
				
//				playerService.addPlayer("d", "cheater");
				
				playerService.notifyPlayerServiceObservers("Round: "+j);
				
				playerService.setNumTurns(numTurns);
				
				startupGameController.setBoolMapLoaded(false);
				
				startupGameController.setBoolCountriesPopulated(false);
				
				startupGameController.loadMap("loadmap "+mapList.get(i));
				
				playerService.initialiseDeckCards();
				
				startupGameController.populateCountries();
				
				startupGameController.placeAll();
				
				//playerService.automateGame();
				
				playerService.removePlayer("a");
				playerService.removePlayer("b");
				playerService.removePlayer("c");
				//playerService.removePlayer("d");
				
			}
			
		}		
		
		playerService.notifyPlayerServiceObservers("TOURNAMENT ENDS");
		
	}
	
	
	
	
	
	
}
