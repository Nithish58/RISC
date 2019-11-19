//package com6441.team7.risc.controller;
//
//import static org.junit.Assert.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//
//import com6441.team7.risc.api.model.GameState;
//import com6441.team7.risc.api.model.MapService;
//import com6441.team7.risc.api.model.Player;
//import com6441.team7.risc.api.model.PlayerService;
//import com6441.team7.risc.view.PhaseViewTest;
//
///**
// * This class tests various commands and functionalities of the AttackGameController.
// * @author Bikash
// */
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class AttackGameControllerTest {
//
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
//	 MapLoaderAdapter mapLoaderAdapter;
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
//	 Player currentPlayer;
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
//		mapService.setState(GameState.ATTACK);
//
//		this.currentPlayer=playerService.getCurrentPlayer();
//		attackController.setView(phaseViewTest);
//
//	}
//
//	/**
//	 * Tests a valid attack command on the AttackGameController
//	 * It returns true if the game state after the command is executed is "fortify"
//	 * @throws Exception  on invalid
//	 */
//	@Test public void test001_validAttackCommand() throws Exception {
//		String expected = "fortify";
//		attackController.readCommand("attack -noattack");
//		assertTrue(mapService.getGameState().getName().equals(expected));
//
//	}
//
//	/**
//	 * The expected game state is "fortify"
//	 * It returns true if the game state after the method is called is "fortify"
//	 */
//	@Test public void test002_endAttack() {
//		String expected = "fortify";
//		attackController.switchToFortification();
//		assertTrue(mapService.getGameState().getName().equals(expected));
//	}
//
//
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
//		mapLoaderAdapter = new MapLoaderAdapter(mapService);
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
//	  /**
//	   * Method to load a map.
//	   * Method first exits from editmapphase by sending command exitmapedit.
//	   * Then command to loadmap is sent.
//	   * @param mapName receives map name
//	   */
//	  	public void loadValidMap(String mapName) {
//	  		phaseViewTest.receiveCommand("exitmapedit"); // Exit Map Editing Phase
//
//	  		phaseViewTest.receiveCommand("loadmap " + mapName); // Load ameroki map
//	}
//
//
//		/**
//		 * Method that sends command to add a player
//		 * @param name of player
//		 */
//		public void addPlayer(String name) {
//			phaseViewTest.receiveCommand("gameplayer -add " + name);
//		}
//
//		/**
//		 * Method that sends command to remove a player
//		 * @param name of player
//		 */
//		public void removePlayer(String name) {
//			phaseViewTest.receiveCommand("gameplayer -remove " + name);
//		}
//
//
//}
