package com6441.team7.risc.api.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.api.wrapperview.PlayerAttackWrapper;
import com6441.team7.risc.controller.AttackGameController;
import com6441.team7.risc.controller.Controller;
import com6441.team7.risc.controller.FortifyGameController;
import com6441.team7.risc.controller.LoadGameController;
import com6441.team7.risc.controller.MapLoaderController;
import com6441.team7.risc.controller.ReinforceGameController;
import com6441.team7.risc.controller.StartupGameController;
import com6441.team7.risc.view.PhaseViewTest;

/**
 * Test class for Cheater Strategy
 * @author Keshav
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CheaterStrategyTest {
	
	
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
	 * AggressiveStrategy class This will be used for testing
	 */
	CheaterStrategy cheaterStrategy;

	
	@Test
	public void test001_reinforce() {
		System.out.println("Cheater reinforce");
		//Setting Context
		
		mapService.setState(GameState.REINFORCE);
		
		Player currentPlayer=playerService.getCurrentPlayer();
		
		currentPlayer.setPlayerCategory("benevolent");
		
		Player nextPlayer=playerService.getNextPlayer();
		
		currentPlayer.setPlayerCategory("cheater");
		
		ArrayList<Integer> beforeCheaterReinforcement=
				new ArrayList<Integer>();
		
		for(Country c:currentPlayer.getCountryPlayerList()) {
			beforeCheaterReinforcement.add(c.getSoldiers());
		}
		
		//MethodCall
		cheaterStrategy=new CheaterStrategy(playerService);
		cheaterStrategy.reinforce();
		
		//Evaluation
		
		for(int i=0;i<currentPlayer.getCountryPlayerList().size();i++) {
			assertEquals(currentPlayer.getCountryPlayerList().get(i).getSoldiers().intValue(),
					(beforeCheaterReinforcement.get(i).intValue()*2));
		}
		
		//In case cheater has no countries and does not do any assertions
		assertTrue(true);
	}
	
	
	
	
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

		addCheaterPlayer("Keshav");
		addBenevolentPlayer("Binsar");

		phaseViewTest.receiveCommand("populatecountries");
		phaseViewTest.receiveCommand("placeall");

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
	 * Method that sends command to add a player It is set to cheater as to
	 * avoid running automated game after army placement The strategy is set right
	 * before every test
	 * 
	 * @param name of player
	 */
	public void addCheaterPlayer(String name) {
		phaseViewTest.receiveCommand("gameplayer -add " + name + " human");
	}
	
	/**
	 * Method that sends command to add a player It is set to benevolent as to
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

		addCheaterPlayer("Keshav");
		addBenevolentPlayer("Binsar");

		phaseViewTest.receiveCommand("populatecountries");
		phaseViewTest.receiveCommand("placeall");

	}

}
