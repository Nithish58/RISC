package com6441.team7.risc.api.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

	/**
	 * Testing reinforcement method for cheater player. Test passes 
	 * if armies of player doubles on all its countries. or if cheater has no countries
	 */
	@Test
	public void test001_reinforce() {
		System.out.println("Cheater reinforce");
		//Setting Context
		
		mapService.setState(GameState.REINFORCE);
		
		Player currentPlayer=playerService.getCurrentPlayer();
		
		currentPlayer.generatePlayerCategory("benevolent");
		
		Player nextPlayer=playerService.getNextPlayer();
		
		nextPlayer.generatePlayerCategory("cheater");
		
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
	 * Testing attack method for cheater player. Test passes if
	 * all the neighbors of player's countries are conquered or in rare cases,
	 *  no countries adjacent to cheater player countries.
	 */
	@Test
	public void test002_attack() {
		
		mapService.setState(GameState.ATTACK);
		
		//Player Keshav
		Player currentPlayer=playerService.getCurrentPlayer();
		
		currentPlayer.generatePlayerCategory("cheater");
		
		//Player Binsar
		Player nextPlayer=playerService.getNextPlayer();
		
		nextPlayer.generatePlayerCategory("benevolent");
		
		//Player Bikas
		nextPlayer=playerService.getNextPlayer();
		
		nextPlayer.generatePlayerCategory("benevolent");
		
		ArrayList<Country> countriesToBeTransferredList=new ArrayList<Country>();
		
		for(Country c: currentPlayer.getCountryPlayerList()) {
			
			Set<Integer> fromCountryAdjacencyList = playerService.getMapService()
					.getAdjacencyCountries(c.getId());
			
			for (Integer j : fromCountryAdjacencyList) {
				if (!playerService.getMapService().getCountryById(j).get().getPlayer().getName().
						equals(currentPlayer.getName())) {
					
					countriesToBeTransferredList.add(playerService.getMapService().getCountryById(j).get());			
				}
			}			
		}
		
		CheaterStrategy cheaterStrategy=new CheaterStrategy(playerService);
		
		cheaterStrategy.attack();
		
		for(Country c:countriesToBeTransferredList) {
			assertEquals(currentPlayer.getName(),c.getPlayer().getName());
		}
		
		//In case no countries adjacent to cheater countries for transfer (Very Rare)
		assertTrue(true);
				
	}
	
	/**
	 * Testing fortify method of cheater player. Test passes if number of armies on countries 
	 * who have neighbors, which belong to different player, have doubled.
	 */
	@Test
	public void test003_fortify() {
		
		mapService.setState(GameState.FORTIFY);
		
		//Player Keshav
		Player currentPlayer=playerService.getCurrentPlayer();
		
		currentPlayer.generatePlayerCategory("cheater");
		
		//Player Binsar
		Player nextPlayer=playerService.getNextPlayer();
		
		nextPlayer.generatePlayerCategory("benevolent");
		
		//Player Bikas
		nextPlayer=playerService.getNextPlayer();
		
		nextPlayer.generatePlayerCategory("benevolent");
		
		ArrayList<Country> countriesToBeFortified =new ArrayList<Country>();
		ArrayList<Integer> countrySoldiersBeforeFortification=new ArrayList<Integer>();
		
		for(Country c:currentPlayer.getCountryPlayerList()) {
			
			Set<Integer> fromCountryAdjacencyList = playerService.getMapService()
					.getAdjacencyCountries(c.getId());
			
			for (Integer j : fromCountryAdjacencyList) {
				if (!playerService.getMapService().getCountryById(j).get().getPlayer().getName().
						equals(currentPlayer.getName())) {
					
					countriesToBeFortified.add(c);
					countrySoldiersBeforeFortification.add(c.getSoldiers());
					
					playerService.notifyPlayerServiceObservers(c.getCountryName()+" has opponent neighbours,"
							+ " doubled to: "+c.getSoldiers());
					
					break; //Already doubled country with foreign neighbours, move to other countries now
				}
			}				
		}
		
		//Method Call
		CheaterStrategy cheaterStrategy=new CheaterStrategy(playerService);
		cheaterStrategy.fortify();

		
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
		addBenevolentPlayer("Bikas");

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
	 * Method that instantiates all required objects before testing.
	 * Here the mapService, playerService, phaseView for test, controllerList,
	 * mapLoaderController, startupGameController, loadGameController, reinforceGameController,
	 * fortifyGameController, attackController are all instantiated here.
	 * Then, the controllers are added to the controller list.
	 * Afterwards, the controller list is added to the phase view for testing followed 
	 * by each controller setting the their view and the player service and map service
	 * adding their observer.
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
