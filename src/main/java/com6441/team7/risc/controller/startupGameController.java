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
public class startupGameController {
	
	private MapLoaderController mapLoaderController;
	
	//private AtomicInteger continentIdGenerator;
    //private AtomicInteger countryIdGenerator;
    
	private boolean boolMapLoaded;
	private boolean boolGamePlayerAdded;
	private boolean boolAllGamePlayersAdded;
	private boolean boolCountriesPopulated;
	
	private boolean[] boolArrayCountriesPlaced;
	
	private AtomicBoolean boolStartUpPhaseOver;
	
	int currentPlayerIndex;
	
	private MapService mapService;
	
	private ArrayList<Player> players;
	
	/**
	 * This is the constructor of the startup controller.
	 * @param mapController Used for calling some file parsing and map loading methods that had already been used in the map loading phase.
	 * @param mapService Main map is passed as a reference.
	 * @param players list of all players passed as reference as well.
	 * 
	 */
	public startupGameController(MapLoaderController mapController ,MapService mapService,
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
        		
        		if(boolMapLoaded) {
        			
        			String fileName=StringUtils.split(command, WHITESPACE)[1];       		
            		loadMap(command);
        		}
        		else System.out.println("Load Map First");
        	}
        	
        	break;
        	
        case GAME_PLAYER:
        	
        	if(boolMapLoaded && (!boolAllGamePlayersAdded)) {
        		editPlayer(commands);
        	}
        	
        	else {
        		System.out.println("You are past adding phase.All Players Added/Removed");
        	}
        	
        	break;
        	
        case POPULATE_COUNTRY:
        	
        	if(boolMapLoaded) {
        		
        		if(!players.isEmpty()) {
        			
        			boolAllGamePlayersAdded=true;
                	populateCountries();               	
        		}
        		
        		else {
        			
        			System.out.println("No Player Added. Add 1 player atleast");
        		}
        	}
        	
        	else {
        		
        		System.out.println("Load a Map first");
        		
        		if(players.isEmpty()) System.out.println("No Player Added. Add 1 player atleast");

        	}
        	
        	break;
        	
        case PLACE_ARMY:
        	
        	String[] strArr=StringUtils.split(command, WHITESPACE);
        	
        	if(strArr.length!=2) System.out.println("Invalid Placearmy command");
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
        	
        case SHOW_MAP:
        	
        	if(boolCountriesPopulated) showMapFull();
        	
        	else showMap();
        	
        	break;
        	
