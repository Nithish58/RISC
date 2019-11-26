package com6441.team7.risc.controller;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

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
import com6441.team7.risc.view.GameView;
import com6441.team7.risc.view.PhaseView;

public class TournamentController {

	
	private StartupGameController startupGameController;
	
	private PlayerService playerService;
	
	private MapService mapService;
	
	private ArrayList<String> mapList;
	
	private ArrayList<String> strPlayerList;
	
	private GameView phaseView;
	
	private int numGames;
	
	private int numTurns;
	
	private String[][] arrResults;
	
	private ArrayList<String> listPlayerStrategy;
	
	private int mapIndex;
	private int gameIndex;
	 
	
	
	public TournamentController(String command, StartupGameController sgc) {
	
		initialiseTournamentVariables(sgc);
		
		//Convert Command To Lower Case for string checks
		
		command=command.toLowerCase();
		
		//launch Hardcoded Tournament
		if(command.equalsIgnoreCase("tournament")) {
			
			initialiseTournamentHardcoded(sgc);
			
			launchTournamentHardcoded();
			return;
		}
		
		if(!validateTournamentConditions(command)) {
			phaseView.displayMessage("Invalid Tournament Command!!");
			return;
		}
		
		//All tournament validations met
		//Can now launch tournament
		launchTournament();
		
	}
	

	
	public void launchTournament() {
		
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
				
				checkAndRemoveExistingPlayers();
				
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
		
		for(int i=0;i<listPlayerStrategy.size();i++) {
			
			String strName="Player "+ listPlayerStrategy.get(i)+" "+(i+1);
			
			
			playerService.addPlayer(strName, listPlayerStrategy.get(i));
		}
		
		playerService.setNumTurns(numTurns);
		
		startupGameController.setBoolMapLoaded(false);
		
		startupGameController.setBoolCountriesPopulated(false);
		
		playerService.initialiseDeckCards();		
		
	}
	
	
	private void checkAndRemoveExistingPlayers() {
		
		for(int i=0;i<listPlayerStrategy.size();i++) {
			
			String strName="Player "+ listPlayerStrategy.get(i)+" "+(i+1);
			
			
			playerService.removePlayer(strName);
		}
		
	}
	
	public void initialiseTournamentVariables(StartupGameController sgc){
		
		this.startupGameController=sgc;
		
		this.playerService=sgc.getPlayerService();
		
		this.mapService=this.playerService.getMapService();
		
		this.phaseView=startupGameController.getPhaseViewSGC();
		
		this.mapIndex=0;
		this.gameIndex=0;
		
	}

	public void setResult(String strResult) {
		arrResults[mapIndex][gameIndex]=strResult;
	}
	
	public boolean validateTournamentConditions(String command) {
		
		if(!(command.contains("-p")&&command.contains("-m")&&command.contains("-g")&&command.contains("-d"))) {
			phaseView.displayMessage("Tournament Command lacks 1 or more params");
			return false;
		}
		
		if(!validateMapCommand(command)) return false;
		
		if(!validatePlayerCommand(command)) return false;
		
		if(!validateNumGames(command)) return false;
		
		if(!validateNumTurns(command)) return false;
		
		
		//Initialise Remaining variables before launching tournament
		playerService.setBoolTournamentMode(true);		
		
		this.playerService.setTournamentController(this);
		
		
		return true;
	}
	/**
	 * Validates Map Command to check if any valid map is inserted
	 * @param command String command
	 * @return true if atleast 1 valid map is chosen
	 */
	public boolean validateMapCommand(String command) {
		
		String strMaps;
		strMaps=StringUtils.substringBetween(command,"-m ", "-p");
		
		if(strMaps==null) return false;
		
		String[] arrStrMaps=strMaps.split("\\s+");
		
		if (this.mapList == null) {
		this.mapList=new ArrayList<>();
		}	
		
		for(String s:arrStrMaps) {
			
			//Checks Map Validity, if valid, adds to list
			startupGameController.loadMap("loadmap "+s);
			if(mapService.isMapValid()) {
				mapList.add(s);
			}
			else phaseView.displayMessage(s+" is invalid.");
		}
		
		if(mapList.size()<=0) {
			phaseView.displayMessage("No Valid Maps Loaded. Cannot Proceed");
			return false;
		}
		
		return true;		
	}
	
