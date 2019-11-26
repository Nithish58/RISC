package com6441.team7.risc.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.api.wrapperview.TournamentWrapper;
import com6441.team7.risc.view.PhaseViewTest;

/**
 * This class tests various commands and functionalities of the AttackGameController.
 * @author Bikash
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TournamentControllerTest {
	
	
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
      * Controller for loadgame
      */
     LoadGameController loadGameController;
	 
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
		loadValidMap("ameroki.map");
		
		//Set boolTounamentOn to true in Tournament Controller to prevent end of game after result and allow evaluation
		
		
	}
	
	/**
	 * Tests if tournament conditions are valid
	 * Also tests all benevolent strategies for tounament
	 * Expected Result: All Games should end in draw
	 * @throws Exception  on invalid
	 */
	@Test public void test001_validTournamentConditions_AllBenevolent() throws Exception {
		
		//Context
		//Adding 3 benevolent players, 5 games, 3 maps, 50 turns
		phaseViewTest.receiveCommand("testtournament -M ameroki.map Africa.map luca.map -P benevolent benevolent benevolent"
				+" -G 5 -D 50");
		
		TournamentWrapper tournamentWrapper=phaseViewTest.getTournamentWrapperForTest();
		
    	String[][] arrResults=tournamentWrapper.getTournamentResult();
    	
    	int countDraws=0;
    	
    	for(int i=0;i<arrResults.length;i++) {
    		
    		String strResult="\n\n"+tournamentWrapper.getMapList().get(i)+"\t\t";
    		
    		for(int j=0;j<arrResults[i].length;j++) {
    			
    			if(arrResults[i][j].equalsIgnoreCase("draw")) countDraws++;
    			    			
    		} 		
    	}
		
		//Evaluation: Number of draws should be equal to total number of games played
    	assertEquals(countDraws,(arrResults[1].length*arrResults.length));
		
	}
	
	/**
	 * Tests if humans are allowed to participate in tournament
	 * Expected: Tournament should not run when there are only humans.
	 * When there are humans, tournament should ignore them.
	 */
	@Test public void test002_addingInvalidHumanPlayers() {
		
		//Context
		//Adding 3 human players, 5 games, 3 maps, 50 turns
		
		String strInvalidPlayerCommand="testtournament -M ameroki.map Africa.map luca.map -P human human human"
				+" -G 5 -D 50";
		
		//Method Call
		phaseViewTest.receiveCommand(strInvalidPlayerCommand);
		
		//Evaluation
		assertFalse(startupGameController.getTournamentController().validatePlayerCommand(strInvalidPlayerCommand));
		
	}
	
	/**
	 * Tests if invalid map files are allowed to take part in tournament
	 * Expected: Tournament should not run when there are no valid map files.
	 * When there are invalid map files, tournament should ignore them.
	 */
	@Test public void test003_invalidMapFiles() {
		
		//Context
		//Adding 3 valid players, 5 games, 3 invalid maps, 50 turns
		
		String strInvalidMapCommand="testtournament -M mauritius.map APP.map invalidTestMap.map -P benevolent cheater cheater"
				+" -G 5 -D 50";
		
		//Method Call
		phaseViewTest.receiveCommand(strInvalidMapCommand);
		
		//Evaluation
		assertFalse(startupGameController.getTournamentController().validateMapCommand(strInvalidMapCommand));
		
	}
	
	/**
	 * Test if cheater wins tournament atleast 1
	 * Expected: Given number of turns (1000) we set for cheater,
	 * it should win atleast once, else something is wrong either with cheater or tournament mode as a whole
	 */
	@Test public void test004_cheaterWinTournament() {
		
		//Context
		//Adding 1 cheater player, 5 games, 3 maps, 1000 turns
		phaseViewTest.receiveCommand("testtournament -M ameroki.map Africa.map luca.map -P benevolent cheater benevolent"
				+" -G 5 -D 1000");
		
		TournamentWrapper tournamentWrapper=phaseViewTest.getTournamentWrapperForTest();
		
    	String[][] arrResults=tournamentWrapper.getTournamentResult();
    	
    	int countCheater=0;
    	
    	for(int i=0;i<arrResults.length;i++) {
    		
    		String strResult="\n\n"+tournamentWrapper.getMapList().get(i)+"\t\t";
    		
    		for(int j=0;j<arrResults[i].length;j++) {
    			
    			if(!arrResults[i][j].equalsIgnoreCase("draw")) countCheater++;
    			    			
    		} 		
    	}
		
		//Evaluation: Cheater should win atleast 1 of them
    	assertTrue(countCheater>0);
		
	}
	
	/**
	 * Test if tournament goes to completion with any kind of player
	 * Cheater excluded
	 * Expected Result: Array Table Result should not be null
	 */
	@Test public void test005_testTournamentCompletion() {
		
		//Context
		//Adding 1 kind of each player except cheater, 5 games, 3 maps, 50 turns
		phaseViewTest.receiveCommand("testtournament -M ameroki.map Africa.map luca.map -P benevolent random aggressive human"
				+" -G 5 -D 50");
		
		TournamentWrapper tournamentWrapper=phaseViewTest.getTournamentWrapperForTest();
		
    	String[][] arrResults=tournamentWrapper.getTournamentResult();
    
    	
    	//Evaluation: Results table should not be null
    	assertNotNull(arrResults);
		
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
        controllerList.add(reinforceGameController);
        controllerList.add(fortifyGameController);
        controllerList.add(attackController);
        controllerList.add(loadGameController);
		
		phaseViewTest.addController(controllerList);

		mapLoaderController.setView(phaseViewTest);
		startupGameController.setView(phaseViewTest);
        reinforceGameController.setView(phaseViewTest);
        fortifyGameController.setView(phaseViewTest);
        attackController.setView(phaseViewTest);
        loadGameController.setView(phaseViewTest);
		

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
		
	  		phaseViewTest.receiveCommand("exitloadgame");
	  		
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
		
		
  	
	  	
}
