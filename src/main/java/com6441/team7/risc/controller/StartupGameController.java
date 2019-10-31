package com6441.team7.risc.controller;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;
import static com6441.team7.risc.api.RiscConstants.MAX_NUM_PLAYERS;

import org.apache.commons.lang3.StringUtils;

import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.api.model.RiscCommand;
import com6441.team7.risc.utils.CommonUtils;
import com6441.team7.risc.utils.MapDisplayUtils;
import com6441.team7.risc.view.DominationView;
import com6441.team7.risc.view.GameView;
import com6441.team7.risc.api.exception.ContinentEditException;
import com6441.team7.risc.api.exception.PlayerEditException;
import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.view.PhaseView;

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

//TODO: FILL IN THE EXISTING CODE IN THIS PART
//TODO: move the logic relating to player to PlayerService.class
public class StartupGameController implements Controller{

	//Keshav Refactoring Part
	
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
	
	
	//JENNY REFACTORING PART
	
	
    //private Controller mapLoaderController;
	private MapLoaderController mapLoaderController; //Casted it here instead of casting everytime
	
	
     // as playerService class has a reference for mapService,
     // we don't need to have a attribute here. But in coding efficiency, I provide here.
     // the value of mapService will come from playerService.getMapService();
     // I don't have domination view because, it is the model to update domination view,
     // so i don't give reference here.
	private MapService mapService;
	private PlayerService playerService;
	private GameView phaseView;
	
	private GameView dominationView;

	public StartupGameController(Controller mapController, PlayerService playerService) {
		this.mapLoaderController= (MapLoaderController) mapController;

		this.playerService = playerService;
		this.mapService = playerService.getMapService();
		
		
		//Keshav Refactoring Part
		this.boolMapLoaded=false;	
		this.boolAllGamePlayersAdded=false;
		this.boolGamePlayerAdded=false;
		this.boolCountriesPopulated=false;		

	}

	public void setView(GameView view){
	    this.phaseView = view;
    }
	
	public void setDominationView(GameView domView) {
		this.dominationView=domView;
	}

    //TODO: read command from phaseView and validate command here
    //TODO: if the command is valid, call corresponding method in playerService
    //TODO: if the command is not valid, call phaseView.displayMessage() to show the error message

    @Override
    public void readCommand(String command) throws Exception {
    	
    	RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);

        String[] commands = {};

        if(command.contains("-")){
            command = StringUtils.substringAfter(command, "-");
            commands = StringUtils.split(command, "-");
        }
        
        //this.boolStartUpPhaseOver=boolStartUpPhaseOver;

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
        			if(!boolMapLoaded) phaseView.displayMessage("Load Map First");
        			
