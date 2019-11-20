//package com6441.team7.risc.controller;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//
//import com6441.team7.risc.api.model.Country;
//import com6441.team7.risc.api.model.GameState;
//import com6441.team7.risc.api.model.MapService;
//import com6441.team7.risc.api.model.Player;
//import com6441.team7.risc.api.model.PlayerService;
//import com6441.team7.risc.api.wrapperview.PlayerFortificationWrapper;
//import com6441.team7.risc.view.PhaseViewTest;
//
//
///**
// * This class tests various commands and functionalities of the FortificationGameController.
// * @author Keshav
// */
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class FortifyGameControllerTest {
//
//	/**
//	 * View which outputs test strings and has some other additonal functionalities than the normal phaseView
//	 */
//	 PhaseViewTest phaseViewTest;
//	 /**
//	  * mapService object stores map Information and game state
//	  */
//	 MapService mapService;
//	 /**
//	  * playerService object keeps track of player information such as current player turn and list of players
//	  */
//	 PlayerService playerService;
//	 /**
//	  * Controller to load map
//	  */
//	 MapLoaderController mapLoaderAdapter;
//	 /**
//	  * Controller for startup phase
//	  */
//	 StartupGameController startupGameController;
//	 /**
//	  * Controller for Reinforcement phase
//	  */
//     ReinforceGameController reinforceGameController ;
//     /**
//      * Controller for Fortification phase
//      */
//     FortifyGameController fortifyGameController ;
//     /**
//      * Controller for Attack phase
//      */
//     AttackGameController attackController ;
//
//	 /**
//	  * list of different controllers
//	  */
//	 List<Controller> controllerList;
//
//	 /**
//	  * Method called before each test case
//	  * Instantiates necessary objects and loads a valid map, adds 3 players, populates countries,
//	  * places armies, then skips reinforcement and attack phase and goes directly to fortification phase.
//	  */
//	@Before public void beforeEachTest() {
//		createObjects();
//
//		loadValidMap("luca.map");
//
//		addPlayer("Keshav");
//		addPlayer("Binsar");
//		addPlayer("Bikas");
//
//		phaseViewTest.receiveCommand("populatecountries");
//		phaseViewTest.receiveCommand("placeall");
//
//		mapService.setState(GameState.FORTIFY);
//	}
//
//	/**
//	 * Test if fortify none works
//	 *Context: Load everything beforehand in beforeTest method; skip reinforcement and attack phase
//	 *Then call command fortify none
//	 *Expected Result: GameState must be changed to reinforce and player turn must be switched to next player.
//	 */
//	@Test public void test001_testFortifyNone() {
//
//		int nextPlayerIndexBeforeMethodCall=playerService.getNextPlayerIndex();
//
//		//Context already set in beforeTest method
//
//		//Method call
//		phaseViewTest.receiveCommand("fortify none");
//
//		//Evaluation
//		int currentPlayerIndex=playerService.getCurrentPlayerIndex();
//
//		assertEquals(currentPlayerIndex,nextPlayerIndexBeforeMethodCall);
//		assertTrue(mapService.getGameState()==GameState.REINFORCE);
//
//	}
//
//	/**
//	 * Method that tests a valid fortify command
//	 * Context: Load everything beforehand in beforeTest method; skip reinforcement and attack phase
//	 * Then call valid fortification command
//	 * Expected Result: numFromCountrySoldiers=initialFromCountrySoldiers-numSoldiersMoved;
//	 * numToCountrySoldiers=initialToCountrySoldiers+numSoldiersMoved;
//	 * GameState changed to reinforcement of next player as well
//	 */
//	@Test public void test002_fortifyValidCommand(){
//
//		//Context
//		Player currentPlayer=playerService.getCurrentPlayer();
//
//		//Get first country in player list
//		Country fromCountry=currentPlayer.getCountryList().get(0);
//		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromCountry.getId());
//
//		//Get first adjacent country in country's list
//		Country toCountry = null;
//		for(Integer i: fromCountryAdjacencyList) {
//			toCountry=mapService.getCountryById(i).get();
//			break;
//		}
//
//		//set toCountry to current player as well
//		toCountry.setPlayer(currentPlayer);
//		int initialToCountrySoldiers=toCountry.getSoldiers().intValue();
//
//		//Set numSoldiers in fromCountry to 5
//		int initialFromCountrySoldiers=5;
//		fromCountry.setSoldiers(initialFromCountrySoldiers);
//		int numSoldiersToMove=3;
//
//		String validFortificationCommand="fortify "+fromCountry.getCountryName()+" "+
//		toCountry.getCountryName()+" "+numSoldiersToMove;
//
//		//Method call
//		phaseViewTest.receiveCommand(validFortificationCommand);
//
//		//Evaluation
//		assertEquals(initialToCountrySoldiers,toCountry.getSoldiers().intValue()-numSoldiersToMove);
//		assertEquals(initialFromCountrySoldiers,fromCountry.getSoldiers().intValue()+numSoldiersToMove);
//		assertTrue(mapService.getGameState()==GameState.REINFORCE);
//
//	}
//
//	/**
//	 * Testing invalid command
//	 * Context already set by loading map etc and being in fortification phase.
//	 * Then call invalid command
//	 * Expected Error Message:"Cannot recognize this command in Fortification Phase. Try Again"
//	 * GameState will remain in FORTIFICATION as well
//	 */
//	@Test public void test003_fortifyInvalidCommand(){
//
//		//Context already set
//
//		String invalidFortificationCommand="fortify dafwailgflgl wasdfov wefiovlsa iv";
//
//		//Method call
//		phaseViewTest.receiveCommand(invalidFortificationCommand);
//
//
//
//		String strOutput="Invalid Fortification Command. Try Again";
//		String displayMessage=phaseViewTest.getStrDisplayMessage();
//
//		assertEquals(strOutput,displayMessage);
//		assertTrue(mapService.getGameState()==GameState.FORTIFY);
//
//	}
//
//	/**
//	 * Testing invalid country
//	 * Context already set; fromCountry is a valid countryname
//	 * Then we call fortification with an invalid countryname
//	 * Expected Error Message: "Invalid Country Name Entered"
//	 * Also GameState should remain in FORTIFICATION.
//	 */
//	@Test public void test004_fortifyInvalidCountry() {
//
//		//Context
//		Player currentPlayer=playerService.getCurrentPlayer();
//
//		//Get first country in player list
//		Country fromCountry=currentPlayer.getCountryList().get(0);
//
//		String strInvalidCountryName="wafafdfadadfsfdsasafdfsrfafbgskfsbikbfkibaisfkbasikfvbsfvziks";
//
//		String fortificationCommand="fortify "+fromCountry.getCountryName()+" "+strInvalidCountryName+
//				" "+5;
//		//Method call
//		phaseViewTest.receiveCommand(fortificationCommand);
//
//		//Evaluation
//		String strOutput="Invalid Country Name Entered";
//		String strDisplayMessage=phaseViewTest.getStrDisplayMessage();
//
//		assertEquals(strOutput,strDisplayMessage);
//		assertTrue(mapService.getGameState()==GameState.FORTIFY);
//
//
//	}
//
//	/**
//	 * Context: Testing fortification when 2 countries not belong to the same owner are called.
//	 * fortification is called using fromCountry of owner but toCountry of next player
//	 * Expected Result: Fortification should not take place: GameState should be in FORTIFY,
//	 * Number of soldiers of fromCountry and toCountry should be unchanged as well.
//	 */
//	@Test public void test005_invalidCountryOwnership() {
//
//		//Context
//
//		//Get first country in current player list
//		Player currentPlayer=playerService.getCurrentPlayer();
//		Country fromCountry=currentPlayer.getCountryList().get(0);
//
//		//Get first country in next player list
//		Player nextPlayer=playerService.getNextPlayer();
//		Country toCountry=nextPlayer.getCountryList().get(0);
//
//		//Set valid numbers for numSoldiers
//		fromCountry.setSoldiers(5);
//		int numSoldiersToMove=3;
//
//		String strFortificationCommand="fortify "+fromCountry.getCountryName()+" "+
//		toCountry.getCountryName()+" "+numSoldiersToMove;
//
//		//Method call
//		phaseViewTest.receiveCommand(strFortificationCommand);
//
//		//Evaluation
//		  Object obj=phaseViewTest.getReturnedObject(); PlayerFortificationWrapper
//		  playerFortificationWrapper=((PlayerFortificationWrapper)obj);
//
//		  assertEquals(playerFortificationWrapper.getCountryFrom().getSoldiers().intValue(),
//				  fromCountry.getSoldiers().intValue());
//
//		  assertEquals(playerFortificationWrapper.getCountryTo().getSoldiers().intValue(),
//				  toCountry.getSoldiers().intValue());
//
//		  assertTrue(mapService.getGameState()==GameState.FORTIFY);
//
//	}
//
//	/**
//	 *Context: Testing fortification command between 2 countries belonging to current player but not adjacent to each other
//	 *Fortification is done from a country to itself such that it is not adjacent to each other technically.
//	 *Expected: GameState should remain FORTIFY. No changed should be done in numSoldiers to tested country.
//	 *Error message should be displayed: "Countries not adjacent to each other"
//	 */
//	public void test006_invalidCountryAdjacencySameOwnership() {
//
//		//Context
//
//		//Get first country in current player list
//		Player currentPlayer=playerService.getCurrentPlayer();
//		Country fromCountry=currentPlayer.getCountryList().get(0);
//		Country toCountry=currentPlayer.getCountryList().get(0);
//
//		String strFortificationCommand="fortify "+fromCountry.getCountryName()+" "+
//		toCountry.getCountryName()+" "+1;
//
//		//Method call
//		phaseViewTest.receiveCommand(strFortificationCommand);
//
//		//Evaluation
//		  Object obj=phaseViewTest.getReturnedObject(); PlayerFortificationWrapper
//		  playerFortificationWrapper=((PlayerFortificationWrapper)obj);
//
//		  String strOutput="Countries not adjacent to each other";
//		  String strDisplayMessage=phaseViewTest.getStrDisplayMessage();
//
//		  assertEquals(strOutput,strDisplayMessage);
//
//		  assertEquals(playerFortificationWrapper.getCountryFrom().getSoldiers().intValue(),
//				  fromCountry.getSoldiers().intValue());
//
//		  assertEquals(playerFortificationWrapper.getCountryTo().getSoldiers().intValue(),
//				  toCountry.getSoldiers().intValue());
//
//		  assertTrue(mapService.getGameState()==GameState.FORTIFY);
//
//	}
//
//	/**
//	 * Context: Test fortification with valid ownership and adjacency but invalid numSoldiers (negative)
//	 * Error message:"Num soldiers must be greater than 0."
//	 * Also GameState must remain in FORTIFICATIOn and there should be no changes in numSoldiers of toCountry and fromCountry.
//	 */
//	@Test public void test007_invalidNumSoldiersLOWERBOUND() {
//
//		//Context
//
//		//Get first country in current player list
//		Player currentPlayer=playerService.getCurrentPlayer();
//		Country fromCountry=currentPlayer.getCountryList().get(0);
//
//		//Get adjacent country in chosen country and set owner to current player
//		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromCountry.getId());
//
//		//Get first adjacent country in country's list
//		Country toCountry = null;
//		for(Integer i: fromCountryAdjacencyList) {
//			toCountry=mapService.getCountryById(i).get();
//			break;
//		}
//
//		//set toCountry to current player as well
//		toCountry.setPlayer(currentPlayer);
//
//		int invalidNumSoldierLOWERBOUND=-1;
//
//		String strFortificationCommand="fortify "+fromCountry.getCountryName()+" "+
//		toCountry.getCountryName()+" "+invalidNumSoldierLOWERBOUND;
//
//		//Method Call
//		phaseViewTest.receiveCommand(strFortificationCommand);
//
//		//Evaluation
//		  Object obj=phaseViewTest.getReturnedObject(); PlayerFortificationWrapper
//		  playerFortificationWrapper=((PlayerFortificationWrapper)obj);
//
//		  String strOutput="Num soldiers must be greater than 0.";
//		  String strDisplayMessage=playerFortificationWrapper.getFortificationDisplayMessage();
//
//		  assertEquals(strOutput,strDisplayMessage);
//
//		  assertEquals(playerFortificationWrapper.getCountryFrom().getSoldiers().intValue(),
//				  fromCountry.getSoldiers().intValue());
//
//		  assertEquals(playerFortificationWrapper.getCountryTo().getSoldiers().intValue(),
//				  toCountry.getSoldiers().intValue());
//
//		  assertTrue(mapService.getGameState()==GameState.FORTIFY);
//
//
//	}
//
//	/**
//	 * Context: Test fortification with valid ownership and adjacency but invalid numSoldiers (greater than fromCountry soldiers)
//	 * Error message:"Not enough soldiers in origin country"
//	 * Also GameState must remain in FORTIFICATION phase and there should be no changes in numSoldiers of toCountry and fromCountry.
//	 */
//	@Test public void test007_invalidNumSoldiersUPPERBOUND() {
//
//		//Context
//
//		//Get first country in current player list
//		Player currentPlayer=playerService.getCurrentPlayer();
//		Country fromCountry=currentPlayer.getCountryList().get(0);
//
//		//Get adjacent country in chosen country and set owner to current player
//		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromCountry.getId());
//
//		//Get first adjacent country in country's list
//		Country toCountry = null;
//		for(Integer i: fromCountryAdjacencyList) {
//			toCountry=mapService.getCountryById(i).get();
//			break;
//		}
//
//		//set toCountry to current player as well
//		toCountry.setPlayer(currentPlayer);
//
//		int invalidNumSoldierUPPERBOUND=fromCountry.getSoldiers();
//
//		String strFortificationCommand="fortify "+fromCountry.getCountryName()+" "+
//		toCountry.getCountryName()+" "+invalidNumSoldierUPPERBOUND;
//
//		//Method Call
//		phaseViewTest.receiveCommand(strFortificationCommand);
//
//		//Evaluation
//		  Object obj=phaseViewTest.getReturnedObject(); PlayerFortificationWrapper
//		  playerFortificationWrapper=((PlayerFortificationWrapper)obj);
//
//		  String strOutput="Not enough soldiers in origin country";
//		  String strDisplayMessage=playerFortificationWrapper.getFortificationDisplayMessage();
//
//		  assertEquals(strOutput,strDisplayMessage);
//
//		  assertEquals(playerFortificationWrapper.getCountryFrom().getSoldiers().intValue(),
//				  fromCountry.getSoldiers().intValue());
//
//		  assertEquals(playerFortificationWrapper.getCountryTo().getSoldiers().intValue(),
//				  toCountry.getSoldiers().intValue());
//
//		  assertTrue(mapService.getGameState()==GameState.FORTIFY);
//
//
//	}
//
//	/**
//	 * Context: Test valid fortification move with all criterias of adjacency same-country-ownership and numSoldiers met.
//	 * HOWEVER, countries called are is not current player's countries.
//	 * Expected Result: Error Message:"fromCountry or toCountry does not belong to current player"
//	 * Also Game should remain in FORTIFICATION phase and there should be no changed to countries input.
//	 */
//	@Test public void test008_validFortificationDifferentPlayerTurn() {
//		//Context
//
//				Player nextPlayer=playerService.getNextPlayer();
//
//				//Get first country in player list
//				Country fromCountry=nextPlayer.getCountryList().get(0);
//				Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromCountry.getId());
//
//				//Get first adjacent country in country's list
//				Country toCountry = null;
//				for(Integer i: fromCountryAdjacencyList) {
//					toCountry=mapService.getCountryById(i).get();
//					break;
//				}
//
//				//set toCountry to nextPlayer as well
//				toCountry.setPlayer(nextPlayer);
//				int initialToCountrySoldiers=toCountry.getSoldiers().intValue();
//
//				//Set numSoldiers in fromCountry to 5
//				int initialFromCountrySoldiers=5;
//				fromCountry.setSoldiers(initialFromCountrySoldiers);
//				int numSoldiersToMove=3;
//
//				String validFortificationCommand="fortify "+fromCountry.getCountryName()+" "+
//				toCountry.getCountryName()+" "+numSoldiersToMove;
//
//				//Method call
//				phaseViewTest.receiveCommand(validFortificationCommand);
//
//				//Evaluation
//
//				Object obj=phaseViewTest.getReturnedObject(); PlayerFortificationWrapper
//				playerFortificationWrapper=((PlayerFortificationWrapper)obj);
//
//				String strOutput="fromCountry or toCountry does not belong to current player";
//				String strDisplayMessage=playerFortificationWrapper.getFortificationDisplayMessage();
//
//				assertEquals(strOutput,strDisplayMessage);
//
//				assertEquals(playerFortificationWrapper.getCountryFrom().getSoldiers().intValue(),
//						  fromCountry.getSoldiers().intValue());
//
//				assertEquals(playerFortificationWrapper.getCountryTo().getSoldiers().intValue(),
//						  toCountry.getSoldiers().intValue());
//
//				assertTrue(mapService.getGameState()==GameState.FORTIFY);
//
//	}
//
//
//
//
//	  /**
//	   * Method to load a map.
//	   * Method first exits from editmapphase by sending command exitmapedit.
//	   * Then command to loadmap is sent.
//	   * @param mapName receives mapName
//	   */
//	  	public void loadValidMap(String mapName) {
//	  		phaseViewTest.receiveCommand("exitmapedit"); // Exit Map Editing Phase
//
//	  		phaseViewTest.receiveCommand("loadmap " + mapName); // Load ameroki map
//	}
//
//	/**
//	 * Method that sends command to add a player
//	 * @param name of player
//	 */
//	public void addPlayer(String name) {
//		phaseViewTest.receiveCommand("gameplayer -add " + name);
//	}
//
//	/**
//	 * Method that sends command to remove a player
//	 * @param name of player
//	 */
//	public void removePlayer(String name) {
//		phaseViewTest.receiveCommand("gameplayer -remove " + name);
//	}
//
//	/**
//	 * Method that instantiates all required objects before testing
//	 */
//	public void createObjects() {
//
//		mapService = new MapService();
//		playerService = new PlayerService(mapService);
//
//		phaseViewTest = new PhaseViewTest();
//		controllerList = new ArrayList<>();
//
//		mapLoaderAdapter = new MapLoaderController(mapService);
//		startupGameController = new StartupGameController(mapLoaderAdapter, playerService);
//        reinforceGameController = new ReinforceGameController(playerService);
//        fortifyGameController = new FortifyGameController(playerService);
//        attackController = new AttackGameController(playerService);
//
//		controllerList.add(mapLoaderAdapter);
//		controllerList.add(startupGameController);
//        controllerList.add(reinforceGameController);
//        controllerList.add(fortifyGameController);
//        controllerList.add(attackController);
//
//		phaseViewTest.addController(controllerList);
//
//		mapLoaderAdapter.setView(phaseViewTest);
//		startupGameController.setView(phaseViewTest);
//        reinforceGameController.setView(phaseViewTest);
//        fortifyGameController.setView(phaseViewTest);
//        attackController.setView(phaseViewTest);
//
//
//
//		mapService.addObserver(phaseViewTest);
//		playerService.addObserver(phaseViewTest);
//
//	}
//
//}
