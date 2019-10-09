package com6441.team7.risc.controller;

import static com6441.team7.risc.api.RiscContants.WHITESPACE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;

import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.RiscCommand;

public class GameController {

	private startupGameController startupPhaseController;
	private reinforceGameController reinforcementGameController;
	private fortifyGameController fortificationGameController;
	
	private MapService mapService;
	
	//private LinkedHashMap<String, Player> players;
	
	private ArrayList<Player> players;
	
	private Player currentPlayer;
	
	private int currentPlayerIndex;
	
	//private Boolean boolStartUpPhaseOver=Boolean.FALSE;
	AtomicBoolean boolStartUpPhaseOver;
	private boolean boolStartUpPhaseSet=false;
	
	private GameState gameState;
	
	private int numPlayers;
	
	public GameController(MapService mapService) {
		
		this.mapService=mapService;
		this.gameState=this.mapService.getGameState();
		
		//this.players=new LinkedHashMap<String,Player>(); 
		this.players=new ArrayList<Player>();
		
		this.currentPlayerIndex=0;
		
		boolStartUpPhaseOver=new AtomicBoolean(false);
		
		startupPhaseController=new startupGameController(this.mapService, this.players,
				this.currentPlayer);
		
		//reinforcementGameController=new reinforceGameController();
		//fortificationGameController=new fortifyGameController();

	}
	

    public void readCommand(String command) throws IOException {
    	
        if(!boolStartUpPhaseOver.get()) {        	
        	
        	if(!boolStartUpPhaseSet) {
        		this.mapService.setState(GameState.START_UP);
        		this.boolStartUpPhaseSet=true;
        	}
        	
        	startupPhaseController.readCommand(command, this.boolStartUpPhaseOver);
        }
        
        else {
        		
            	if(this.mapService.getGameState()==GameState.REINFORCE) {
            		
            		//reinforcementGameController.readCommand(command)
            		
            		//System.out.println("REINFORCE:"+this.currentPlayer.getName());
            		//reinforcementGameController=new reinforceGameController(this.currentPlayer,
            		//		this.mapService);
            		
            		this.currentPlayer=players.get(currentPlayerIndex);
            		
            		//System.out.println("REINFORCE:"+this.currentPlayer.getName());
            		
            		reinforcementGameController=new reinforceGameController(this.currentPlayer,
            													this.mapService);
            		
            	}
            	
            	else if(this.mapService.getGameState()==GameState.FORTIFY) {
            		
            		fortificationGameController=new fortifyGameController(this.currentPlayer,
            				this.mapService);
            		
            		if(currentPlayerIndex==players.size()-1) {
            			currentPlayerIndex=0;
            		}
            		
            		else currentPlayerIndex++;            		 
            		
            	}

        }
  
    }

	
    /*
	 * public void startUpPhase(String command){
	 * 
	 * //this.mapService.setState(GameState.START_UP);
	 * 
	 * System.out.println("Start Up Ends"); }
	 * 
	 * 
	 * public void reinforcementPhase(String command) {
	 * 
	 * //this.mapService.setState(GameState.REINFORCE);
	 * 
	 * 
	 * }
	 * 
	 * public void fortificationPhase(String command) {
	 * 
	 * //this.mapService.setState(GameState.FORTIFY);
	 * 
	 * 
	 * }
	 */
	
}
