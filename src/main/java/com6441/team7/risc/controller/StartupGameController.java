package com6441.team7.risc.controller;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;
import static com6441.team7.risc.api.RiscConstants.MAX_NUM_PLAYERS;

import com6441.team7.risc.api.model.*;
import com6441.team7.risc.api.model.StartupStateEntity;
import com6441.team7.risc.utils.SaveGameUtils;
import com6441.team7.risc.utils.builder.AbstractStartUpStateBuilder;
import com6441.team7.risc.utils.builder.ConcreteStartUpStateBuilder;

import org.apache.commons.lang3.StringUtils;

import com6441.team7.risc.api.wrapperview.PlayerInitialArmyWrapper;
import com6441.team7.risc.api.wrapperview.PlayerInitialCountryAssignmentWrapper;
import com6441.team7.risc.api.wrapperview.PlayerPlaceArmyWrapper;
import com6441.team7.risc.utils.CommonUtils;
import com6441.team7.risc.utils.MapDisplayUtils;
import com6441.team7.risc.view.GameView;
import com6441.team7.risc.api.exception.PlayerEditException;

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
public class StartupGameController implements Controller{

	/**
	 * a builder for build startupStateEntity
	 */
	private final AbstractStartUpStateBuilder builder;
	
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
	 * check if all countries have been successfully populated
	 */
	private boolean boolAllCountriesPlaced;

	/**
	 * private Controller mapLoaderAdapter;
	 */
	private MapLoaderController mapLoaderController; //Casted it here instead of casting everytime

	/**
	 * a reference of mapService
	 */
	private MapService mapService;

	/**
	 * a reference of playerService
	 */
	private PlayerService playerService;

	/**
	 * a reference of phaseView
	 */
	private GameView phaseView;
	
	/**
	 * reference to tournament controller
	 */
	private TournamentController tournamentController;



	/**
	 * constructor to set mapController and playerService
	 * @param mapController MapController
	 * @param playerService PlayerService
	 */
	public StartupGameController(Controller mapController, PlayerService playerService) {
		this.mapLoaderController = (MapLoaderController) mapController;

		this.playerService = playerService;
		this.mapService = playerService.getMapService();
		
		this.boolMapLoaded=false;	
		this.boolAllGamePlayersAdded=false;
		this.boolGamePlayerAdded=false;
		this.boolCountriesPopulated=false;
		this.builder = new ConcreteStartUpStateBuilder();

	}


	/**
	 * set the view
	 * @param view GameView
	 */
	public void setView(GameView view){
	    this.phaseView = view;
    }




	/**
	 * extends method from IController to read command from the view
	 * check the validity of the commands,
	 * if the command is valid, call relative methods
	 * if not, display error messages to the phase view
	 * @param command Command
	 * @throws Exception on invalid
	 */
	@Override
    public void readCommand(String command) throws Exception {
    	
    	RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);

        String[] commands = {};

        if(command.toLowerCase(Locale.CANADA).contains("-add") ||
        	command.toLowerCase(Locale.CANADA).contains("-remove")){

            command = StringUtils.substringAfter(command, "-");
            commands = command.split("\\s-");
        }
        
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
        	
        	//Reconcatenate broken countryname split by '-' command:
        	
        	if(!boolCountriesPopulated) {
        		phaseView.displayMessage("Populate countries first");
        		return;
        	}
        	
        	String[] strArr=StringUtils.split(command, WHITESPACE);
        	
        	if(strArr.length!=2) phaseView.displayMessage("Invalid Placearmy command");
        	else {
        		placeArmy(strArr[1]);
        	}
        	
        	break;
        	
        case PLACE_ALL:
        	
        	if(!boolCountriesPopulated) {
        		phaseView.displayMessage("Populate countries first");
        		return;
        	}
        	
        	placeAll();
        	break;
        	
        case TOURNAMENT:
        	tournamentController=new TournamentController(command,this);
        	break;
        	
        case SHOW_PLAYER:
        	
        	MapDisplayUtils.showPlayer(mapService,playerService,phaseView);
        	break;
        	
