package com6441.team7.risc.api.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com6441.team7.risc.api.wrapperview.PlayerAttackWrapper;
import com6441.team7.risc.controller.AttackGameController;
import com6441.team7.risc.controller.Controller;
import com6441.team7.risc.controller.FortifyGameController;
import com6441.team7.risc.controller.MapLoaderController;
import com6441.team7.risc.controller.ReinforceGameController;
import com6441.team7.risc.controller.StartupGameController;
import com6441.team7.risc.view.PhaseViewTest;

public class PlayerServiceTest {

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
	 * Method called before each test method.
	 * It creates views and controllers, loads a valid map,
	 * Adds 2 players, and populates all countries.
	 */
	@Before
	public void setupTest(){
		createObjects();

		loadValidMap("luca.map");

		addPlayer("Keshav");
		addPlayer("Binsar");

		phaseViewTest.receiveCommand("populatecountries");
		phaseViewTest.receiveCommand("placeall");

		mapService.setState(GameState.ATTACK);
	}


	/**
	 * Tests addPlayer() method
	 * The test passes if the expected name is equals to the name retrieved using the getter
	 */
	@Test
	public void test001_addPlayer() {
		String name = "John";
		playerService.addPlayer(name);
		String expectedName = name;
		assertTrue(playerService.getPlayerByName(name).getName().equals(expectedName));
	}

	/**
	 * Tests removePlayer() method
	 * The test passes if the method returns true
	 */
	@Test
	public void test002_removePlayer() {
		String name = playerService.getCurrentPlayerName();
		assertTrue(playerService.removePlayer(name));
	}
	
	/**
	 * Testing switch player method
	 * Evaluation: should switch player and then return to first player of list (round-robin) when
	 * switch player has been called by last player.
	 */
	@Test public void test003_switchNextPlayer() {
		
		//context
		Player nextPlayer=playerService.getNextPlayer();
		String nextPlayerName=nextPlayer.getName();
		
		//Method call
		playerService.switchNextPlayer();
		
		//Evaluation
		String currentPlayerName=playerService.getCurrentPlayerName();
		
		assertEquals(currentPlayerName,nextPlayerName);
		
		//Context to test round-robin player
		Player nextNextPlayer=playerService.getNextPlayer();
		String nextNextPlayerName=nextNextPlayer.getName();
		
		//Method call
		playerService.switchNextPlayer();
		
		//Evaluation
		currentPlayerName=playerService.getCurrentPlayerName();
		assertEquals(currentPlayerName,nextNextPlayerName);
		
		
	}

	/**
	 * Method to load a map. Method first exits from editmapphase by sending command
	 * exitmapedit. Then command to loadmap is sent.
	 *
	 * @param mapName receives value from map name
	 */
	public void loadValidMap(String mapName) {
		phaseViewTest.receiveCommand("exitmapedit"); // Exit Map Editing Phase

		phaseViewTest.receiveCommand("loadmap " + mapName); // Load ameroki map
	}

	/**
	 * Method that sends command to add a player
	 *
	 * @param name of player
	 */
	public void addPlayer(String name) {
		phaseViewTest.receiveCommand("gameplayer -add " + name);
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
		reinforceGameController = new ReinforceGameController(playerService);
		fortifyGameController = new FortifyGameController(playerService);
		attackController = new AttackGameController(playerService);

		controllerList.add(mapLoaderController);
		controllerList.add(startupGameController);
		controllerList.add(reinforceGameController);
		controllerList.add(fortifyGameController);
		controllerList.add(attackController);

		phaseViewTest.addController(controllerList);

		mapLoaderController.setView(phaseViewTest);
		startupGameController.setView(phaseViewTest);
		reinforceGameController.setView(phaseViewTest);
		fortifyGameController.setView(phaseViewTest);
		attackController.setView(phaseViewTest);

		mapService.addObserver(phaseViewTest);
		playerService.addObserver(phaseViewTest);

	}
}
