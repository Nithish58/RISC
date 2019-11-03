package com6441.team7.risc.api.model;

import com6441.team7.risc.api.wrapperview.PlayerChangeWrapper;
import com6441.team7.risc.api.wrapperview.PlayerEditWrapper;

import java.util.*;

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
    
    
    
    
    private Player currentPlayer;


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
		
		Player currentPlayer=listPlayers.get(currentPlayerIndex);
		PlayerChangeWrapper playerChangeWrapper=new PlayerChangeWrapper(currentPlayer);
		
		setChanged();
		notifyObservers(playerChangeWrapper);
	}

    public Player addPlayer(String name){
        //TODO: add a player in the playerList
    	Player newPlayer=new Player(name);
    	listPlayers.add(newPlayer);
    	
		//Add Player to Wrapper function and send wrapper function to observers
		PlayerEditWrapper playerEditWrapper=new PlayerEditWrapper();
		playerEditWrapper.setAddedPlayer(newPlayer);
    	
    	setChanged();
        notifyObservers(playerEditWrapper);
        
    	return newPlayer;
    }

    public boolean removePlayer(String playerName){
        //TODO: remove a player in the playerList
    	
		for(int i=0;i<listPlayers.size();i++) {
			
			if(listPlayers.get(i).getName().equals(playerName)) {
				
				Player removedPlayer=listPlayers.remove(i);
				
				//Add Player to Wrapper function and send wrapper function to observers
				PlayerEditWrapper playerEditWrapper=new PlayerEditWrapper();
				playerEditWrapper.setRemovedPlayer(removedPlayer);
				
				setChanged();
		    	//NOTIFY BEFORE RETURN
		        notifyObservers(playerEditWrapper);
		    				
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
    
    public Player getPlayerByName(String name) {
    	
    	for(Player p:listPlayers) {
    		if(p.getName().equals(name)) return p;
    	}
    	
    	return null;
    }
    
    public Player getCurrentPlayer() {
    	
    	if(currentPlayerIndex<0) return null;
    	
    	return listPlayers.get(currentPlayerIndex);
    }
    
    public int getCurrentPlayerIndex() {
    	
    	return currentPlayerIndex;
    }

    //jenny: add this method to set current player
    public void setCurrentPlayer(Player player){
        currentPlayer = player;
    }

   // public boolean isPlayerValid(){ return false; }
    
    public boolean checkPlayerExistance(String playerName) {
    	
		for(int i=0;i<listPlayers.size();i++) {
			if(listPlayers.get(i).getName().equals(playerName)) {
				return true;
			}
		}
    	
    	return false;
    }

    //jenny: call method in mapService to get total number of countries conquered by the player
    public long getConqueredCountriesNumber(Player player){

        return mapService.getConqueredCountriesNumber(player);
    }

    //jenny: call method in mapService to get total number of continents conquered by the player
    public long getReinforcedArmyByConqueredContinents(Player player){
        return mapService.getReinforceArmyByConqueredContinents(player);
    }

    //jenny: call method in mapService to get total number of countries conquered by the player
    public List<String> getConqueredContries(Player player){
        return mapService.getConqueredCountriesNameByPlayer(player);}



    //jenny: call method in player to reinforceArmy
    public void reinforceArmy(Player player, String country, int armyNum){
        mapService.reinforceArmyToCountry(country, armyNum);
        ReinforcedArmyWrapper reinforcedArmyWrapper = new ReinforcedArmyWrapper(player, country, armyNum);
        setChanged();
        notifyObservers(reinforcedArmyWrapper);
    }

    //jenny: show cards information of the player
    public List<String> showCardsInfo(Player player){
        return player.getCardList();
    }

    //jenny: validate cards validity
    public boolean isTradeInCardsValid(Player player, List<String> cardList){
        return player.meetTradeInCondition(cardList);
    }

    //jenny: remove Cards from Player
    public void removeCards(Player player, List<String> cardList){
        player.removeCards(cardList);
        notifyObservers(player);
    }


    public boolean isPlayerValid(){ return false; }

    
    
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
	
	public int getNextPlayerIndex() {
		
		if((currentPlayerIndex+1)<=listPlayers.size()-1) return currentPlayerIndex+1;
		
		else return 0;
	}
	
	public Player getNextPlayer() {
		return listPlayers.get(getNextPlayerIndex());
	}
	
	/**
	 * Function that notifies all playerService observers that it has been changed and then sends an object to the observers
	 * @param object that can be of different classes (different wrapper classes)
	 */
	public void notifyPlayerServiceObservers(Object object) {
		
		setChanged();
		notifyObservers(object);
	}
	
	/**
	 * This method checks if any player owns any continent.
	 * It loops through all countries in each continent and check if they have the same owner.
	 * Used for domination view and attack phase
	 * @return map of <continent id, player name>, if any player owns the respective continent
	 */
	public Map<Integer, String> checkContinentOwners() {
		
		Map<Integer, Set<Integer>> continentCountriesMap = mapService.getContinentCountriesMap();
		
		Map<Integer, String> continentOwnerMap=new HashMap<>();
		
		//Loop through every continent's countries
		//check if owner is same for all countries of the continent
		
    	for(Map.Entry<Integer, Set<Integer>> item :
			mapService.getContinentCountriesMap().entrySet()) {

    		int key=(int) item.getKey();

    		Optional<Continent> optionalContinent=mapService.getContinentById(key);
    		Continent currentContinent= (Continent) optionalContinent.get();
    		
    		Set<Integer> value=item.getValue();
    		
    		//If continent empty, move to next continent
    		if(value.size()==0) continue;
    		
    		//Only 1 country in continent: player of that country therefore owns continent
    		if(value.size()==1) {
    			
    			int countryId=-1; //initialising variable
    			
    			for(Integer i:value) countryId=i;
    			
    			Optional<Country> optionalCountry=mapService.getCountryById(countryId);
    			Country currentCountry=optionalCountry.get();
    			String currentCountryOwnerName=currentCountry.getPlayer().getName();
    			
    			continentOwnerMap.put(key, currentCountryOwnerName);
    			continue;
    		}
    		
    		boolean boolSameOwner=true;
    		
    		String ownerName="";
    		
    		int counter=0;
    				
    		for(Integer i:value) {
    			//For Each Country In Continent, Get owner
    			Optional<Country> optionalCountry=mapService.getCountryById(i);

    			Country currentCountry=optionalCountry.get();
    			String currentCountryOwnerName=currentCountry.getPlayer().getName();
    			
    			//Set owner of first country as ownerName with which all other country owner names will be compared
    			if(counter==0) {
    				ownerName=currentCountryOwnerName;
    				counter++;
    				continue;
    			}

    			if(!currentCountryOwnerName.equals(ownerName)) {
    				boolSameOwner=false;
    				break;
    			}
    			
    			counter++;
    		}    //End of looping through all countries of 1 continent
		
    		if(boolSameOwner) continentOwnerMap.put(key, ownerName);
    		
    	}
   	
    	return continentOwnerMap;
		
	}  //End of method
	
	/*
	public void fortifyCurrentPlayer(PlayerFortificationWrapper playerFortificationWrapper) {
		getCurrentPlayer().fortify(this, playerFortificationWrapper);
	}
	*/

}