        case SHOW_ALL_PLAYERS:

        	MapDisplayUtils.showAllPlayers(mapService,playerService,phaseView);
        	break;
        	
        case SHOW_PLAYER_ALL_COUNTRIES:

        	MapDisplayUtils.showPlayerAllCountries(mapService,playerService,phaseView);
        	break;
        
        case SHOW_PLAYER_COUNTRIES:

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
        	
        case TEST_TOURNAMENT:
        	tournamentController=new TournamentController(command, this,true);
        	break;

		case SAVEGAME:
			saveGame();
			break;

        default:
            throw new IllegalArgumentException("cannot recognize this command");

        }
        
    }


    
    /**
	 * load map from the map file
	 * @param s mapname
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
        	
            return true;
        }
        else{

            return false;
        }
    }
    
	/**
	 * for each string, call editPlayerFromUserInput method
	 * @param command string array of editPlayer command. 
	 */
	private void editPlayer(String[] command) {
		
			 Arrays.stream(command).forEach(this::editPlayerFromUserInput);				
	
	}
    
	/**
	 * if the command is add, call addPlayer()
	 * if the command is remove, call removePlayer()
	 * else throw exception
	 * @param command command string
	 */
    private void editPlayerFromUserInput(String command) {
        String[] commands = StringUtils.split(command, " ");
        switch (convertFormat(commands[0])) {
            case "add":
                addPlayer(commands);
                break;
            case "remove":
                removePlayer(commands);
                break;
            default:
                throw new PlayerEditException("The gameplayer command " + command + " is not valid.");
        }
    }
    
    
	/**
	 * validate each player info, if player info is valid, add it to the list of players
	 * if not valid, throw an exception
	 * @param command array of string commands
	 */
	private void addPlayer(String[] command) {
    	
    	if(playerService.getPlayerList().size()<MAX_NUM_PLAYERS) {
    		
        	try {
        		
        		if(command.length!=3) {
        			phaseView.displayMessage("Invalid Add Command. Try again");
        			return;
        		}
        		
        		String playerName=convertFormat(command[1]);
        		String playerType=convertFormat(command[2]);
        		
        		boolean nameFound=playerService.checkPlayerExistance(playerName);
        		
        		if(!nameFound) {
        			
        			Player newPlayer=playerService.addPlayer(playerName, playerType);       			
        			
        			this.boolGamePlayerAdded=true;
        		}
        		
        		else {
        			phaseView.displayMessage("Player Already Exists. Try different name.");
        		}
        		
        	}
        	
        	catch(Exception e) {
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
	 * @param command array of string commands
	 */
	private void removePlayer(String[] command) {
    	
    	try {
    		
    		String playerName=convertFormat(command[1]);
    		
    		boolean playerRemoved=playerService.removePlayer(playerName);
    		
    		//If playerList is empty after player removed, cannot proceed to populatecountries
    		//therefore boolean gamePlayerAdded must be reset to false
    		if(playerRemoved) {
    			
    			if(playerService.getPlayerList().isEmpty())
    				this.boolGamePlayerAdded=false;
    		}
    		
    		else phaseView.displayMessage("Cannot remove, player does not exist.");
    		
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
	 * Triggers Notification to PlayerService Observers when every country is assigned to player.
	 * Also triggers world domination view update when 
	 */
	public void populateCountries() {
		
				//If countries already populated, do not proceed again/
				if(boolCountriesPopulated) {
					phaseView.displayMessage("Countries already populated");
					return;
				}
							
				//If number of players invalid (0 or greater than player limit or if only 1 player present)
				if(!checkNumPlayersValid()) return;
				
				
				int numPlayers=playerService.getPlayerList().size();
			
				int numInitialArmies=determineNumInitialArmies(numPlayers);
				assignInitialArmies(numInitialArmies);
				
				//Keeps track of which players have placed all their armies
				this.boolArrayCountriesPlaced=new boolean[playerService.getPlayerList().size()];
				
				Stack<Country> stackCountry=new Stack<>();
				
				Iterator setIter=mapService.getCountries().iterator();
				
				while(setIter.hasNext()) {
					stackCountry.push((Country) setIter.next());
				}
				
				//shuffle countries in stack to make them random
				Collections.shuffle(stackCountry);
				Collections.shuffle(stackCountry);
				Collections.shuffle(stackCountry);
				
				int currentPlayerIndex=0;
				
				//allocate randomly shuffled countries in stack to players in round-robin fashion.
				
				while(!stackCountry.isEmpty()) {
					
					Player currentPlayer=playerService.getPlayerList().get(currentPlayerIndex);
					
					Country currentCountry=stackCountry.pop();
					
					currentCountry.setPlayer(currentPlayer);  //allocation of country to player
					
					currentPlayer.reduceArmy(1);   //reduce numArmies remaining to be placed
					
					currentCountry.addSoldiers(1);  //add army to country
								
					currentPlayer.addCountryToPlayerList(currentCountry);
					
					if(currentPlayer.getArmies()==0) {
						boolArrayCountriesPlaced[currentPlayerIndex]=true;
					}
					
					
					//Trigger to NOTIFY playerService Observers after assigning country to player
					PlayerInitialCountryAssignmentWrapper playerInitialCountryAssignment
					=new PlayerInitialCountryAssignmentWrapper(currentPlayer,currentCountry);
					
					
					playerService.notifyPlayerServiceObservers(playerInitialCountryAssignment);
					
					
					currentPlayerIndex++;
					
					//reset index to 0 when end of player list reached
					if(currentPlayerIndex==playerService.getPlayerList().size())
						currentPlayerIndex=0;					
				}
				
				phaseView.displayMessage("Countries Populated. Start placing your armies now.\n");
				
				//Trigger to Notify PlayerService Observers after assigning all countries
				playerService.evaluateWorldDomination();
				
				this.boolCountriesPopulated=true;
				
				playerService.setCurrentPlayerIndex(0);
		
	}
	
	/**
	 * Method that checks if number of players is valid before populating countries
	 * Checks the following conditions:
	 * <ul>
	 * <li>If only 1 player present, player automatically wins</li>
	 * <li>If no player present, cannot proceed to country population</li>
	 * <li>If more than max num players present, cannot proceed to country population</li>
	 * </ul>
	 * @return true if number of players valid, else false.
	 */
	private boolean checkNumPlayersValid() {
		int numPlayers=playerService.getPlayerList().size();
		phaseView.displayMessage("NumPlayers: "+numPlayers);
		

		if(numPlayers==1) {
			phaseView.displayMessage("PLAYER "+playerService.getPlayerList()
											.get(0).getName()+" WINS");
			CommonUtils.endGame(phaseView);
			return false;
		}
		
		else if(numPlayers==0) {
			phaseView.displayMessage("No Players Added. Try again");
			return false;
		}
		
		else if(numPlayers>MAX_NUM_PLAYERS) {
			phaseView.displayMessage("Player limit exceeded. Cannot Proceed");
			return false;
		}
		
		return true;
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
	 * @param numPlayers number of Players
	 * @return number of intial armies allocated to each player
	 */
	public int determineNumInitialArmies(int numPlayers) {
		return 40-((numPlayers-2)*5);
	}
	
	/**
	 * Set the number of initial armies for every player respectively.
	 * Triggers notification to playerservice observers using PlayerInitialArmyWrapper
	 * @param numArmies number of initial armies allocated
	 */
	private void assignInitialArmies(int numArmies) {

		
		for(Player p: playerService.getPlayerList()) {
			p.setArmies(numArmies);
			
			PlayerInitialArmyWrapper playerInitialArmyWrapper=new PlayerInitialArmyWrapper(p);
			playerService.notifyPlayerServiceObservers(playerInitialArmyWrapper);
			
		}
		
	}
	
	/**
	 * place an army on the country by its countryName
	 * by each player until all players have placed all their armies
	 * Triggers notification to playerservice observers using PlaceArmyWrapper
	 * Triggers notification to domination view
	 * @param countryName CountryName
	 */
	public void placeArmy(String countryName) {
    	
    	boolean countryFound=false;
    	
    	Player currentPlayer=playerService.getCurrentPlayer();
    	int currentPlayerIndex=playerService.getCurrentPlayerIndex();
    	
    
    	
    	//Check if all armies of a specific player placed.
    	//if yes: switch to next player, else place army
    	if(!boolArrayCountriesPlaced[currentPlayerIndex]) {
    		
    		for(Country c:currentPlayer.getCountryPlayerList()) {
    		    
        		if(countryName.equalsIgnoreCase(c.getCountryName())) {
        			countryFound=true;
        			
        			currentPlayer.reduceArmy(1);
        			
        			c.addSoldiers(1);		
        			
        			//Notify observers
        			PlayerPlaceArmyWrapper playerPlaceArmyWrapper
        			=new PlayerPlaceArmyWrapper(currentPlayer,c);

        			playerService.notifyPlayerServiceObservers(playerPlaceArmyWrapper);
        			
        			playerService.evaluateWorldDomination();
        			
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
        		 boolAllCountriesPlaced=true;
        		
        		for(boolean b: boolArrayCountriesPlaced) {
        			
        			if(!b) {
        				boolAllCountriesPlaced=false;
        				break;
        			}    			
        		}
        		
        		if(boolAllCountriesPlaced) {
        			phaseView.displayMessage("All Armies Placed for all players.\n.");
        			
        			playerService.setCurrentPlayerIndex(0);
        			
        			this.mapService.setState(GameState.REINFORCE);
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
	 * Triggers notification to playerService observers using PlaceArmyWrapper
	 * Triggers notification to domination view after all armies have been placed
	 */
	public void placeAll() {
    	
		//For every player in list
    	for(Player p:playerService.getPlayerList()) {

    		//while armies are still remaining
    		while(p.getArmies()>0) {
    			
    			//random placement + decrement random range size TO AVOID COLLISIONS
    			
    			int randomIndex=ThreadLocalRandom.current().nextInt(0,p.getCountryPlayerList().size());
    			
    			p.getCountryPlayerList().get(randomIndex).addSoldiers(1);
    			p.reduceArmy(1);
    			
    			//Notify Observers - Same as placeArmy
    			
    			PlayerPlaceArmyWrapper playerPlaceArmyWrapper
    			=new PlayerPlaceArmyWrapper(p,p.getCountryPlayerList().get(randomIndex));
    			
    			playerService.notifyPlayerServiceObservers(playerPlaceArmyWrapper);
    			
    			//playerService.evaluateWorldDomination();  //Commented out else too much updates
    		}
    		
			playerService.evaluateWorldDomination();
    		
    	}

    	phaseView.displayMessage("All Players Placed.");

    	//Observers notified in state-setting method in playerservice
    	playerService.setCurrentPlayerIndex(0);
    	
    	//Observers notified in state-setting method in mapservice
    	this.mapService.setState(GameState.REINFORCE);

    	Player player = playerService.getCurrentPlayer();
    	
    	playerService.showCardsInfo(player);
    	
    	playerService.automateGame();

    }
	
	/**
	 * delete white spaces and make the string to lower cases
	 * @param name string to be formatted
	 * @return returns formatted string
	 */
	private String convertFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase(Locale.CANADA);
    }
	
	/**
	 * Getter method for PlayerService
	 * @return PlayerService reference
	 */
	public PlayerService getPlayerService() {
		return playerService;
	}
	

	/**
	 * Create setter for boolMapLoadeded
	 * @param bool boolean
	 */
	public void setBoolMapLoaded(boolean bool) {
		this.boolMapLoaded = bool;
	}
	
	
	/**
	 * Create setter for boolCountriesPopulated
	 * @param bool boolean value
	 */
	public void setBoolCountriesPopulated(boolean bool) {
		this.boolCountriesPopulated = bool;
	}

	/**
	 * Setter for boolean values from StartupStateEntity
	 * @param startupStateEntity object from which details are pulled
	 */
	public void setStatus(StartupStateEntity startupStateEntity){
		this.boolCountriesPopulated = startupStateEntity.isBoolCountriesPopulated();
		this.boolMapLoaded = startupStateEntity.isBoolMapLoaded();
		this.boolAllGamePlayersAdded = startupStateEntity.isBoolAllGamePlayersAdded();
		this.boolGamePlayerAdded = startupStateEntity.isBoolGamePlayerAdded();
		this.boolAllCountriesPlaced = startupStateEntity.isBoolAllCountriesPlaced();
		this.boolArrayCountriesPlaced = startupStateEntity.boolArrayCountriesPlaced();
	}


	/**
	 * save game, set the command of current game phase
	 */
	public void saveGame(){


		if(!boolCountriesPopulated){
			phaseView.displayMessage("sorry we only accept save map after populating countries");
			return;
		}


		if(!boolAllCountriesPlaced){
			playerService.setCommand(RiscCommand.PLACE_ALL.getName() + RiscCommand.PLACE_ARMY.getName());
		}

		phaseView.displayMessage("game has successfully saved!");
		save(mapService, playerService);

	}

	/**
	 * save the game to json file
	 * @param mapService to store map information
	 * @param playerService to store player information
	 */
	private void save(MapService mapService, PlayerService playerService){

		 MapStatusEntity mapStatusEntity = mapService.getMapStatusEntity();
		 PlayerStatusEntity playerStatusEntity = playerService.getPlayerStatusEntity();

		 StartupStateEntity startupStateEntity =  getStartUpStateEntity();


		 Map<String, Object> entities = new HashMap<>();
		 entities.put(MapStatusEntity.class.getSimpleName(), mapStatusEntity);
		 entities.put(PlayerStatusEntity.class.getSimpleName(), playerStatusEntity);
		 entities.put(StartupStateEntity.class.getSimpleName(), startupStateEntity);
		 SaveGameUtils.saveGame(entities);

	}


	/**
	 * get boolean value isBoolAllCountriesPlaced
	 * @return isBoolAllCountriesPlaced
	 */
	public boolean isBoolAllCountriesPlaced() {
		return boolAllCountriesPlaced;
	}

	/**
	 * set boolean value setBoolAllCountriesPlaced
	 * @param boolAllCountriesPlaced if all countries been placed
	 */
	public void setBoolAllCountriesPlaced(boolean boolAllCountriesPlaced) {
		this.boolAllCountriesPlaced = boolAllCountriesPlaced;
	}


	/**
	 * set array of boolean values of boolArrayCountriesPlaced
	 * @param arrB the array of boolean values to store if the country has been placed army
	 */
	public void setBoolArrayCountriesPlaced(boolean[] arrB) {
		this.boolArrayCountriesPlaced=arrB;
	}
	
	/**
	 * Getter method for phaseview
	 * Used by Tournament Controller only when it is instantiated
	 * @return phaseview of startup controller
	 */
	public GameView getPhaseViewSGC() {
		return phaseView;
	}
	
	/**
	 * Getter Method to get Tournament Controller
	 * @return tournamentController reference
	 */
	public TournamentController getTournamentController() {
		return this.tournamentController;
	}
	
	/**
	 * Create a new startup state entity with new boolean values
	 */
	public void constructStartUpStateEntity(){
		builder.createNewStartUpStateEntity();
		builder.buildBoolMapLoaded(boolMapLoaded);
		builder.buildBoolGamePlayerAdded(boolGamePlayerAdded);
		builder.buildBoolAllGamePlayersAdded(boolAllGamePlayersAdded);
		builder.buildBoolCountriesPopulated(boolCountriesPopulated);
		builder.buildBoolArrayCountriesPlaced(boolArrayCountriesPlaced);
		builder.buildBoolAllCountriesPlaced(boolAllCountriesPlaced);
	}

	/**
	 * build the startupStateEntity
	 * @return startUpStateEntity
	 */
	public StartupStateEntity getStartUpStateEntity(){
		constructStartUpStateEntity();
		return builder.getStartupStateEntity();
	}

	/**
	 * get the builder
	 * @return builder
	 */
	public AbstractStartUpStateBuilder getBuilder() {
		return builder;
	}
	
}