        			else if(boolCountriesPopulated)
        				phaseView.displayMessage("Countries already populated. Cannot "
        					+ "load new map now.");
        		}
        	}
        	
        	break;
        	
        case GAME_PLAYER:
        	
        	if(boolMapLoaded && (!boolAllGamePlayersAdded)) {
        		editPlayer(commands);
        	}
        	
        	else {
        		
        		if(!boolMapLoaded) phaseView.displayMessage("Load Map First.");
        		
        		else if(boolAllGamePlayersAdded) phaseView.displayMessage
        											("You are past adding phase.All Players Added/Removed");
        	}
        	
        	break;
        	
        case POPULATE_COUNTRY:
        	
        	if(boolMapLoaded) {
        		
        		if(!playerService.getPlayerList().isEmpty()) {
        			
        			boolAllGamePlayersAdded=true;
                	populateCountries();               	
        		}
        		
        		else {
        			
        			phaseView.displayMessage("No Player Added. Add 1 player atleast");
        		}
        	}
        	
        	else {
        		
        		phaseView.displayMessage("Load a Map first");
        		
        		if(playerService.getPlayerList().isEmpty()) 
        			phaseView.displayMessage("No Player Added. Add 1 player atleast");

        	}
        	
        	break;
        	
        case PLACE_ARMY:
        	
        	//NEED TO CHECK IF MAP LOADED AND COUNTRIES POPULATED FIRST
        	//NOT YET DONE
        	
        	String[] strArr=StringUtils.split(command, WHITESPACE);
        	
        	if(strArr.length!=2) phaseView.displayMessage("Invalid Placearmy command");
        	else {
        		placeArmy(strArr[1]);
        	}
        	
        	break;
        	
        case PLACE_ALL:
        	
        	//NEED TO CHECK IF MAP LOADED AND COUNTRIES POPULATED FIRST
        	//NOT YET DONE
        	
        	placeAll();
        	break;
        	
        case SHOW_PLAYER:
        	
        	//NEED TO CHECK IF PLAYER ADDED FIRST
        	//NEED TO CHECK IF COUNTRY POPULATED AS WELL
        	//NOT YET DONE
        	
        	MapDisplayUtils.showPlayer(mapService,playerService,phaseView);
        	break;
        	
        case SHOW_ALL_PLAYERS:
        	//NEED TO CHECK IF ALL PLAYERS ADDED FIRST (bool allGamePlayersAdded)
        	//NOT YET DONE
        	MapDisplayUtils.showAllPlayers(mapService,playerService,phaseView);
        	break;
        	
        case SHOW_PLAYER_ALL_COUNTRIES:
        	//NEED TO CHECK IF COUNTRIES POPULATED FIRST
        	//NOT YET DONE
        	MapDisplayUtils.showPlayerAllCountries(mapService,playerService,phaseView);
        	break;
        
        case SHOW_PLAYER_COUNTRIES:
        	//NEED TO CHECK IF COUNTRIES POPULATED FIRST
        	//NOT YET DONE
        	MapDisplayUtils.showPlayerCountries(mapService,playerService,phaseView);
        	break;
        	
        case SHOW_MAP:
        	//If countries populated, shows version with ownership and numArmies
        	if(boolCountriesPopulated) MapDisplayUtils.showMapFullPopulated(mapService, phaseView);
        	
        	//If countries not yet populated, shows version with no ownership and numArmies      	
        	else MapDisplayUtils.showMapFullUnpopulated(mapService, phaseView);
        	
        	break;
        	
        case EXIT:
        	CommonUtils.endGame(phaseView);
        	break;
        	
        default:
            throw new IllegalArgumentException("cannot recognize this command");

        } //End of switch    	
        
    } //End of readcommand function


    
    /**
	 * load map from the map file
	 * @param mapname
	 * @return a string instead of null if map not loaded successfully, that is why optional is used/
	 */
	Optional<String> loadMap(String s) {
		
	
	        String[] commands = StringUtils.split(s, " ");

	        if (commands.length != 2) {
	        
	           phaseView.displayMessage("Command LoadMap is not valid"); 
	            return Optional.empty();
	        }

	        String command = commands[0];
	        String path = commands[1];

	        if (command.toLowerCase(Locale.CANADA).equals("loadmap")) {

	        	this.mapLoaderController.setContinentIdGenerator(0);
	            this.mapLoaderController.setCountryIdGenerator(0);
	        	mapService.emptyMap();
	        	mapLoaderController.readFile(path);
	        
	        	MapDisplayUtils.showMap(mapService);
	        	
	        	//If Map not valid, boolMapLoaded will be false.
	        	//Consequently, user will not be able to proceed without loading a valid map.
	        	if(validateMap()) {
	        		this.boolMapLoaded=true;
	        	}
	        	else {
	        		this.boolMapLoaded=false;
	        		phaseView.displayMessage("Please load a Valid Map before proceeding.");
	        	}
	        	
	            return Optional.of(path);
	        }

	        phaseView.displayMessage("Command LoadMap is not valid"); 
	        return Optional.empty();
		
	}
    
	/**
	 * validate the map
	 * @return true if valid, false if not valid
	 */
	private boolean validateMap() {

        if(mapService.isMapValid()){
        	
        	phaseView.displayMessage("Map is Valid");
            return true;
        }
        else{

        	phaseView.displayMessage("Map is not Valid");
            return false;
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
                throw new PlayerEditException("The gameplayer command " + s + " is not valid.");
        }
    }
    
    
	/**
	 * validate each player info, if player info is valid, add it to the list of players
	 * if not valid, throw an exception
	 * @param array of string commands
	 */
	private void addPlayer(String[] s) {
    	
    	if(playerService.getPlayerList().size()<MAX_NUM_PLAYERS) {
    		
        	try {
        		
        		String playerName=convertFormat(s[1]);
        		
        		boolean nameFound=playerService.checkPlayerExistance(playerName);
        		
        		if(!nameFound) {
        			//players.add(new Player(playerName));
        			//view.displayMessage("Player Added: "+playerName);
        			
        			Player newPlayer=playerService.addPlayer(playerName);
        			
        			newPlayer.addObserver(phaseView);
        			newPlayer.addObserver(dominationView);
        			
        			this.boolGamePlayerAdded=true;
        		}
        		
        		else {
        			phaseView.displayMessage("Player Already Exists. Try different name");
        		}
        		
        	}
        	
        	catch(Exception e) {
        		e.printStackTrace();
        		throw new PlayerEditException("gameplayer command: cannot add/remove it is not valid", e);
        	}
    		
    	}
    	
    	else {
    		phaseView.displayMessage("Limit of "+MAX_NUM_PLAYERS+" players reached.");
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
    		
    		boolean playerRemoved=playerService.removePlayer(playerName);
    		
    		if(!playerRemoved) {
    			phaseView.displayMessage("gameplayer command: cannot remove, player does not exist");
    		}
    		
    	}
    	
    	catch(Exception e) {
    		throw new PlayerEditException("gameplayer command: cannot add/remove it is not valid", e);
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
			
			int numPlayers=playerService.getPlayerList().size();
			phaseView.displayMessage("NumPlayers: "+numPlayers);
			
			//CHECK IF ONLY 1 PLAYER: PLAYER WINS
			if(numPlayers==1) {
				phaseView.displayMessage("PLAYER "+playerService.getPlayerList()
												.get(0).getName()+" WINS");
				CommonUtils.endGame(phaseView);
			}
			
			else if(numPlayers==0) {
				phaseView.displayMessage("No Players Added. Try again");
			}
			
			else if(numPlayers>MAX_NUM_PLAYERS) {
				phaseView.displayMessage("Player limit exceeded. Cannot Proceed");
			}
			
			else {
				
				int numInitialArmies=determineNumInitialArmies(numPlayers);
				assignInitialArmies(numInitialArmies);
				
				this.boolArrayCountriesPlaced=new boolean[playerService.getPlayerList().size()];
				
				//view.displayMessage("Number of Initial Armies:"+numInitialArmies+"\n");
				
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
					
					//ADDING OBSERVERS TO EACH COUNTRY
					currentCountry.addObserver(phaseView);
					currentCountry.addObserver(dominationView);
					
					currentCountry.setPlayer(playerService.getPlayerList().get(currentPlayerIndex));
					
					playerService.getPlayerList().get(currentPlayerIndex).reduceArmy(1);
					
					currentCountry.addSoldiers(1);
					

					
					playerService.getPlayerList().get(currentPlayerIndex)
															.addCountryToPlayerList(currentCountry);
					
					if(playerService.getPlayerList().get(currentPlayerIndex).getArmies()==0) {
						boolArrayCountriesPlaced[currentPlayerIndex]=true;
					}
					
					currentPlayerIndex++;
					
					//reset index to 0 when end of player list reached
					if(currentPlayerIndex==playerService.getPlayerList().size()) currentPlayerIndex=0;
					
				}
				
				/*
				for(Country c:mapService.getCountries()) {
					view.displayMessage(c.getId()+" "+c.getCountryName()+" "+c.getPlayer().getName()
							+" "+c.getSoldiers());
				}
				*/
				
				phaseView.displayMessage("Countries Populated. Start placing your armies now.");
				
				this.boolCountriesPopulated=true;
				
				/*
				for(Player p:players) {
					view.displayMessage("Remaining Armies for "+p.getName()
											+": "+p.getArmies());
				}
				*/
				
				playerService.setCurrentPlayerIndex(0);
			}

		}
		
		else {
			phaseView.displayMessage("Countries already populated");
		}
		
	}  //End of PopulateCountries
	
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
		
		//NEED TO MAKE PLAYER OBSERVABLE AS WELL
		
		for(Player p: playerService.getPlayerList()) {
			p.setArmies(numArmies);
		}
		
	}
	
	/**
	 * place an army on the country by its countryName
	 * by each player until all players have placed all their armies
	 * @param countryName
	 */
	public void placeArmy(String countryName) {
    	
    	boolean countryFound=false;
    	
    	Player currentPlayer=playerService.getCurrentPlayer();
    	int currentPlayerIndex=playerService.getCurrentPlayerIndex();
    	
    	//Check if all armies of a specific player placed.
    	//if yes: switch to next player, else place army
    	if(!boolArrayCountriesPlaced[currentPlayerIndex]) {
    		
    		for(Country c:currentPlayer.getCountryList()) {
    		    
        		if(countryName.equalsIgnoreCase(c.getCountryName())) {
        			countryFound=true;
        			
        			currentPlayer.reduceArmy(1);
        			
        			c.addSoldiers(1);		
        			
        			
        			//phaseView.displayMessage(currentPlayer.getName()+" placed army successfully.");
        			if(currentPlayer.getArmies()==0) {
        				boolArrayCountriesPlaced[currentPlayerIndex]=true;        				
        			}
        			break;
        		}
        	}
        	
        	if(!countryFound) {
        		phaseView.displayMessage("Wrong Country Name!!");
        	}
        	
        	else { //country successfully placed - switch to next player
        		
        		if(currentPlayer.getArmies()==0) {
        			boolArrayCountriesPlaced[currentPlayerIndex]=true;
        			phaseView.displayMessage("All armies placed for "+currentPlayer.getName());
        		}
        		
        		//IF ALL Players have numArmies 0: Startup Phase Over, Switch To Next Phase 
        		boolean boolAllCountriesPlaced=true;
        		
        		for(boolean b: boolArrayCountriesPlaced) {
        			
        			if(!b) {
        				boolAllCountriesPlaced=false;
        				break;
        			}    			
        		}
        		
        		if(boolAllCountriesPlaced) {
        			phaseView.displayMessage("All Armies Placed for all players.\n.");
        			this.mapService.setState(GameState.REINFORCE);
        			
        			//playerService.switchNextPlayer();
        			playerService.setCurrentPlayerIndex(0);
        		}
        		
        		else {
        			//Switch to Next Player
            		playerService.switchNextPlayer();
        		}
        	}
    		
    	}

    	else {
    		phaseView.displayMessage(currentPlayer.getName()+" has already placed all its armies.");
    		playerService.switchNextPlayer();
    	}
	
    }
	
	
	
	/**
	 * automatically randomly place all remaining unplaced armies for all players
	 */
	public void placeAll() {
    	
    	for(Player p:playerService.getPlayerList()) {
    		
    		while(p.getArmies()>0) {
    			
    			int randomIndex=ThreadLocalRandom.current().nextInt(0,p.getCountryList().size());
    			
    			p.countryPlayerList.get(randomIndex).addSoldiers(1);
    			p.reduceArmy(1);
    			
    		}
    		
    	}
    	//notify observers??
    	phaseView.displayMessage("All Players Placed.");
    	
    	//showAllPlayers();
    	
    	//this.boolStartUpPhaseOver.set(true);
    	this.mapService.setState(GameState.REINFORCE);
    	
    	playerService.setCurrentPlayerIndex(0);
    	//view.displayMessage("Player Turn: "+players.get(0).getName());
    }
	
	/**
	 * delete white spaces and make the string to lower cases
	 * @param name
	 * @return
	 */
	private String convertFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase(Locale.CANADA);
    }
	
}   //END OF CLASS
