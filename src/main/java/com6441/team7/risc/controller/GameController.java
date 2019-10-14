package com6441.team7.risc.controller;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.RiscCommand;

public class GameController {

	private startupGameController startupPhaseController;
	private reinforceGameController reinforcementGameController;
	private fortifyGameController fortificationGameController;
	
	private MapLoaderController mapLoaderController;
	
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
	
	public GameController(MapLoaderController mapController,MapService mapService) {
		
		this.mapLoaderController=mapController;
		
		this.mapService=mapService;
		this.gameState=this.mapService.getGameState();
		
		//this.players=new LinkedHashMap<String,Player>(); 
		this.players=new ArrayList<Player>();
		
		this.currentPlayerIndex=0;
		
		boolStartUpPhaseOver=new AtomicBoolean(false);
		
		startupPhaseController=new startupGameController(this.mapLoaderController,this.mapService,
																	this.players);
		
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
            		
            		this.currentPlayer=players.get(currentPlayerIndex);
            		
            		System.out.println("REINFORCE:"+this.currentPlayer.getName());
            		
            		reinforcementGameController=new reinforceGameController(this.currentPlayer,
            													this.mapService,
            													startupPhaseController,
            													command);
            		
            	}
            	
            	else if(this.mapService.getGameState()==GameState.FORTIFY) {
            		
            		fortificationGameController=new fortifyGameController(this.currentPlayer,
            				this.mapService);
            		
            		switchNextPlayer();

            	}

        }
  
    }
    
    private void switchNextPlayer() {
		
		if(currentPlayerIndex==players.size()-1) {
			currentPlayerIndex=0;
		}
		
		else currentPlayerIndex++;    
    }
    	
}
