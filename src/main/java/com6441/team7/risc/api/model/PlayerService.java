package com6441.team7.risc.api.model;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class PlayerService extends Observable {

    private MapService mapService;

    //to store newly added player to the list
    //private CircularFifoQueue<Player> playerList;
    
    //Keshav Refactoring part
    
    //Jenny I am continuing using arraylist because many functions use arraylist and it'il be tedious to refactor
    //It's gonna be hard to change everything.
    //Also we need to keep track of current player even for queue.
    //Very tedious to remove player from front and add to back of queue everytime we switch player
    //So let's stick with arraylist.
    
	/**
	 * List of players playing the game
	 */
	private ArrayList<Player> listPlayers;
	
	/**
	 * Keeps track of current player index to access current player from list of players.
	 * Used to switch to next player in list as well by incrementing index
	 */
	int currentPlayerIndex;
    
    
    
    

    public PlayerService(MapService mapService){

        this.mapService = mapService;
        //playerList = new CircularFifoQueue<>();
        
        this.listPlayers=new ArrayList<Player>();
        
        //Because no players added when PlayerService object is being instantiated in App.class
        this.currentPlayerIndex=-1;
        
    }

    public MapService getMapService() {
        return mapService;
    }

    @Override
    public void addObserver(Observer observer) {

        super.addObserver(observer);
    }
    
	public void setCurrentPlayerIndex(int num) {
		this.currentPlayerIndex=num;
		
		setChanged();
		notifyObservers(new Integer(currentPlayerIndex));
	}

    public Player addPlayer(String name){
        //TODO: add a player in the playerList
    	Player newPlayer=new Player(name);
    	listPlayers.add(newPlayer);
    	
    	setChanged();
        notifyObservers(name);
    	return newPlayer;
    }

    public boolean removePlayer(String playerName){
        //TODO: remove a player in the playerList
    	
		for(int i=0;i<listPlayers.size();i++) {
			if(listPlayers.get(i).getName().equals(playerName)) {
				Player removedPlayer=listPlayers.remove(i);
				
				setChanged();
		    	//NOTIFY BEFORE RETURN
		        notifyObservers(removedPlayer);
		        
		        removedPlayer.deleteObservers();
				
				return true;
			}
		}
		
		return false;
    }

    public ArrayList<Player> getPlayerList(){
        return listPlayers;
    }

    public String getCurrentPlayerName(){
    	
    	if(currentPlayerIndex<0) return ""; 
    	
    	Player currentPlayer=listPlayers.get(currentPlayerIndex);
        return currentPlayer.getName();
    }
    
    public Player getCurrentPlayer() {
    	
    	if(currentPlayerIndex<0) return null;
    	
    	return listPlayers.get(currentPlayerIndex);
    }
    
    public int getCurrentPlayerIndex() {
    	
    	return currentPlayerIndex;
    }

    public String getConqueredContries(){ return null;}


   // public boolean isPlayerValid(){ return false; }
    
    public boolean checkPlayerExistance(String playerName) {
    	
		for(int i=0;i<listPlayers.size();i++) {
			if(listPlayers.get(i).getName().equals(playerName)) {
				return true;
			}
		}
    	
    	return false;
    }

    
    
	/**
	 * This method keeps track of the currentPlayerIndex and switches to the next player as soon as a player's
	 * turn is over.
	 *Uses Atomic Boolean boolFortificationPhaseOver to take decisions.
	 */
	public void switchNextPlayer() {


			if(currentPlayerIndex==listPlayers.size()-1) {
				this.setCurrentPlayerIndex(0);
			}

			else setCurrentPlayerIndex(this.currentPlayerIndex+1);

	}
	

}
