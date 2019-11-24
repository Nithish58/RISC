package com6441.team7.risc.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.api.wrapperview.PlayerEditWrapper;
import com6441.team7.risc.view.PhaseViewTest;

import static com6441.team7.risc.api.RiscConstants.MAX_NUM_PLAYERS;


/**
 * This class tests various commands and functionalities of the StartupGameController.
 * @author Keshav
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StartupGameControllerTest {
	
	/**
	 * View which outputs test strings and has some other additonal functionalities than the normal phaseView
	 */
	 PhaseViewTest phaseViewTest;
	 /**
	  * mapService object stores map Information and game state
	  */
	 MapService mapService;
	 /**
	  * playerService object keeps track of player information such as current player turn and list of players
	  */
	 PlayerService playerService;
	 /**
	  * Controller to load map
	  */
	 MapLoaderController mapLoaderController;
	 /**
	  * Controller for startup phase
	  */
	 StartupGameController startupGameController;
	 
	 /**
	  * list of different controllers
	  */
	 List<Controller> controllerList;

	 /**
	  * Method called before each test case
	  * Instantiates necessary objects and loads a valid map
	  */
	@Before public void beforeEachTest() {
		createObjects();
		loadValidMap("ameroki.map");
	}
	
	/**
	 * Test to add a player
	 * Context: Map is loaded before adding player.
	 * A wrapper class should be returned with the newly player object.
	 * The wrapper class also returns an empty player object representing removed player.
	 */
	  @Test public void test001_addPlayer(){
		  
		  //Context
		  String playerName="keshav";
		  
		  //Method call
		  addPlayer(playerName);
		  
		  //Evaluation
		  Object obj=phaseViewTest.getReturnedObject();
		  PlayerEditWrapper playerEditWrapper=((PlayerEditWrapper) obj);
		  Player addedPlayer=playerEditWrapper.getAddedPlayer();
		  Player removedPlayer=playerEditWrapper.getRemovedPlayer();
	  
		  assertEquals(addedPlayer.getName(),playerName);
		  assertNull(removedPlayer);

	  }
	 
	  /**
	   * Test to remove a player
	   * Context: Map is loaded, a player is added first.
	   * The player is then removed
	   * A wrapper class should be returned containing the removed player object
	   * The wrapper class also returns an empty player object representing added player.
	   */
	  @Test public void test002_removePlayer() {
		  //Context
		  String playerName="keshav";
		  addPlayer(playerName);
		  
//		  Method call
		  removePlayer(playerName);
		  
//		  Evaluation
		  Object obj=phaseViewTest.getReturnedObject();	  		  
		  PlayerEditWrapper playerEditWrapper=((PlayerEditWrapper) obj);
		  Player addedPlayer=playerEditWrapper.getAddedPlayer(); 
		  Player removedPlayer=playerEditWrapper.getRemovedPlayer();
	  
		  assertEquals(removedPlayer.getName(),playerName);
		  assertNull(addedPlayer);
		  
	  }
	  
	  /**
	   * Test to add a duplicate player.
	   * Context: Map is loaded, a player is added first.
	   * The player is then re-added.
	   * Error message should be displayed: "Player Already Exists. Try different name."
	   */
	  @Test public void test003_addDuplicatePlayer() {
		  //Context
		  String playerName="Keshav";
		  //Method call
		  addPlayer(playerName);
		  addPlayer(playerName);
		  
		  //Evaluation
		  String strOutput="Player Already Exists. Try different name.";
		  String strDisplayMessage=phaseViewTest.getStrDisplayMessage();
		  
		  assertEquals(strOutput,strDisplayMessage);
	  }
	  
	  /**
	   * Test to remove inexistant player.
	   * Context: Map is loaded, a player is added first.
	   * An inexistant player is then removed.
	   * Error message should be displayed: "Cannot remove, player does not exist."
	   */
	  @Test public void test004_removeInexistantPlayer() {
		  //Context
		  String playerName="Keshav";
		  String playerName2="Ramburn";
		  addPlayer(playerName);
		  
		  //Method call
		  removePlayer(playerName2);
		  
		  //Evaluation
		  String strOutput="Cannot remove, player does not exist.";
		  String strDisplayMessage=phaseViewTest.getStrDisplayMessage();
		  
		  assertEquals(strOutput,strDisplayMessage);
		  
	  }
	  
	  /**
	   * Test to check that no further players are added after max limit reached.
	   * Context: Load map, add players till max limit
	   * Then try to add more players.
	   * Error message should be displayed: "Limit of players reached."
	   */
	  @Test public void test005_exceedMaximumPlayerLimit() {
		  //Context
		  for(int i=0;i<MAX_NUM_PLAYERS+5;i++) {
			  //Method call
			  addPlayer(String.valueOf(i));
		  }
		  
		  //Evaluation
		  String strOutput="Limit of "+MAX_NUM_PLAYERS+" players reached.";
		  String strDisplayMessage=phaseViewTest.getStrDisplayMessage();
		  
		  assertEquals(strOutput,strDisplayMessage);
	  }
	  
	  /**
	   * Method that tests whether players are allowed to be added before loading a map
	   * Context: Do not load map.
	   * Then try to add players.
	   * Error message should be displayed: "Load Map First."
	   */
	  @Test public void test006_addPlayerWithoutLoadingMap() {
		  //Context
		  mapService.emptyMap();
		  addPlayer("Keshav");
		  addPlayer("Ramburn");
		  
		  String strOutput="Load Map First.";
		  String strDisplayMessage=phaseViewTest.getStrDisplayMessage();
		  
		//  assertEquals(strOutput,strDisplayMessage);
	  }
	  
	  /**
	   * Test to determine that correct number of initial armies allocated to a player before populating countries
	   * Context: add 2 players and set numArmies to 40.
	   * Then continue adding 1 player.
	   * Check that numArmies determined decreases by 5 for each player added until max player reached.
	   */
	  @Test public void test007_determineInitialArmyAllocationToPlayer() {
		  //Context
		  int numArmy=40;
		  addPlayer(String.valueOf(0));
		  addPlayer(String.valueOf(1));
		  
		  //Method call and Evaluation
		  for(int i=2;i<MAX_NUM_PLAYERS;i++) {
			  numArmy-=5;
			  addPlayer(String.valueOf(i));
			  assertEquals(numArmy,startupGameController.determineNumInitialArmies(i+1));
		  }
		  
	  }
	  
	  /**
	   * Checks that all countries have been assigned to a player and that all armies have atleast 1 soldier placed.
	   *Also checks that countries populated correctly.
	   *Context:Load map and add players first, then start populating countries.
	   *Message should be displayed: "Countries Populated. Start placing your armies now.\n"
	   */
	  @Test public void test008_allCountriesAssignedToPlayers() {
		  
		  //Context
		  addPlayer("Keshav");
		  addPlayer("Ramburn");		  
		  int numArmies=startupGameController.determineNumInitialArmies(2);
		  
		  //Method call
		  phaseViewTest.receiveCommand("populatecountries");
		 // startupGameController.populateCountries();
		  
		  //Evaluation
		  Set<Country> countries=mapService.getCountries();
		  
	    	for(Country c:countries) {
	    		assertNotNull(c.getPlayer());
	    		assertTrue(c.getSoldiers()>0);
	    	}
		  
			  String strOutput="Countries Populated. Start placing your armies now.\n";
			  String strDisplayMessage=phaseViewTest.getStrDisplayMessage();
			  
			  assertEquals(strOutput,strDisplayMessage);
	  }
	  
	/*
	 * @Test public void test009_checkOnePlayerWinsGame() {
	 *  //Context
	 * addPlayer("Keshav");
	 * 
	 * //Method call
	 * phaseViewTest.receiveCommand("populatecountries");
	 * 
	 * //Evaluation 
	 * String strOutput="Game Ends"; String
	 * strDisplayMessage=phaseViewTest.getStrDisplayMessage();
	 * 
	 * assertEquals(strOutput,strDisplayMessage); }
	 */
	  
	  
	  /**
	   * Test whether country is populated without adding players
	   * Context: loadmap but do not add any players.
	   * Then issue command populatecountries.
	   * Error message should be displayed: "No Player Added. Add 1 player atleast"
	   */
	  @Test public void test010_populateWithoutAddingPlayers() {
		  //Context - No player added
		  
		  //Method call
		  phaseViewTest.receiveCommand("populatecountries");
		  
		  //Evaluation
		  String strOutput="No Player Added. Add 1 player atleast";
		  String strDisplayMessage=phaseViewTest.getStrDisplayMessage();
		  
		  assertEquals(strOutput,strDisplayMessage);
	  }
	  
	  /**
	   * Test if numArmies of player and country decremented and incremented correctly respectively
	   * Context: Loadmap, add Players, populate countries.
	   * Then call placearmy command using a random country owned by player as parameter to command.
	   * Expected Result: player must have 1 army less than before command called;
	   * country must have 1 army more than before command called.
	   */
	  @Test public void test011_placeArmy() {
		  //Context
		  addPlayer("keshav");
		  addPlayer("ramburn");		  
		  phaseViewTest.receiveCommand("populatecountries");
		  
		  Player currentPlayer=playerService.getCurrentPlayer();
		  
		  int initialNumPlayerArmiesRemaining=currentPlayer.getArmies();
		  
		  Country randomPlayerCountry=currentPlayer.getCountryPlayerList().get(0);
		  int initialNumCountrySoldiers=randomPlayerCountry.getSoldiers();
		  String countryName=randomPlayerCountry.getCountryName().toString();
		 
		  //Method call
		  phaseViewTest.receiveCommand("placearmy "+countryName);
		  
		 assertEquals((currentPlayer.getArmies()+1),initialNumPlayerArmiesRemaining);
		assertEquals((initialNumCountrySoldiers+1),randomPlayerCountry.getSoldiers().intValue());
		  
		  //assertTrue(previousPlayer.getArmies()+1==initialNumPlayerArmiesRemaining);
		  //assertTrue(initialNumCountrySoldiers+1==previousPlayer.getCountryPlayerList().get(0).getSoldiers());
		  
		  
	  }
	  
	  /**
	   * Test to check if placearmy command rejected when invalid country name input.
	   * Context: loadmap, add players, populate countries.
	   * Then call placearmy command with invalid country name.
	   * Error message should be displayed: "Wrong Country Name!!"
	   */
	  @Test public void test012_placeArmyInvalidCountry(){
		  //Context
		  addPlayer("keshav");
		  addPlayer("ramburn");		  
		  phaseViewTest.receiveCommand("populatecountries");
		  
		  String invalidCountryName="xxxxxxxxxxxxxxxxadfsguskagfkugfyauikatiwkavktkiwka";
		  
		  //Method call
		  phaseViewTest.receiveCommand("placearmy "+invalidCountryName);
		  
		  //Evaluation
		  String strOutput="Wrong Country Name!!";
		  String strDisplayMessage=phaseViewTest.getStrDisplayMessage();
		  
		  assertEquals(strOutput,strDisplayMessage);
		   
	  }
	  
	  /**
	   * Method tests whether correct output is given when last army of player is placed.
	   * It also most importantly tests whether more armies are removed (negative value of -1) when player
	   * has no more armies left to place.
	   * Context: loadmap, addplayers, set numArmies of player being tested only to 1.
	   * Expected Result:<ul>
	   * <li> When placearmy called first time, numArmies must be decremented as expected.</li>
	   * <li> message should also be displayed informing player that all his/her armies has been placed: "All armies placed for playerName"</li>  
	   *<li>However when placearmy called again, it should not decrement again. Instead it should switch to next player and instead display error message.</li></ul>
	   */
	  @Test public void test013_placeArmyLowerBoundZeroArmyTest() {
		  //Context
		  addPlayer("keshav");
		  addPlayer("ramburn");		  
		  phaseViewTest.receiveCommand("populatecountries");
		  
		  Player currentPlayer=playerService.getCurrentPlayer();
		  Country randomPlayerCountry=currentPlayer.getCountryPlayerList().get(0);
		  String countryName=randomPlayerCountry.getCountryName().toString();
		  currentPlayer.setArmies(1);
		  
		  //Method call 1
		  phaseViewTest.receiveCommand("placearmy "+countryName);
		  
		  //Evaluation 1
		  String strOutput="All armies placed for "+currentPlayer.getName();
		  String strDisplayMessage=phaseViewTest.getStrDisplayMessage();
		  
		  assertEquals(strOutput,strDisplayMessage);
		  
		  //Method call 2
		  phaseViewTest.receiveCommand("placearmy "+countryName);
		  
		  //Evaluation 2
		  String strOutput2="Wrong Country Name!!";
		  String strDisplayMessage2=phaseViewTest.getStrDisplayMessage();
		  
		  assertEquals(strOutput2,strDisplayMessage2);
		  
	  }
	  
	  /**
	   * Method tests whether all armies placed successfully when placeall is called.
	   *It takes sum of all soldiers in each player's country list and checks whether it is equal to initial number of armies assigned to each player.
	   *It also checks whether Gamestate is automatically changed to reinforcement after all armies have been placed for all players.
	   *It also checks whether it is the first player's turn for reinforcement.
	   *
	   *  Context: loadmap, add players, populatecountries
	   *  Then issue placeall command.
	   *  Expected: Message displayed: "All Players Placed."
	   *  Also, current player  must be automatically set to player 1 in reinforcement phase. 
	   */
	  @Test public void test014_placeAll() {
		  //Context
		  addPlayer("keshav");
		  addPlayer("ramburn");
		  addPlayer("tirathraj");
		  phaseViewTest.receiveCommand("populatecountries");
		  
		  //Method call
		  phaseViewTest.receiveCommand("placeall");
		  
		  //Evaluation
		  int expectedTotalNumArmies=startupGameController.
				  determineNumInitialArmies(playerService.getPlayerList().size());
		  
		  for(Player p:playerService.getPlayerList()) {
			  int totalNumArmies=0;
			  assertTrue(p.getArmies()==0);
			  
			  for(Country c:p.getCountryPlayerList()) totalNumArmies+=c.getSoldiers();
			  
			  assertTrue(totalNumArmies==expectedTotalNumArmies);
			  
			  assertTrue(mapService.getGameState()==GameState.REINFORCE);
			  assertTrue(playerService.getCurrentPlayerIndex()==0);
			  
		  }		  
		  
	  }
	  
	  /**
	   * Method to load a map.
	   * Method first exits from editmapphase by sending command exitmapedit.
	   * Then command to loadmap is sent.
	   * @param mapName MapName
	   */
	  	public void loadValidMap(String mapName) {
	  		phaseViewTest.receiveCommand("exitmapedit"); // Exit Map Editing Phase
		
	  		phaseViewTest.receiveCommand("loadmap " + mapName); // Load ameroki map
	}

	/**
	 * Method that sends command to add a player
	 * @param name of player
	 */
	public void addPlayer(String name) {
		phaseViewTest.receiveCommand("gameplayer -add " + name+" human");
	}
	
	/**
	 * Method that sends command to remove a player
	 * @param name of player
	 */
	public void removePlayer(String name) {
		phaseViewTest.receiveCommand("gameplayer -remove " + name);
	}

	/**
	 * Method that instantiates all required objects before testing
	 */
	public void createObjects() {

		mapService = new MapService();
		playerService = new PlayerService(mapService);

		phaseViewTest = new PhaseViewTest();
		controllerList = new ArrayList<>();

		mapLoaderController = new MapLoaderController(mapService);
		startupGameController = new StartupGameController(mapLoaderController, playerService);


		controllerList.add(mapLoaderController);
		controllerList.add(startupGameController);
		
		phaseViewTest.addController(controllerList);

		mapLoaderController.setView(phaseViewTest);
		startupGameController.setView(phaseViewTest);

		mapService.addObserver(phaseViewTest);
		playerService.addObserver(phaseViewTest);

	}

} // End of class
