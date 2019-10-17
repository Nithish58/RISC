package com6441.team7.risc.controller;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;
import static java.util.Objects.isNull;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com6441.team7.risc.view.CommandPromptView;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com6441.team7.risc.api.exception.ContinentEditException;
import com6441.team7.risc.api.exception.ContinentParsingException;
import com6441.team7.risc.api.exception.CountryParsingException;
import com6441.team7.risc.api.exception.MissingInfoException;
import com6441.team7.risc.api.exception.NeighborParsingException;
import com6441.team7.risc.api.exception.PlayerEditException;
import com6441.team7.risc.api.model.Continent;
import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.RiscCommand;

/**
 * 
 * This class implements the different StartUpPhase Functions of the game.
 * It also focuses on ensuring that commands are run in the order they are intended too.
 * Consequently, it carries out many checks to enforce order when users enter commands.
 * For example, it does not allow countries to be populated before a player has been added.
 * Another example is that it does not allow more players to be added/removed after countries have been populated.
 * 
 * Some important functions this class implements are:
 * <ul>
 * <li>Allowing the loading of an existing game map to play.</li>
 * <li>Adding/Removing Game Players</li>
 * <li>Enforcing checks on player limits. Maximum of 9 players allowed for this game.</li>
 * <li>Determining number of initial armies allocated to each player.</li>
 * <li>Randomly assigning countries to players initially</li>
 * <li>Allowing players to place armies on their countries in a round-robin fashion.</li>
 * <li>Providing a placeall function to speedup the army placement process</li>
 * </ul>
 * 
 * @author Keshav
 *
 */
public class StartupGameController {
	
	/**
	 * Reference to map loader controller
	 */
	private MapLoaderController mapLoaderController;
	
	/**
	 * Reference to view
	 */
	private CommandPromptView view;

    /**
     * Boolean that checks if map is loaded.
     * Used to control game flow.
     */
	private boolean boolMapLoaded;
	/**
	 * Boolean that checks if atleast 1 player is added.
	 * If this is false, does not proceed further and asks addition of at least 1 player.
	 * Used to control game flow.
	 */
	private boolean boolGamePlayerAdded;
	/**
	 * Boolean that checks if all players have been added.
	 * Set to true when populate countries called
	 * Can no longer add more players after this point.
	 * Used to control game flow.
	 */
	private boolean boolAllGamePlayersAdded;
	/**
	 * Boolean that checks if a country have been successfully populated.
	 * If true, allows proceeding to next phase.
	 * Else need to populate countries first
	 * Used to control game flow.
	 */
	private boolean boolCountriesPopulated;
	/**
	 * Array of boolean that checks if all armies have been placed on all countries.
	 * If true, allows proceeding to next phase; else program keep looping over players until all armies have been placed.
	 */
	private boolean[] boolArrayCountriesPlaced;
	/**
	 * Atomic Boolean that is set to true after all countries have been populated.
	 * When true, does not give control to startup phase again.
	 */
	private AtomicBoolean boolStartUpPhaseOver;
	/**
	 * Keeps track of current player index to access current player from list of players
	 */
	int currentPlayerIndex;
	/**
	 * Reference to mapService model
	 */
	private MapService mapService;
	/**
	 * List of players playing the game
	 */
	private ArrayList<Player> players;
	
	/**
	 * This is the constructor of the startup controller.
	 * @param mapController Used for calling some file parsing and map loading methods that had already been used in the map loading phase.
	 * @param mapService Main map is passed as a reference.
	 * @param players list of all players passed as reference as well.
	 * 
	 */
	public StartupGameController(MapLoaderController mapController ,MapService mapService,
			ArrayList<Player> players) {
		
		this.mapLoaderController=mapController;
		
		this.boolMapLoaded=false;
		
		
		this.boolAllGamePlayersAdded=false;
		this.boolGamePlayerAdded=false;
		this.boolCountriesPopulated=false;
			
		this.mapService=mapService;
		
		this.players=players; 

	}
	
/**
 * This method "routes" different commands to their respective functions.
 * <ul>
 * <li>It first analyses the commands itself and check for their validity.</li>
 * <li> If valid, it calls their functions </li>
 * 
 * </ul>
 * @param command String of user command
 * @param boolStartUpPhaseOver true if start up phase is over (when all armies placed). This boolean value then allows switching to next state.
 * 
 */
	public void readCommand(String command, AtomicBoolean boolStartUpPhaseOver) {
		
        RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);