	public boolean validatePlayerCommand(String command) {
		
		String strPlayer;
		strPlayer=StringUtils.substringBetween(command,"-p ", "-g");
		
		if(strPlayer==null) return false;
		
		boolean validPlayerFound=false;
		
		if(this.listPlayerStrategy==null) {
			this.listPlayerStrategy=new ArrayList<String>();
		}
		
		String[] arrStrStrategy=strPlayer.split("\\s+");
		
		for(int i=0;i<arrStrStrategy.length;i++) {
			String s=arrStrStrategy[i];
			
			if(s.equalsIgnoreCase("random")||s.equalsIgnoreCase("aggressive")||s.equalsIgnoreCase("human")||
				s.equalsIgnoreCase("cheater")||s.equalsIgnoreCase("benevolent")){
					
					//String strName="Player "+ arrStrStrategy[i]+" "+(i+1);
					//playerService.addPlayer(strName, arrStrStrategy[i]);
					listPlayerStrategy.add(arrStrStrategy[i]);
					validPlayerFound=true;
				}
			
		}
		
		if(!validPlayerFound) {
			phaseView.displayMessage("No Valid Strategies found");
			return false;
		}
		
		return true;
		
	}
	
	
	public boolean validateNumGames(String command) {
		
		String strNumGames;
		strNumGames=StringUtils.substringBetween(command,"-g ", "-d");
		
		if(strNumGames==null) return false;
		
		String[] arrNumGames=strNumGames.split("\\s+");
		
		if(arrNumGames.length!=1) {
			phaseView.displayMessage("Invalid Num Games");
			return false;
		}
		
		try {
			
			this.numGames=Integer.parseInt(arrNumGames[0]);
			
			if(numGames<=0) {
				phaseView.displayMessage("Num Games should be >0.");
				return false;
			}
			
		}
		
		catch(NumberFormatException e) {
			phaseView.displayMessage("Invalid Num Format");
		}
		
		return true; //Valid Num Games
		
		
	}
	
	public boolean validateNumTurns(String command) {
		
		String strNumTurns;
		strNumTurns=StringUtils.substringAfter(command, "-d ");
		
		if(strNumTurns==null) return false;
		
		String[] arrNumTurns=strNumTurns.split("\\s+");
		
		if(arrNumTurns.length!=1) {
			phaseView.displayMessage("Invalid Num Turns.");
			return false;
		}
		
		
		try {
			
			this.numTurns=Integer.parseInt(arrNumTurns[0]);
			
			if(numTurns<=0) {
				phaseView.displayMessage("NumTurns should be >0.");
			}
			
		}
		catch(NumberFormatException e) {
			phaseView.displayMessage("Invalid Num Format");
		}
		
		return true; //Valid Num Turns
		
	}
	
	
public void initialiseTournamentHardcoded(StartupGameController sgc) {
		
		
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
		
	}
	
	public void launchTournamentHardcoded() {
		
		//hardcoded for test purpose
		numTurns = 1000;
		//hardcoded for test purpose 
		numGames = 5;
		
		arrResults=new String[mapList.size()][numGames];
		
		for(mapIndex=0;mapIndex<mapList.size();mapIndex++) {
			
			for(gameIndex=0;gameIndex<numGames;gameIndex++) {				
				
				playerService.notifyPlayerServiceObservers("\n\n"+mapList.get(mapIndex)
				+" Match "+(gameIndex+1));
				
				checkAndResetGameStatesHardcoded();
				
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
	
	private void checkAndResetGameStatesHardcoded() {
		
		playerService.notifyPlayerServiceObservers("Resetting Game States");
		
		//Reset numTurns to 0
		playerService.setMoveCounter(0);
		
		//Reset Winner and boolWinner
		playerService.setBoolPlayerWinner(false);
		playerService.setPlayerWinner(null);
		
		//ResetPlayers
		//checkAndRemoveExistingPlayers();
		
		playerService.addPlayer("a", "benevolent");								
		playerService.addPlayer("b", "aggressive");				
		playerService.addPlayer("c", "cheater");				
		playerService.addPlayer("d", "benevolent");				
		playerService.addPlayer("e", "aggressive");		
		
		playerService.setNumTurns(numTurns);
		
		startupGameController.setBoolMapLoaded(false);
		
		startupGameController.setBoolCountriesPopulated(false);
		
		playerService.initialiseDeckCards();		
		
	}
	
}
