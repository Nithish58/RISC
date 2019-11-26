package com6441.team7.risc.api.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
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
public class BenevolentStrategyTest {

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
	 * BenevolentStrategy class This will be used for testing
	 */
	BenevolentStrategy benevolentStrategy;

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

		loadValidMap("luca.map");

		addBenevolentPlayer("Keshav");
		addBenevolentPlayer("Binsar");

		phaseViewTest.receiveCommand("populatecountries");
		phaseViewTest.receiveCommand("placeall");

	}

	/**
	 * Testing the reinforce method for benevolent player. The test passes if the
	 * reinforcement armies are added to weakest country.
	 */
	@Test
	public void test001_reinforce() {
		System.out.println("Aggressive player reinforce");

		Player currentPlayer = playerService.getCurrentPlayer();

		currentPlayer.setPlayerCategory("benevolent");

		mapService.setState(GameState.REINFORCE);

		benevolentStrategy = new BenevolentStrategy(playerService);
		
		Country setMinimumCountry = currentPlayer.getCountryPlayerList().get(0);
		
		setMinimumCountry.setSoldiers(0);


		
		//get reinforcement army calculation result
		int reinforcementsNum = benevolentStrategy.calculateReinforcedBenevolentArmies(currentPlayer);
		
		benevolentStrategy.reinforce();

		assertEquals(setMinimumCountry.getSoldiers().intValue(), reinforcementsNum);
	}

	/**
	 * Testing the attack method for benevolent player. The test passes if the
	 * benevolent player can attack correctly, that is never attack, and moves to next phase.
	 * 
	 * @throws Exception on invalid
	 */
	@Test
	public void test002_attack() throws Exception {
		System.out.println("Benevolent attack");
		Player currentPlayer = playerService.getCurrentPlayer();

		// Set the state to ATTACK
		mapService.setState(GameState.ATTACK);

		benevolentStrategy = new BenevolentStrategy(playerService);
		
		benevolentStrategy.attack();

		assertTrue(mapService.getGameState() == GameState.FORTIFY);
	}
	
	/**
	 * Testing the fortify method for benevolent player. The test passes if the
	 * benevolent player can fortify the weakest country that can be fortified correctly, 
	 * that is neighbors with spare armies, and moves to next phase.
	 * 
	 * @throws Exception on invalid
	 */
	@Test
	public void test003_fortify() throws Exception {
		System.out.println("Benevolent fortify");
		Player currentPlayer = playerService.getCurrentPlayer();

		// Set the state to FORTIFY
		mapService.setState(GameState.FORTIFY);

		benevolentStrategy = new BenevolentStrategy(playerService);
		
		for (Country c : mapService.getCountries()) {
			c.setSoldiers(1);
		}
		
		Player nextPlayer = playerService.getNextPlayer();
		
		benevolentStrategy.fortify();

		assertTrue(mapService.getGameState() == GameState.REINFORCE);
		
		assertEquals(nextPlayer.getName(), playerService.getCurrentPlayer().getName());
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
	public void addBenevolentPlayer(String name) {
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

		addBenevolentPlayer("Keshav");
		addBenevolentPlayer("Binsar");

		phaseViewTest.receiveCommand("populatecountries");
		phaseViewTest.receiveCommand("placeall");

	}
}