        default:
            throw new IllegalArgumentException("cannot recognize this command");

        }
        
	}

	
	//CHANGE STATE TO REINFORCEMENT IN MAPSERVICE AFTER END OF LAST METHOD

	//Assign randomly countries to players
	//Calculate number of initial soldiers allocated to players based on numPlayers
	//Assign 1 army to each country and decrement from a player's total
	/**
	 * This method randomly assigns countries to players.
	 * The random technique used in this method is shuffling the list of countries 3 times so that they are no
	 * longer in order of id. This is done by using a stack.
	 * The countries which are mixed and no longer in order are then allocated to players in round-robin fashion.
	 */
	private void populateCountries() {
		
		if(!boolCountriesPopulated) {
			
			int numPlayers=players.size();
			System.out.println("NumPlayers: "+numPlayers);
			
			//CHECK IF ONLY 1 PLAYER: PLAYER WINS
			if(numPlayers==1) {
				System.out.println("PLAYER "+players.get(0).getName()+" WINS");
				endGame();
			}
			
			else if(numPlayers==0) {
				System.out.println("No Players Added. Try again");
			}
			
			else if(numPlayers>9) {
				System.out.println("Player limit exceeded. Cannot Proceed");
			}
			
			else {
				
				int numInitialArmies=determineNumInitialArmies(numPlayers);
				assignInitialArmies(numInitialArmies);
				
				System.out.println(numInitialArmies);
				
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
					System.out.println(c.getId()+" "+c.getCountryName()+" "+c.getPlayer().getName()
							+" "+c.getSoldiers());
				}
							
				//gameplayer -add keshav -add jenny -remove jenny -add binsar -add jenny -add bikash -add keshav -add lol
				
				System.out.println("Countries Populated. Start placing your armies now.");
				
				this.boolCountriesPopulated=true;
				
				this.boolArrayCountriesPlaced=new boolean[players.size()];
				
				for(Player p:players) {
					System.out.println("Remaining Armies for "+p.getName()
											+": "+p.getArmies());
				}
				
				this.currentPlayerIndex=0;
				System.out.println("\nCurrent Player:"+players.get(this.currentPlayerIndex).getName());
				
			}

		}
		
		else {
			System.out.println("Countries already populated");
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
	
	
	private void editPlayer(String[] s) {
		
			 Arrays.stream(s).forEach(this::editPlayerFromUserInput);				
	
	}

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
    
    private void addPlayer(String[] s) {
    	
    	if(players.size()<9) {
    		
        	try {
        		
        		String playerName=convertFormat(s[1]);
        		
        		boolean nameFound=false;
        		
        		for(int i=0;i<players.size();i++) {
        			if(players.get(i).getName().equals(playerName)) {
        				nameFound=true;
        				System.out.println("Player Already Exists. Try different name");
        				break;
        			}
        		}
        		
        		if(!nameFound) {
        			players.add(new Player(playerName));
        			System.out.println("Player Added");
        		}
        		
        		
        		
        	}
        	
        	catch(Exception e) {
        		throw new PlayerEditException("gameplayer command: cannot add/remove it is not valid", e);
        	}
    		
    	}
    	
    	else {
    		System.out.println("Limit of 9 players reached.");
    	}
    	

    }
    
    
    private void removePlayer(String[] s) {
    	
    	try {
    		
    		String playerName=convertFormat(s[1]);
    		
    		boolean nameFound=false;
    		
    		for(int i=0;i<players.size();i++) {
    			if(players.get(i).getName().equals(playerName)) {
    				nameFound=true;
    				players.remove(i);
    				System.out.println("Player Removed");
    				break;
    			}
    		}
    		
    		if(!nameFound) System.out.println("gameplayer command: cannot remove, player does not exist");
    		
    	}
    	
    	catch(Exception e) {
    		throw new PlayerEditException("gameplayer command: cannot add/remove it is not valid", e);
    	}
    }
    
    
	Optional<String> loadMap(String s) {
		
	
	        String[] commands = StringUtils.split(s, " ");

	        if (commands.length != 2) {
	        	
	          //  view.displayMessage("The command editmap is not valid");
	           System.out.println("Command LoadMap is not valid"); 
	            return Optional.empty();
	        }

	        String command = commands[0];
	        String path = commands[1];

	        if (command.toLowerCase(Locale.CANADA).equals("loadmap")) {
	            //readFile(path);
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

	       // view.displayMessage("The command loadmap is not valid");
	        System.out.println("Command LoadMap is not valid"); 
	        return Optional.empty();

		//NEED TO UPDATE BOOLFILENAME
		
	}
	
   
    private boolean validateMap() {

        if(mapService.isMapValid()){
            //view.displayMessage("map is valid");
        	System.out.println("Map is Valid");
            return true;
        }
        else{
            //view.displayMessage("map is not valid");
        	System.out.println("Map is not Valid");
            return false;
        }
    }

    private void showMap() {
        mapService.printCountryInfo();
        mapService.printContinentInfo();
        mapService.printNeighboringCountryInfo();
    }
	
    private String convertFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase(Locale.CANADA);
    }

    
    private int endGame() {
    	return 0;
    }
    

    
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
        			
        		}
        	}
        	
        	if(!countryFound) System.out.println("Wrong Country Name!!");
        	
        	else {
        		
        		if(currentPlayer.getArmies()==0) {
        			boolArrayCountriesPlaced[currentPlayerIndex]=true;
        			System.out.println("All armies placed for "+currentPlayer.getName());
        		}
        		
        		//IF ALL Players have numArmies 0: Startup Phase Over, Switch To Next Phase 
        		boolStartUpPhaseOver.set(true);
        		
        		for(boolean b: boolArrayCountriesPlaced) {
        			
        			if(!b) {
        				boolStartUpPhaseOver.set(false);
        			}    			
        		}
        		
        		if(boolStartUpPhaseOver.get()) {
        			System.out.println("All Armies Placed.\n.");
        			this.mapService.setState(GameState.REINFORCE);
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
    
    private void switchToNextPlayer() {
    	if(currentPlayerIndex==(players.size()-1)) currentPlayerIndex=0;
		
		else currentPlayerIndex++;
    	
    	showPlayer();
    	
    }
    
    public void placeAll() {
    	
    	for(Player p:players) {
    		
    		while(p.getArmies()>0) {
    			
    			int randomIndex=ThreadLocalRandom.current().nextInt(0,p.countryPlayerList.size());
    			
    			p.countryPlayerList.get(randomIndex).addSoldiers(1);
    			p.reduceArmy(1);
    			
    		}
    		
    	}
    	System.out.println("All Players Placed.");
    	showAllPlayers();
    	
    	this.boolStartUpPhaseOver.set(true);
    	this.mapService.setState(GameState.REINFORCE);
    }
    
    public void showAllPlayers() {
    	
    	for(Player p:players) {
    		
        	Collections.sort(p.countryPlayerList, new Comparator<Country>() {

    			@Override
    			public int compare(Country c1, Country c2) {

    				return c1.getContinentName().compareTo(c2.getContinentName());
    			}
        		
        	}
        	);
        	
        	System.out.println("Current Player: "+p.getName()+
        			" , Num Armies Remaining: "+p.getArmies());
        	
        	System.out.println("Continent \t\t\t\t Country \t\t\t\t NumArmies");
        	
        	for(Country c :p.countryPlayerList) {
        		System.out.println(c.getContinentName()+"\t\t\t"+c.getCountryName()
        						+"\t\t\t"+c.getSoldiers());
        	}

    	}

    }
    
    
    public void showPlayer() {
    	
    	Collections.sort(players.get(currentPlayerIndex).countryPlayerList, new Comparator<Country>() {

			@Override
			public int compare(Country c1, Country c2) {

				return c1.getContinentName().compareTo(c2.getContinentName());
			}
    		
    	}
    	);
    	
    	System.out.println("Current Player: "+players.get(currentPlayerIndex).getName()+
    			" , Num Armies Remaining: "+players.get(currentPlayerIndex).getArmies());
    	
    	System.out.println("Continent \t\t\t\t Country \t\t\t\t NumArmies");
    	
    	for(Country c:players.get(currentPlayerIndex).countryPlayerList) {
    		System.out.println(c.getContinentName()+"\t\t\t"+c.getCountryName()+"\t\t\t"+c.getSoldiers());
    	}
    	
    }
    
    
    
    public void showMapFull() {
    	
    	System.out.println("Entered Showmap");
    	
    	for(Map.Entry<Integer, Set<Integer>> item :
    						mapService.getContinentCountriesMap().entrySet()) {
    		
    		int key=(int) item.getKey();
    		   		
    		
    		Optional<Continent> optionalContinent=mapService.getContinentById(key);
    		Continent currentContinent= (Continent) optionalContinent.get();
    		
    		System.out.println("\t\t\t\t\t\t\t\t\tContinent "+currentContinent.getName());
    		System.out.println();
    		
    		Set<Integer> value=item.getValue();
    		
    		for(Integer i:value) {
    			//For Each Country In Continent, Get details + Adjacency Countries
    			Optional<Country> optionalCountry=mapService.getCountryById(i);
    			
    			Country currentCountry=optionalCountry.get();
    			String strCountryOutput="";
    			
    			strCountryOutput+=currentCountry.getCountryName()+" "+currentCountry.getPlayer().getName()+
    					" "+currentCountry.getSoldiers()+" soldiers ";
    			
    			Set<Integer> adjCountryList= mapService.getAdjacencyCountriesMap().get(i);
    			
    			for(Integer j:adjCountryList) {
    				strCountryOutput+=" --> "+mapService.getCountryById(j).get().getCountryName();
    			}
    			
    			System.out.println(strCountryOutput+"\n");
    		}
    		
    		
    	}

    }
    
    
}