        String[] commands = {};

        if(command.contains("-")){
            command = StringUtils.substringAfter(command, "-");
            commands = StringUtils.split(command, "-");
        }
        
        this.boolStartUpPhaseOver=boolStartUpPhaseOver;

        switch(commandType) {
        	
        case LOAD_MAP:
        	
        	if(!boolMapLoaded) {
        		
        		String fileName=StringUtils.split(command, WHITESPACE)[1];       		
        		loadMap(command);        		
        	}
        	
        	else {
        		
        		if(boolMapLoaded && !boolCountriesPopulated) {
        			
        			String fileName=StringUtils.split(command, WHITESPACE)[1];       		
            		loadMap(command);
        		}
        		else {
        			if(!boolMapLoaded) view.displayMessage("Load Map First");
        			
        			else if(boolCountriesPopulated) view.displayMessage("Countries already populated. Cannot "
        					+ "load new map now.");
        		}
        	}
        	
        	break;
        	
        case GAME_PLAYER:
        	
        	if(boolMapLoaded && (!boolAllGamePlayersAdded)) {
        		editPlayer(commands);
        	}
        	
        	else {
        		
        		if(!boolMapLoaded) view.displayMessage("Load Map First.");
        		
        		else if(boolAllGamePlayersAdded)	view.displayMessage("You are past adding phase.All Players Added/Removed");
        	}
        	
        	break;
        	
        case POPULATE_COUNTRY:
        	
        	if(boolMapLoaded) {
        		
        		if(!players.isEmpty()) {
        			
        			boolAllGamePlayersAdded=true;
                	populateCountries();               	
        		}
        		
        		else {
        			
        			view.displayMessage("No Player Added. Add 1 player atleast");
        		}
        	}
        	
        	else {
        		
        		view.displayMessage("Load a Map first");
        		
        		if(players.isEmpty()) view.displayMessage("No Player Added. Add 1 player atleast");

        	}
        	
        	break;
        	
        case PLACE_ARMY:
        	
        	String[] strArr=StringUtils.split(command, WHITESPACE);
        	
        	if(strArr.length!=2) view.displayMessage("Invalid Placearmy command");
        	else {
        		placeArmy(strArr[1]);
        	}
        	
        	break;
        	
        case PLACE_ALL:
        	placeAll();
        	break;
        	
        case SHOW_PLAYER:
        	showPlayer();
        	break;
        	
        case SHOW_ALL_PLAYERS:
        	showAllPlayers();
        	break;
        	
        case SHOW_PLAYER_ALL_COUNTRIES:       	
        	showPlayerAllCountries();
        	break;
        
        case SHOW_PLAYER_COUNTRIES:
        	showPlayerCountries();
        	break;
        	
        case SHOW_MAP:
        	
        	if(boolCountriesPopulated) showMapFull();
        	
        	else mapLoaderController.showMapFull();
        	
        	break;
        	
        default:
            throw new IllegalArgumentException("cannot recognize this command");

        }
        
	}

	/**
	 * This method randomly assigns countries to players.
	 * The random technique used in this method is shuffling the list of countries 3 times so that they are no
	 * longer in order of id. This is done by using a stack.
	 * The countries which are mixed and no longer in order are then allocated to players in round-robin fashion.
	 * Then assigns 1 army to each country.
	 */
	private void populateCountries() {
		
		if(!boolCountriesPopulated) {
			
			int numPlayers=players.size();
			view.displayMessage("NumPlayers: "+numPlayers);
			
			//CHECK IF ONLY 1 PLAYER: PLAYER WINS
			if(numPlayers==1) {
				view.displayMessage("PLAYER "+players.get(0).getName()+" WINS");
				endGame();
			}
			
			else if(numPlayers==0) {
				view.displayMessage("No Players Added. Try again");
			}
			
			else if(numPlayers>9) {
				view.displayMessage("Player limit exceeded. Cannot Proceed");
			}
			
			else {
				
				int numInitialArmies=determineNumInitialArmies(numPlayers);
				assignInitialArmies(numInitialArmies);
				
				view.displayMessage("Number of Initial Armies:"+numInitialArmies+"\n");
				
				Stack<Country> stackCountry=new Stack<>();
				
				Iterator setIter=mapService.getCountries().iterator();
				
				while(setIter.hasNext()) {
					stackCountry.push((Country) setIter.next());
				}
				
				Collections.shuffle(stackCountry);
				Collections.shuffle(stackCountry);
				Collections.shuffle(stackCountry);
				
				int currentPlayerIndex=0;
				
				while(!stackCountry.isEmpty()) {
					
					Country currentCountry=stackCountry.pop();
					
					currentCountry.setPlayer(players.get(currentPlayerIndex));
					
					currentCountry.addSoldiers(1);
					
					players.get(currentPlayerIndex).reduceArmy(1);
					
					players.get(currentPlayerIndex).countryPlayerList.add(currentCountry);
					
					currentPlayerIndex++;
					
					if(currentPlayerIndex==players.size()) currentPlayerIndex=0;
					
				}
				
				
				for(Country c:mapService.getCountries()) {
					view.displayMessage(c.getId()+" "+c.getCountryName()+" "+c.getPlayer().getName()
							+" "+c.getSoldiers());
				}
				
				view.displayMessage("Countries Populated. Start placing your armies now.");
				
				this.boolCountriesPopulated=true;
				
				this.boolArrayCountriesPlaced=new boolean[players.size()];
				
				for(Player p:players) {
					view.displayMessage("Remaining Armies for "+p.getName()
											+": "+p.getArmies());
				}
				
				this.currentPlayerIndex=0;
				view.displayMessage("\nCurrent Player:"+players.get(this.currentPlayerIndex).getName());
				
			}

		}
		
		else {
			view.displayMessage("Countries already populated");
		}
		
	}
	
	/**
	 * This method determines the initial number of armies allocated to each player.
	 * The rules are an approved adaptation of Hasbro 1 by Mr Joey Paquet.
	 * They are as follows:
	 * <ul>
	 * <li>2 Players: 40 armies</li>
	 * <li>3 Players: 35 armies</li>
	 * <li>4 Players: 30 armies</li>
	 * <li>...</li>
	 *</ul>
	 *This pattern goes on until a maximum of 9 players with 5 armies each.
	 * 
	 * @param numPlayers
	 * @return number of intial armies allocated to each player
	 */
	private int determineNumInitialArmies(int numPlayers) {
		return 40-((numPlayers-2)*5);
	}
	
	/**
	 * Set the number of initial armies for every player respectively.
	 * @param numArmies number of initial armies allocated
	 */
	private void assignInitialArmies(int numArmies) {
		
		for(Player p: players) {
			p.setArmies(numArmies);
		}
		
	}


	/**
	 * for each string, call editPlayerFromUserInput method
	 * @param s
	 */
	private void editPlayer(String[] s) {
		
			 Arrays.stream(s).forEach(this::editPlayerFromUserInput);				
	
	}


	/**
	 * if the command is add, call addPlayer()
	 * if the command is remove, call removePlayer()
	 * else throw exception
	 * @param command string
	 */
    private void editPlayerFromUserInput(String s) {
        String[] commands = StringUtils.split(s, " ");
        switch (convertFormat(commands[0])) {
            case "add":
                addPlayer(commands);
                break;
            case "remove":
                removePlayer(commands);
                break;
            default:
                throw new ContinentEditException("The gameplayer command " + s + " is not valid.");
        }
    }

	/**
	 * validate each player info, if player info is valid, add it to the list of players
	 * if not valid, throw an exception
	 * @param array of string commands
	 */
	private void addPlayer(String[] s) {
    	
    	if(players.size()<9) {
    		
        	try {
        		
        		String playerName=convertFormat(s[1]);
        		
        		boolean nameFound=false;
        		
        		for(int i=0;i<players.size();i++) {
        			if(players.get(i).getName().equals(playerName)) {
        				nameFound=true;
        				view.displayMessage("Player Already Exists. Try different name");
        				break;
        			}
        		}
        		
        		if(!nameFound) {
        			players.add(new Player(playerName));
        			view.displayMessage("Player Added: "+playerName);
        		}
        		
        		
        		
        	}
        	
        	catch(Exception e) {
        		throw new PlayerEditException("gameplayer command: cannot add/remove it is not valid", e);
        	}
    		
    	}
    	
    	else {
    		view.displayMessage("Limit of 9 players reached.");
    	}
    	

    }


	/**
	 * validate player info, if player info is valid, remove it to the list of players
	 * if not valid, throw an exception
	 * @param array of string commands
	 */
	private void removePlayer(String[] s) {
    	
    	try {
    		
    		String playerName=convertFormat(s[1]);
    		
    		boolean nameFound=false;
    		
    		for(int i=0;i<players.size();i++) {
    			if(players.get(i).getName().equals(playerName)) {
    				nameFound=true;
    				players.remove(i);
    				view.displayMessage("Player Removed: "+playerName);
    				break;
    			}
    		}
    		
    		if(!nameFound) view.displayMessage("gameplayer command: cannot remove, player does not exist");
    		
    	}
    	
    	catch(Exception e) {
    		throw new PlayerEditException("gameplayer command: cannot add/remove it is not valid", e);
    	}
    }
	
	/**
	 * Getter Method for view
	 * @return view 
	 */
    public CommandPromptView getView() {
    	return view;
    }
    
    /**
     * Setter method for view
     * @param view
     */
    public void setView(CommandPromptView v) {
    	this.view=v;
    	
    }

	/**
	 * load map from the map file
	 * @param mapname
	 * @return a string instead of null if map not loaded successfully, that is why optional is used/
	 */
	Optional<String> loadMap(String s) {
		
	
	        String[] commands = StringUtils.split(s, " ");

	        if (commands.length != 2) {
	        	
	          //  view.displayMessage("The command editmap is not valid");
	           view.displayMessage("Command LoadMap is not valid"); 
	            return Optional.empty();
	        }

	        String command = commands[0];
	        String path = commands[1];

	        if (command.toLowerCase(Locale.CANADA).equals("loadmap")) {

	        	this.mapLoaderController.setContinentIdGenerator(0);
	            this.mapLoaderController.setCountryIdGenerator(0);
	        	mapService.emptyMap();
	        	mapLoaderController.readFile(path);
	        
	        	showMap();
	        	
	        	if(validateMap()) {
	        		boolMapLoaded=true;
	        	}
	        	
	            return Optional.of(path);
	        }

	        view.displayMessage("Command LoadMap is not valid"); 
	        return Optional.empty();
		
	}


	/**
	 * validate the map
	 * @return true if valid, false if not valid
	 */
	private boolean validateMap() {

        if(mapService.isMapValid()){
            //view.displayMessage("map is valid");
        	view.displayMessage("Map is Valid");
            return true;
        }
        else{
            //view.displayMessage("map is not valid");
        	view.displayMessage("Map is not Valid");
            return false;
        }
    }

	/**
	 * show map information including countries, continents, and neighboring countries
	 */
	private void showMap() {
        mapService.printCountryInfo();
        mapService.printContinentInfo();
        mapService.printNeighboringCountryInfo();
    }

	/**
	 * delete white spaces and make the string to lower cases
	 * @param name
	 * @return
	 */
	private String convertFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase(Locale.CANADA);
    }

	/**
	 * end the game
	 * called when only 1 player is present.
	 */
	private void endGame() {
    	view.displayMessage("Game Ends");
    	System.exit(0);;
    }

	/**
	 * place an army on the country by its countryName
	 * by each player until all players have placed all their armies
	 * @param countryName
	 */
	public void placeArmy(String countryName) {
    	
    	boolean countryFound=false;
    	Player currentPlayer=players.get(currentPlayerIndex);
    	
    	//Check if all armies of a specific player placed: if yes: switch to next player, else place army
    	if(!boolArrayCountriesPlaced[currentPlayerIndex]) {
    		
    		for(Country c:currentPlayer.countryPlayerList) {
    		    
        		if(countryName.equalsIgnoreCase(c.getCountryName())) {
        			countryFound=true;
        			
        			currentPlayer.reduceArmy(1);
        			
        			c.addSoldiers(1);		
        			
        			view.displayMessage(currentPlayer.getName()+" placed army successfully.");
        			if(currentPlayer.getArmies()==0) {
        				boolArrayCountriesPlaced[currentPlayerIndex]=true;        				
        			}
        			break;
        		}
        	}
        	
        	if(!countryFound) view.displayMessage("Wrong Country Name!!");
        	
        	else {
        		
        		if(currentPlayer.getArmies()==0) {
        			boolArrayCountriesPlaced[currentPlayerIndex]=true;
        			view.displayMessage("All armies placed for "+currentPlayer.getName());
        		}
        		
        		//IF ALL Players have numArmies 0: Startup Phase Over, Switch To Next Phase 
        		boolStartUpPhaseOver.set(true);
        		
        		for(boolean b: boolArrayCountriesPlaced) {
        			
        			if(!b) {
        				boolStartUpPhaseOver.set(false);
        			}    			
        		}
        		
        		if(boolStartUpPhaseOver.get()) {
        			view.displayMessage("All Armies Placed for all players.\n.");
        			this.mapService.setState(GameState.REINFORCE);
        			switchToNextPlayer();
        		}
        		
        		else {
        			//Switch to Next Player
            		switchToNextPlayer();
        		}
        	}
    		
    	}

    	else {
    		switchToNextPlayer();
    	}
	
    }

	/**
	 * switchPlayers
	 */
	private void switchToNextPlayer() {
    	if(currentPlayerIndex==(players.size()-1)) currentPlayerIndex=0;
		
		else currentPlayerIndex++;
    	
    	view.displayMessage("Player Turn: "+players.get(currentPlayerIndex).getName());
    	//showPlayer();
    	
    }

	/**
	 * automatically randomly place all remaining unplaced armies for all players
	 */
	public void placeAll() {
    	
    	for(Player p:players) {
    		
    		while(p.getArmies()>0) {
    			
    			int randomIndex=ThreadLocalRandom.current().nextInt(0,p.countryPlayerList.size());
    			
    			p.countryPlayerList.get(randomIndex).addSoldiers(1);
    			p.reduceArmy(1);
    			
    		}
    		
    	}
    	view.displayMessage("All Players Placed.");
    	showAllPlayers();
    	
    	this.boolStartUpPhaseOver.set(true);
    	this.mapService.setState(GameState.REINFORCE);
    	
    	view.displayMessage("Player Turn: "+players.get(0).getName());
    }

	/**
	 * show players information including current player name, number of armies, continent,
	 * countries, and number of armies on each country
	 */
	public void showAllPlayers() {
    	
    	for(Player p:players) {
    		
        	Collections.sort(p.countryPlayerList, new Comparator<Country>() {

    			@Override
    			public int compare(Country c1, Country c2) {

    				return c1.getContinentName().compareTo(c2.getContinentName());
    			}
        		
        	}
        	);
        	
        	view.displayMessage("Current Player: "+p.getName()+
        			" , Num Armies Remaining: "+p.getArmies());
        	
        	for(Country c :p.countryPlayerList) {
        		view.displayMessage(c.getContinentName()+"\t"+c.getCountryName()
        						+"\t"+c.getSoldiers());
        	}

    	}

    }

	/**
	 * show all the players including current player name, number of armies, continent,
	 * 	 * countries, and number of armies on each country
	 */
	public void showPlayer() {
    	
    	Collections.sort(players.get(currentPlayerIndex).countryPlayerList, new Comparator<Country>() {

			@Override
			public int compare(Country c1, Country c2) {

				return c1.getContinentName().compareTo(c2.getContinentName());
			}
    		
    	}
    	);
    	
    	view.displayMessage("Current Player: "+players.get(currentPlayerIndex).getName()+
    			" , Num Armies Remaining: "+players.get(currentPlayerIndex).getArmies());
    	
    	for(Country c:players.get(currentPlayerIndex).countryPlayerList) {
    		view.displayMessage(c.getContinentName()+"\t"+c.getCountryName()+"\t"+c.getSoldiers());
    	}
    	
    }


	/**
	 * show countries occupied by the current player
	 */
	public void showPlayerCountries() {
    	
    	Player currentPlayer=players.get(currentPlayerIndex);
    	
    	for(Map.Entry<Integer, Set<Integer>> item :
    						mapService.getContinentCountriesMap().entrySet()) {
    		
    		int key=(int) item.getKey();
    		   		
    		
    		Optional<Continent> optionalContinent=mapService.getContinentById(key);
    		Continent currentContinent= (Continent) optionalContinent.get();
    		
    		view.displayMessage("\t\t\t\t\t\t\t\t\tContinent "+currentContinent.getName());
    		view.displayMessage("\n");
    		
    		Set<Integer> value=item.getValue();
    		
    		for(Integer i:value) {
    			//For Each Country In Continent, Get details + Adjacency Countries
    			Optional<Country> optionalCountry=mapService.getCountryById(i);
    			
    			Country currentCountry=optionalCountry.get();
    			
    			if(currentCountry.getPlayer().getName().equalsIgnoreCase(currentPlayer.getName())) {
    				
        			String strCountryOutput="";
        			
        			strCountryOutput+=currentCountry.getCountryName().toUpperCase()+":"+currentCountry.getPlayer().getName().toUpperCase()+
        					", "+currentCountry.getSoldiers()+" soldiers   ";
        			
        			Set<Integer> adjCountryList= mapService.getAdjacencyCountriesMap().get(i);
        			
        			for(Integer j:adjCountryList) {
        				
        				if(mapService.getCountryById(j).get().getPlayer().getName()
        						.equalsIgnoreCase(currentPlayer.getName())){
        	        				
        	        		strCountryOutput+=" --> "+mapService.getCountryById(j).get().getCountryName()+
        	        				"("+mapService.getCountryById(j).get().getPlayer().getName()+
        	        				":"+mapService.getCountryById(j).get().getSoldiers()+")";
        						}
        				
        			}
        			
        			view.displayMessage(strCountryOutput+"\n");    				
    				
    			}
    			

    		}

    	}

    }

	/**
	 * show all players and their occupying countries
	 */
	public void showPlayerAllCountries() {
    	
    	Player currentPlayer=players.get(currentPlayerIndex);
    	
    	for(Map.Entry<Integer, Set<Integer>> item :
    						mapService.getContinentCountriesMap().entrySet()) {
    		
    		int key=(int) item.getKey();
    		   		
    		
    		Optional<Continent> optionalContinent=mapService.getContinentById(key);
    		Continent currentContinent= (Continent) optionalContinent.get();
    		
    		view.displayMessage("\t\t\t\t\t\t\t\t\tContinent "+currentContinent.getName());
    		view.displayMessage("\n");
    		
    		Set<Integer> value=item.getValue();
    		
    		for(Integer i:value) {
    			//For Each Country In Continent, Get details + Adjacency Countries
    			Optional<Country> optionalCountry=mapService.getCountryById(i);
    			
    			Country currentCountry=optionalCountry.get();
    			
    			if(currentCountry.getPlayer().getName().equalsIgnoreCase(currentPlayer.getName())) {
    				
        			String strCountryOutput="";
        			
        			strCountryOutput+=currentCountry.getCountryName().toUpperCase()+":"+currentCountry.getPlayer().getName().toUpperCase()+
        					", "+currentCountry.getSoldiers()+" soldiers   ";
        			
        			Set<Integer> adjCountryList= mapService.getAdjacencyCountriesMap().get(i);
        			
        			for(Integer j:adjCountryList) {
        	        				
        	        		strCountryOutput+=" --> "+mapService.getCountryById(j).get().getCountryName()+
        	        				"("+mapService.getCountryById(j).get().getPlayer().getName()+
        	        				":"+mapService.getCountryById(j).get().getSoldiers()+")";
        						
        				
        			}
        			
        			view.displayMessage(strCountryOutput+"\n");    				
    				
    			}
    			

    		}

    	}

    }


	/**
	 * print map in a format of continent, countries belong to the continent
	 * for each country, print the players that occupy, the number of soldiers and
	 * its neighboring countries
	 */
	public void showMapFull() {
    	
    	for(Map.Entry<Integer, Set<Integer>> item :
    						mapService.getContinentCountriesMap().entrySet()) {
    		
    		int key=(int) item.getKey();
    		   		
    		
    		Optional<Continent> optionalContinent=mapService.getContinentById(key);
    		Continent currentContinent= (Continent) optionalContinent.get();
    		
    		view.displayMessage("\t\t\t\t\t\t\t\t\tContinent "+currentContinent.getName());
    		view.displayMessage("\n");
    		
    		Set<Integer> value=item.getValue();
    		
    		for(Integer i:value) {
    			//For Each Country In Continent, Get details + Adjacency Countries
    			Optional<Country> optionalCountry=mapService.getCountryById(i);
    			
    			Country currentCountry=optionalCountry.get();
    			String strCountryOutput="";
    			
    			strCountryOutput+=currentCountry.getCountryName().toUpperCase()+":"+currentCountry.getPlayer().getName().toUpperCase()+
    					", "+currentCountry.getSoldiers()+" soldiers   ";
    			
    			Set<Integer> adjCountryList= mapService.getAdjacencyCountriesMap().get(i);
    			
    			for(Integer j:adjCountryList) {
    				strCountryOutput+=" --> "+mapService.getCountryById(j).get().getCountryName()+
    						"("+mapService.getCountryById(j).get().getPlayer().getName()+
    						":"+mapService.getCountryById(j).get().getSoldiers()+")";
    			}
    			
    			view.displayMessage(strCountryOutput+"\n");
    		}

    	}

    }
    

	
    
}
