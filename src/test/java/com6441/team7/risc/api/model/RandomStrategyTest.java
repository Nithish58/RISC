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
 * This is the test class for RandomStrategy class
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RandomStrategyTest {

	/**
	 * View which outputs test strings and has some other additonal functionalities
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
	 * RandomStrategy class This will be used for testing
	 */
	RandomStrategy randomStrategy;

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

		loadValidMap("strangereal.map");

		addRandomPlayer("Keshav");
		addRandomPlayer("Binsar");

		phaseViewTest.receiveCommand("populatecountries");
		phaseViewTest.receiveCommand("placeall");

	}

	/**
	 * Testing the attack method for random player. The test passes if the game
	 * state is switched to ATTACK
	 */
	@Test
	public void test001_reinforce() {
		System.out.println("Random player reinforce");

		Player currentPlayer = playerService.getCurrentPlayer();

		currentPlayer.generatePlayerCategory("random");

		mapService.setState(GameState.REINFORCE);

		randomStrategy = new RandomStrategy(playerService);

		randomStrategy.reinforce();

		assertTrue(mapService.getGameState() == GameState.ATTACK);
	}

	/**
	 * Testing the aggressive attack method. The expected result is the total number
	 * of soldiers of either side's soldiers decrease or there are no adjacent
	 * target countries found
	 * 
	 * @throws Exception on invalid
	 */
	@Test
	public void test002_attack() throws Exception {
		System.out.println("Random attack");

		mapService.setState(GameState.ATTACK);

		randomStrategy = new RandomStrategy(playerService);
		
		int totalNumOfSoldiersBeforeAttack = 0;
		
		for (Country c : mapService.getCountries()) {
			totalNumOfSoldiersBeforeAttack += c.getSoldiers().intValue();
		}
		
		randomStrategy.attack();
		
		int totalNumSoldiersAfterAttack = 0;
		
		for (Country c : mapService.getCountries()) {
			totalNumSoldiersAfterAttack += c.getSoldiers().intValue();
		}
		
		assertTrue(totalNumSoldiersAfterAttack <= totalNumOfSoldiersBeforeAttack);
		
		assertTrue(mapService.getGameState()==GameState.FORTIFY);

		
	}

	/**
	 * Testing the fortification method for aggressive player. The expected result
	 * is the number of aggressive player's strongest country increases if there's
	 * an adjacent owned country found or stays the same if there aren't any
	 * 
	 */
	@Test
	public void test003_fortify() {
		System.out.println("Random fortify");
		
		mapService.setState(GameState.FORTIFY);

		randomStrategy = new RandomStrategy(playerService);
		
		Player currentPlayer = playerService.getCurrentPlayer();
		
		Player nextPlayer = playerService.getNextPlayer();
		
		randomStrategy.fortify();
		
		currentPlayer = playerService.getCurrentPlayer();
		
		assertEquals(currentPlayer.getName(), nextPlayer.getName());
		
		assertEquals(mapService.getGameState(), GameState.REINFORCE);
		

	}
	
	/**
	 * Testing fortify command with none option for random player.  
	 */
	@Test
	public void test004_fortifyNone() {
		System.out.println("Random fortify none");
		
		mapService.setState(GameState.FORTIFY);

		randomStrategy = new RandomStrategy(playerService);
		
		Player currentPlayer = playerService.getCurrentPlayer();
		
		Player nextPlayer = playerService.getNextPlayer();
		
		for (Country c : mapService.getCountries()) {
			
			c.setSoldiers(1);
		}
		
		randomStrategy.fortify();
		
		currentPlayer = playerService.getCurrentPlayer();
		
		assertEquals(currentPlayer.getName(), nextPlayer.getName());
		
		assertEquals(mapService.getGameState(), GameState.REINFORCE);
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
	public void addRandomPlayer(String name) {
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
		loadGameController = new LoadGameController(mapService, playerService);
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

		addRandomPlayer("Keshav");
		addRandomPlayer("Binsar");

		phaseViewTest.receiveCommand("populatecountries");
		phaseViewTest.receiveCommand("placeall");

	}
}