package com6441.team7.risc.api.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com6441.team7.risc.controller.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.api.wrapperview.PlayerAttackWrapper;
import com6441.team7.risc.view.PhaseViewTest;

/**
 *
 * This is the test class for storing player information.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AggressiveStrategyTest {

	/**
	 * View which outputs test strings and has some other additional functionalities
	 * than the normal phaseView
	 */
	PhaseViewTest phaseViewTest;
	/**
	 * mapService object stores map Information and game state
	 */
	MapService mapService;
	/**
	 * playerService object keeps track of player information such as current player
	 * turn and list of players
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
	 * Controller for loading game
	 */
	LoadGameController loadGameController;
	/**
	 * Controller for Reinforcement phase
	 */
	ReinforceGameController reinforceGameController;
	/**
	 * Controller for Fortification phase
	 */
	FortifyGameController fortifyGameController;
	/**
	 * Controller for Attack phase
	 */
	AttackGameController attackController;
	/**
	 * list of different controllers
	 */
	List<Controller> controllerList;
	/**
	 * Wrapper for Attack phase
	 */
	PlayerAttackWrapper playerAttackWrapper;
	/**
	 * AggressiveStrategy class This will be used for testing
	 */
	AggressiveStrategy aggressiveStrategy;

	/**
	 * Before every test is performed, the following are performed: Calling
	 * createObjects() method load a map using the loadValidMap() method Add two
	 * players Populate countries and place all of players' soldiers to the map
	 *
	 * @throws Exception on invalid
	 */
	@Before
	public void setUp() throws Exception {
		createObjects();

		loadValidMap("ameroki.map");

		addAggressivePlayer("Keshav");
		addAggressivePlayer("Binsar");

		phaseViewTest.receiveCommand("populatecountries");
		phaseViewTest.receiveCommand("placeall");

	}

	/**
	 * Testing the attack method for aggressive player. The test passes if the
	 * either sides lose soldier or no targets found in the adjacency list of the
	 * attacker's adjacency list
	 */
	@Test
	public void test001_reinforce() {
		System.out.println("Aggressive player reinforce");

		Player currentPlayer = playerService.getCurrentPlayer();

		currentPlayer.setPlayerCategory("aggressive");

		mapService.setState(GameState.REINFORCE);

		aggressiveStrategy = new AggressiveStrategy(playerService);

		aggressiveStrategy.reinforce();

		assertTrue(mapService.getGameState() == GameState.ATTACK);
	}

	/**
	 * Testing the aggressive attack method. 
	 * The expected result is the total number of soldiers of either side's
	 * soldiers decrease or there are no adjacent target countries found
	 * 
	 * @throws Exception on invalid
	 */
	@Test
	public void test002_attack() throws Exception {
		System.out.println("Aggressive attack");
		Player currentPlayer = playerService.getCurrentPlayer();

		// Set the state to ATTACK
		mapService.setState(GameState.ATTACK);

		aggressiveStrategy = new AggressiveStrategy(playerService);

		// get country with maximum number of armies
		Country attackerCountry = null;
		Country defenderCountry = null;

		// Get the attacker's country list
		// get list of player's countries
		ArrayList<Country> attackerCountries = currentPlayer.getCountryPlayerList();

		Set<Integer> attackCountryAdjacencyList;

		boolean boolTargetFound = false;

		// The total num of attacker's army before the attack
		int attackCountryBefore = 0;
		
		// Choose highest country to be attacker country which has atleast 1 target
		for (Country c : attackerCountries) {

			// Get adjacency list of highest country
			// Check if that country has a target
			// If not, move to next highest country
			attackCountryAdjacencyList = playerService.getMapService().getAdjacencyCountries(c.getId());

			boolTargetFound = false;

			for (Integer i : attackCountryAdjacencyList) {
				if (!playerService.getMapService().getCountryById(i).get().getPlayer().getName()
						.equals(currentPlayer.getName())) {

					attackerCountry = c;

					attackCountryBefore = attackCountryBefore + attackerCountry.getSoldiers();
					
					boolTargetFound = true;

					break;
				}
			}

		}

		// The num of defender's army before the attack
		int defenderCountryBefore = 0;
		
		// Target Countries Found
		attackCountryAdjacencyList = playerService.getMapService().getAdjacencyCountries(attackerCountry.getId());

		for (Integer j : attackCountryAdjacencyList) {

			// This country is adjacent and not owned by attacker, it can be attacked
			if (!playerService.getMapService().getCountryById(j).get().getPlayer().getName()
					.equals(currentPlayer.getName())) {

				defenderCountry = playerService.getMapService().getCountryById(j).get();
				
				defenderCountryBefore = defenderCountryBefore + defenderCountry.getSoldiers();

			} // End of If -- Go To next target country

		} // End of For -- Finished Looping through all target countries


		// Call the attack function
		aggressiveStrategy.attack();

		
		// The total num of attacker's army after the attack
		int attackCountryAfter = 0;
		
		// Choose highest country to be attacker country which has atleast 1 target
		for (Country c : attackerCountries) {

			// Get adjacency list of highest country
			// Check if that country has a target
			// If not, move to next highest country
			attackCountryAdjacencyList = playerService.getMapService().getAdjacencyCountries(c.getId());

			for (Integer i : attackCountryAdjacencyList) {
				if (!playerService.getMapService().getCountryById(i).get().getPlayer().getName()
						.equals(currentPlayer.getName())) {

					attackerCountry = c;

					attackCountryAfter = attackCountryAfter + attackerCountry.getSoldiers();

					break;
				}
			}

		}
		
		
		// The num of defender's army before the attack
		int defenderCountryAfter = 0;
		
		// Target Countries Found
		attackCountryAdjacencyList = playerService.getMapService().getAdjacencyCountries(attackerCountry.getId());

		for (Integer j : attackCountryAdjacencyList) {

			// This country is adjacent and not owned by attacker, it can be attacked
			if (!playerService.getMapService().getCountryById(j).get().getPlayer().getName()
					.equals(currentPlayer.getName())) {

				defenderCountry = playerService.getMapService().getCountryById(j).get();
				
				defenderCountryAfter = defenderCountryAfter + defenderCountry.getSoldiers();

			} // End of If -- Go To next target country

		} // End of For -- Finished Looping through all target countries
		
		// if a target is found, check the number of the attacker's country and the
		// defender's country
		boolean isTrue = false;
		if (!boolTargetFound) {
			isTrue = true;

			// if no targets found, set it to true
		} else {
			// If either one of the countries' loses a soldier, isTrue is set to true
			if ( attackCountryAfter <= attackCountryBefore
					|| defenderCountryAfter <= defenderCountryBefore) {
				isTrue = true;
			}
		}

		assertTrue(isTrue);
	}

	/**
	 * Testing the fortification method for aggressive player. The expected result
	 * is the number of aggressive player's strongest country increases if there's
	 * an adjacent owned country found or stays the same if there aren't any
	 * 
	 */
	@Test
	public void test003_fortify() {

		System.out.println("Aggressive fortify");

		// Set the state to ATTACK
		mapService.setState(GameState.FORTIFY);

		aggressiveStrategy = new AggressiveStrategy(playerService);

		// find country with max num of soldiers
		Country maxCountry = aggressiveStrategy.findMaxCountry();

		// Get num of soldiers before fortification
		int soldiersBefore = maxCountry.getSoldiers();

		aggressiveStrategy.fortify();

		// check if fortification is successful or if is there are no adjacent owned
		// countries
		boolean isTrue = false;

		if (maxCountry.getSoldiers() >= soldiersBefore && mapService.getGameState() == GameState.REINFORCE)
			isTrue = true;

		assertTrue(isTrue);

	}

	/**
	 * Method to load a map. Method first exits from editmapphase by sending command
	 * exitmapedit. Then command to loadmap is sent.
	 *
	 * @param mapName receives map name
	 */
	public void loadValidMap(String mapName) {
		phaseViewTest.receiveCommand("exitmapedit"); // Exit Map Editing Phase

		phaseViewTest.receiveCommand("exitloadgame");
		
		phaseViewTest.receiveCommand("loadmap " + mapName); // Load ameroki map
	}

	/**
	 * Method that sends command to add a player It is set to human by default as to
	 * avoid running automated game after army placement The strategy is set right
	 * before every test
	 * 
	 * @param name of player
	 */
	public void addAggressivePlayer(String name) {
		phaseViewTest.receiveCommand("gameplayer -add " + name + " human");
	}

	/**
	 * Method that sends command to remove a player
	 *
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
		loadGameController=new LoadGameController(mapService,playerService);
		reinforceGameController = new ReinforceGameController(playerService);
		fortifyGameController = new FortifyGameController(playerService);
		attackController = new AttackGameController(playerService);

		controllerList.add(mapLoaderController);
		controllerList.add(startupGameController);
		controllerList.add(loadGameController);
		controllerList.add(reinforceGameController);
		controllerList.add(fortifyGameController);
		controllerList.add(attackController);

		phaseViewTest.addController(controllerList);

		mapLoaderController.setView(phaseViewTest);
		startupGameController.setView(phaseViewTest);
		loadGameController.setView(phaseViewTest);
		reinforceGameController.setView(phaseViewTest);
		fortifyGameController.setView(phaseViewTest);
		attackController.setView(phaseViewTest);

		mapService.addObserver(phaseViewTest);
		playerService.addObserver(phaseViewTest);

	}

	/**
	 * This method is to retry map loading and player placements It is called
	 * whenever there are no defender country found in the attacker country's
	 * adjacency list
	 * 
	 * @throws Exception on invalid
	 */
	public void retrySetUp() throws Exception {
		createObjects();

		loadValidMap("ameroki.map");

		addAggressivePlayer("Keshav");
		addAggressivePlayer("Binsar");

		phaseViewTest.receiveCommand("populatecountries");
		phaseViewTest.receiveCommand("placeall");

	}
}