package com6441.team7.risc.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.view.PhaseViewTest;

/**
 * This class tests various commands and functionalities of the AttackGameController.
 * @author Bikash
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AttackGameControllerTest {
	
	
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
	  * Controller for Reinforcement phase
	  */
     ReinforceGameController reinforceGameController ;
     /**
      * Controller for Fortification phase
      */
     FortifyGameController fortifyGameController ;
     /**
      * Controller for Attack phase
      */
     AttackGameController attackController ;
	 
	 /**
	  * list of different controllers
	  */
	 List<Controller> controllerList;
	 
	 Player currentPlayer;
	 
	 /**
	  * Method called before each test case
	  * Instantiates necessary objects and loads a valid map, adds 3 players, populates countries,
	  * places armies, then skips reinforcement and attack phase and goes directly to fortification phase.
	  */
	@Before public void beforeEachTest() {
		createObjects();
		
		loadValidMap("luca.map");
		
		addPlayer("Keshav");
		addPlayer("Binsar");
		addPlayer("Bikas");
		
		phaseViewTest.receiveCommand("populatecountries");
		phaseViewTest.receiveCommand("placeall");
		
		mapService.setState(GameState.ATTACK);
		
		this.currentPlayer=playerService.getCurrentPlayer();
		
	}
	
	/**
	 * Tests validAttackCommand() method on the AttackGameController
	 */
	@Ignore
	@Test public void test001_validAttackCommand() {
		
		Country fromCountryAttack=currentPlayer.getCountryList().get(0);
		
		//Get adjacent country in chosen country
		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromCountryAttack.getId());
				
		//Get first adjacent country in country's list
		Country toCountryAttack = null;								
		for(Integer i: fromCountryAdjacencyList) {
			toCountryAttack=mapService.getCountryById(i).get();
			break;
			}
		
		String fromCountryName=fromCountryAttack.getCountryName();
		String toCountryName=toCountryAttack.getCountryName();
		
		int numDiceAttacker=3;
		int numDiceDefender=2;
		
		currentPlayer.setNumDiceAttacker(numDiceAttacker);
		currentPlayer.setNumDiceDefender(numDiceDefender);
		
		fromCountryAttack.setSoldiers(10);
		toCountryAttack.setSoldiers(10);
		
		int[] arrDiceAttacker=  {6,6,6};
		int[] arrDiceDefender= {5,5};
		
		currentPlayer.decideBattleResult(arrDiceAttacker, arrDiceDefender);
		
		assertEquals(mapService.getCountryByName(fromCountryName).get().getSoldiers().intValue(),10);
		assertEquals(mapService.getCountryByName(toCountryName).get().getSoldiers().intValue(),8);
		
	}
	
	/**
	 * Tests method to end attack phase.
	 * The expected game state is "fortify"
	 */
	@Test public void test002_endAttack() {
		String expected = "fortify";
		attackController.endAttackPhase();
		assertTrue(mapService.getGameState().getName().equals(expected));
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
	
	  /**
	   * Method to load a map.
	   * Method first exits from editmapphase by sending command exitmapedit.
	   * Then command to loadmap is sent.
	   * @param mapName receives map name
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
			phaseViewTest.receiveCommand("gameplayer -add " + name);
		}
		
		/**
		 * Method that sends command to remove a player
		 * @param name of player
		 */
		public void removePlayer(String name) {
			phaseViewTest.receiveCommand("gameplayer -remove " + name);
		}
  	
	  	
}
