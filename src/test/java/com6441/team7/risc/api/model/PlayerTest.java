package com6441.team7.risc.api.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.api.wrapperview.PlayerAttackWrapper;
import com6441.team7.risc.controller.AttackGameController;
import com6441.team7.risc.controller.Controller;
import com6441.team7.risc.controller.FortifyGameController;
import com6441.team7.risc.controller.MapLoaderController;
import com6441.team7.risc.controller.ReinforceGameController;
import com6441.team7.risc.controller.StartupGameController;
import com6441.team7.risc.view.PhaseViewTest;

/**
 *
 * This is the test class for storing player information.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlayerTest {

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


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Before every test is performed, the following are performed:
	 * Calling createObjects() method
	 * load a map using the loadValidMap() method
	 * Add two players
	 * Populate countries and place all of players' soldiers to the map
	 *
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		createObjects();

		loadValidMap("luca.map");

		addPlayer("Keshav");
		addPlayer("Binsar");

		phaseViewTest.receiveCommand("populatecountries");
		phaseViewTest.receiveCommand("placeall");

		mapService.setState(GameState.ATTACK);

	}

	/**
	 * This method is performed after every test
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Testing the single attack method.
	 * It tests the regular attack function (i.e.: without the allout param)
	 * The expected result is the number of either side's soldiers decrease by 1,
	 * depending on which side loses in the dice roll
	 * @throws Exception
	 */
	@Test
	public void test001_attackSingle() throws Exception {
		System.out.println("Attack single");
		Player currentPlayer = playerService.getCurrentPlayer();

		// Get first country in player list
		Country fromAttackCountry = currentPlayer.getCountryList().get(0);

		// numbers of soldiers on fromAttackCouuntry is set to 4 to ensure that a valid
		// number of
		// dices can be thrown
		fromAttackCountry.setSoldiers(4);
		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromAttackCountry.getId());

		// print adjacency list
		System.out.print("Adjacency list for attacker country " + fromAttackCountry.getCountryName()+" ");
		for (Integer i : fromCountryAdjacencyList)
			System.out.print(i + " ");
		System.out.println();

		// Get first adjacent country in country's list
		Country toAttackCountry = null;
		while (toAttackCountry == null) {
			for (Integer i : fromCountryAdjacencyList) {
				if (!mapService.getCountryById(i).get().getPlayer().getName().equals(currentPlayer.getName())) {
					toAttackCountry = mapService.getCountryById(i).get();
					System.out.println("Defender country is " + toAttackCountry.getCountryName());
					break;
				}
			}
			if (toAttackCountry == null) {
				System.out.println("No adjacent defender country found. Retrying the set up.");
				setUp();
			}
		}
		System.out.println("Defender country is " + toAttackCountry);
		// numbers of soldiers on toAttackCouuntry is set to 2 to ensure that a valid
		// number of
		// dices can be thrown
		toAttackCountry.setSoldiers(2);

		// expectedAttackerSoldier is the expected number of attacker soldier if
		// the attacker loses a soldier
		Integer expectedAttackerSoldier = fromAttackCountry.getSoldiers() - 1;

		// expectedDefenderSoldier is the expected number of defender soldier if
		// the defender loses a soldier
		Integer expectedDefenderSoldier = toAttackCountry.getSoldiers() - 1;

		// This is for checking the condition after attack
		boolean isTrue = false;

		// Instantiate playerAttackWrapper
		playerAttackWrapper = new PlayerAttackWrapper(fromAttackCountry, toAttackCountry);

		// Set the number of attacker dices to 3
		playerAttackWrapper.setNumDiceAttacker(3);

		// Set the number of defender dices to 1
		playerAttackWrapper.setNumDiceDefender(1);

		// Call the attack function
		currentPlayer.attack(playerService, playerAttackWrapper);

		// If either one of the countries' loses a soldier, isTrue is set to true
		if (expectedAttackerSoldier.equals(fromAttackCountry.getSoldiers())
				|| expectedDefenderSoldier.equals(toAttackCountry.getSoldiers()))
			isTrue = true;

		assertTrue(isTrue);
	}

	/**
	 * Tests attack until soldiers from either attacker or defender is out
	 * @throws Exception
	 */
	@Ignore
	@Test
	public void test002_attackAllOut() throws Exception {
		System.out.println("Attack all out");
		Player currentPlayer = playerService.getCurrentPlayer();

		// Get first country in player list
		Country fromAttackCountry = currentPlayer.getCountryList().get(0);

		// numbers of soldiers on fromAttackCouuntry is set to 4 to ensure that a valid
		// number of
		// dices can be thrown
		fromAttackCountry.setSoldiers(4);
		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromAttackCountry.getId());

		// Get first adjacent country in country's list
		Country toAttackCountry = null;
		while (toAttackCountry == null) {
			for (Integer i : fromCountryAdjacencyList) {
				if (!mapService.getCountryById(i).get().getPlayer().getName().equals(currentPlayer.getName())) {
					toAttackCountry = mapService.getCountryById(i).get();
					System.out.println("Defender country is " + toAttackCountry.getCountryName());
					break;
				}
			}
			if (toAttackCountry == null) {
				System.out.println("No adjacent defender country found. Retrying the set up.");
				setUp();
			}
		}

		// numbers of soldiers on toAttackCouuntry is set to 2 to ensure that a valid
		// number of
		// dices can be thrown
		toAttackCountry.setSoldiers(2);

		// expectedAttackerSoldier is the expected number of attacker soldier if
		// the attacker is only left with one soldier
		Integer expectedAttackerSoldier = 1;

		// expectedDefenderSoldier is the expected number of defender soldier if
		// the defender lost all of his/her soldiers
		Integer expectedDefenderSoldier = 0;

		// This is for checking the condition after attack
		boolean isTrue = false;

		// Instantiate playerAttackWrapper
		playerAttackWrapper = new PlayerAttackWrapper(fromAttackCountry, toAttackCountry);

		playerAttackWrapper.setBooleanAllOut();

		// Set the number of attacker dices to 3
		playerAttackWrapper.setNumDiceAttacker(3);

		// Set the number of defender dices to 1
		playerAttackWrapper.setNumDiceDefender(1);

		// Call the attack function
		currentPlayer.attack(playerService, playerAttackWrapper);

		// If either one of the countries' loses a soldier, isTrue is set to true
		if (currentPlayer.isAttackerLastManStanding() || currentPlayer.checkDefenderPushedOut())
			isTrue = true;

		assertTrue(isTrue);
	}

	/**
	 * Tests rolling attacker's dice
	 * The expected result is that the returned dice array has the same length as the expected one
	 */
	@Test
	public void test003_rollAttackerDice() {
		// Context
		Player currentPlayer = playerService.getCurrentPlayer();
		System.out.println("Roll attacker dice");
		int numOfDice = 3;
		int[] expectedAttackerDice = new int[numOfDice];

		assertTrue(expectedAttackerDice.length == currentPlayer.rollAttackerDice(numOfDice).length);
	}

	/**
	 * Tests rolling defender's dice
	 * The expected result is that the returned dice array has the same length as the expected one
	 */
	@Test
	public void test004_rollDefenderDice() {
		// Context
		Player currentPlayer = playerService.getCurrentPlayer();
		System.out.println("Roll defender dice");
		int numOfDice = 2;
		int[] expectedDefenderDice = new int[numOfDice];

		assertTrue(expectedDefenderDice.length == currentPlayer.rollDefenderDice(numOfDice).length);
	}

	/**
	 * Tests if the player wins the game
	 */
	@Ignore
	@Test
	public void test005_checkPlayerWin() {
		System.out.println("Check if the attacker wins the entire game");
		Player currentPlayer = playerService.getCurrentPlayer();
		System.out.println("Attacker country list size: " + currentPlayer.getCountryList().size());
		System.out.println("Total country list  size: " + mapService.getCountries().size());

		// Get first country in player list
		Country fromAttackCountry = currentPlayer.getCountryList().get(0);
		Country toAttackCountry = playerService.getNextPlayer().countryPlayerList.get(0);
		// numbers of soldiers on fromAttackCouuntry is set to 4 to ensure that a valid
		// number of
		// dices can be thrown
		fromAttackCountry.setSoldiers(1000);
		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromAttackCountry.getId());

		Set<Country> countryList = mapService.getCountries();

		for (Player p : playerService.getPlayerList()) {

			if (!p.getName().equals(fromAttackCountry.getPlayer().getName()))
				for (Country c : p.getCountryList()) {
					if (!c.getCountryName().equals(toAttackCountry.getCountryName()))
						toAttackCountry.getPlayer().getCountryList().remove(c.getId());
					currentPlayer.getCountryList().add(c);
				}

		}

		for (Country country : countryList) {
			System.out.println(country);
		}

		Set<Integer> toCountryAdjacencyList = mapService.getAdjacencyCountries(toAttackCountry.getId());

		System.out.println("Attacker country list size after transfer: " + currentPlayer.getCountryList().size());
		System.out.println("Total country list  size after: " + mapService.getCountries().size());
		// numbers of soldiers on toAttackCountry is set to 2 to ensure that a valid
		// number of
		// dices can be thrown
		toAttackCountry.setSoldiers(1);

		// expectedAttackerSoldier is the expected number of attacker soldier if
		// the attacker is only left with one soldier
		Integer expectedAttackerSoldier = 1;

		// expectedDefenderSoldier is the expected number of defender soldier if
		// the defender lost all of his/her soldiers
		Integer expectedDefenderSoldier = 0;

		// This is for checking the condition after attack
		boolean isTrue = false;

		// Instantiate playerAttackWrapper
		playerAttackWrapper = new PlayerAttackWrapper(fromAttackCountry, toAttackCountry);

		playerAttackWrapper.setBooleanAllOut();

		// Set the number of attacker dices to 3
		playerAttackWrapper.setNumDiceAttacker(3);

		// Set the number of defender dices to 1
		playerAttackWrapper.setNumDiceDefender(1);

		// Call the attack function
		currentPlayer.attack(playerService, playerAttackWrapper);
		System.out.println("Attacker country list size: " + currentPlayer.getCountryList().size());
		System.out.println("Total country list  size: " + mapService.getCountries().size());
		// If either one of the countries' loses a soldier, isTrue is set to true
		if (currentPlayer.getCountryList().size() == mapService.getCountries().size())
			isTrue = true;

		assertTrue(isTrue);
	}


	/**
	 * This checks if the attacker owns all the territory
	 * and is declared the winner
	 * @throws Exception on invalid value
	 */
	@Test
	public void test006_checkPlayerWinTheGame() throws Exception{
		System.out.println("Check if the attacker owns all countries and wins the entire game");
		Player currentPlayer = playerService.getCurrentPlayer();
		System.out.println("Attacker country list size: " + currentPlayer.getCountryList().size());
		System.out.println("Total country list  size: " + mapService.getCountries().size());

		// Get first country in player list
		Country fromAttackCountry = currentPlayer.getCountryList().get(0);
		Country toAttackCountry = playerService.getNextPlayer().countryPlayerList.get(0);
		// numbers of soldiers on fromAttackCouuntry is set to 4 to ensure that a valid
		// number of
		// dices can be thrown
		fromAttackCountry.setSoldiers(1000);
		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromAttackCountry.getId());

		Set<Country> countryList = mapService.getCountries();

		for (Player p : playerService.getPlayerList()) {

			if (!p.getName().equals(fromAttackCountry.getPlayer().getName()))
				for (Country c : p.getCountryList()) {
					toAttackCountry.getPlayer().getCountryList().remove(c.getId());
					currentPlayer.getCountryList().add(c);
				}

		}



		System.out.println("Attacker country list size after transfer: " + currentPlayer.getCountryList().size());
		System.out.println("Total country list  size after: " + mapService.getCountries().size());


		// This is for checking the condition after attack
		boolean isTrue = false;


		System.out.println("Attacker country list size: " + currentPlayer.getCountryList().size());
		System.out.println("Total country list  size: " + mapService.getCountries().size());
		// If either one of the countries' loses a soldier, isTrue is set to true
		if (currentPlayer.getCountryList().size() == mapService.getCountries().size())
			isTrue = true;

		assertTrue(isTrue);
	}

	/**
	 * Tests validate attack conditions
	 *
	 * @throws Exception
	 */
	@Test
	public void test007_validateAttackConditions() throws Exception {
		System.out.println("Validate attack conditions");
		Player currentPlayer = playerService.getCurrentPlayer();

		// Get first country in player list
		Country fromAttackCountry = currentPlayer.getCountryList().get(0);

		// numbers of soldiers on fromAttackCouuntry is set to 4 to ensure that a valid
		// number of
		// dices can be thrown
		fromAttackCountry.setSoldiers(25);
		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromAttackCountry.getId());

		// Get first adjacent country in country's list
		Country toAttackCountry = null;
		while (toAttackCountry == null) {
			for (Integer i : fromCountryAdjacencyList) {
				if (!mapService.getCountryById(i).get().getPlayer().getName().equals(currentPlayer.getName())) {
					toAttackCountry = mapService.getCountryById(i).get();
					System.out.println("Defender country is " + toAttackCountry.getCountryName());
					break;
				}
			}
			if (toAttackCountry == null) {
				System.out.println("No adjacent defender country found. Retrying the set up.");
				setUp();
			}
		}

		// numbers of soldiers on toAttackCouuntry is set to 2 to ensure that a valid
		// number of
		// dices can be thrown
		toAttackCountry.setSoldiers(2);

		// Instantiate playerAttackWrapper
		playerAttackWrapper = new PlayerAttackWrapper(fromAttackCountry, toAttackCountry);

		// Set the number of attacker dices to 3
		playerAttackWrapper.setNumDiceAttacker(3);

		// Set the number of defender dices to 1
		playerAttackWrapper.setNumDiceDefender(1);

		// Call the attack function
		currentPlayer.attack(playerService, playerAttackWrapper);

		assertTrue(currentPlayer.validateAttackConditions(playerService));
	}

	/**
	 * Testing if attacker country actually belongs to the attacker
	 * The test passes if the method isAttackerLastManStanding returns true
	 * @throws Exception on invalid value
	 */
	@Test
	public void test008_checkLastManStanding() throws Exception {
		System.out.println("Check last man standing for attacker");
		Player currentPlayer = playerService.getCurrentPlayer();

		// Get first country in player list
		Country fromAttackCountry = currentPlayer.getCountryList().get(0);

		// numbers of soldiers on fromAttackCouuntry is set to 1
		fromAttackCountry.setSoldiers(1);
		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromAttackCountry.getId());

		// print adjacency list
		System.out.print("Adjacency list for attacker country " + fromAttackCountry.getCountryName()+" ");
		for (Integer i : fromCountryAdjacencyList)
			System.out.print(i + " ");
		System.out.println();

		// Get first adjacent country in country's list
		Country toAttackCountry = null;
		while (toAttackCountry == null) {
			for (Integer i : fromCountryAdjacencyList) {
				if (!mapService.getCountryById(i).get().getPlayer().getName().equals(currentPlayer.getName())) {
					toAttackCountry = mapService.getCountryById(i).get();
					System.out.println("Defender country is " + toAttackCountry.getCountryName());
					break;
				}
			}
			if (toAttackCountry == null) {
				System.out.println("No adjacent defender country found. Retrying the set up.");
				setUp();
			}
		}
		System.out.println("Defender country is " + toAttackCountry);
		// numbers of soldiers on toAttackCouuntry is set to 2
		toAttackCountry.setSoldiers(2);

		// This is for checking the condition if the attacker only has 1 soldier left
		boolean isTrue = false;

		// Instantiate playerAttackWrapper
		playerAttackWrapper = new PlayerAttackWrapper(fromAttackCountry, toAttackCountry);

		// Set the number of attacker dices to 3
		playerAttackWrapper.setNumDiceAttacker(3);

		// Set the number of defender dices to 1
		playerAttackWrapper.setNumDiceDefender(1);

		// Call the attack function
		currentPlayer.attack(playerService, playerAttackWrapper);

		// If the attacker only has 1 soldier left in a country, isTrue is set to true
		if (currentPlayer.isAttackerLastManStanding())
			isTrue = true;

		assertTrue(isTrue);
	}

	/**
	 * Testing if the defender no longer has soldiers in one of their countries
	 * The test passes if the method checkDefenderPushedOut returns true
	 * @throws Exception 
	 */
	@Ignore
	@Test
	public void test009_checkIsDefenderPushedOut() throws Exception {
		System.out.println("Check if defender has no soldiers left in a country");
		Player currentPlayer = playerService.getCurrentPlayer();

		// Get first country in player list
		Country fromAttackCountry = currentPlayer.getCountryList().get(0);

		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromAttackCountry.getId());

		// print adjacency list
		System.out.print("Adjacency list for attacker country " + fromAttackCountry.getCountryName()+" ");
		for (Integer i : fromCountryAdjacencyList)
			System.out.print(i + " ");
		System.out.println();

		// Get first adjacent country in country's list
		Country toAttackCountry = null;
		while (toAttackCountry == null) {
			for (Integer i : fromCountryAdjacencyList) {
				if (!mapService.getCountryById(i).get().getPlayer().getName().equals(currentPlayer.getName())) {
					toAttackCountry = mapService.getCountryById(i).get();
					System.out.println("Defender country is " + toAttackCountry.getCountryName());
					break;
				}
			}
			if (toAttackCountry == null) {
				System.out.println("No adjacent defender country found. Retrying the set up.");
				setUp();
			}
		}
		
		// numbers of soldiers on fromAttackCountry is set to 10
		fromAttackCountry.setSoldiers(10);
		
		System.out.println("Defender country is " + toAttackCountry);
		// numbers of soldiers on toAttackCouuntry is set to 2
		toAttackCountry.setSoldiers(1);

		// This is for checking the condition if the defender has no soldier left in a country
		boolean isTrue = false;

		// Instantiate playerAttackWrapper
		playerAttackWrapper = new PlayerAttackWrapper(fromAttackCountry, toAttackCountry);

		// Set the number of attacker dices to 3
		playerAttackWrapper.setNumDiceAttacker(3);

		// Set the number of defender dices to 1
		playerAttackWrapper.setNumDiceDefender(1);
		
		// Call the attack function
		currentPlayer.attack(playerService, playerAttackWrapper);
		
		boolean isDefenderHasOwnership=currentPlayer.checkDefenderOwnership();
		// If the defender has no soldiers left in a country, isTrue is set to true
		if (isDefenderHasOwnership)
			isTrue = true;

		assertTrue(isTrue);
	}
	
	/**
	 * Tests if an attack condition is not valid
	 * In this test, the attacker's dice is set to above the allowed maximum
	 * The test passes if the validateAttackConditions() method returns false
	 * @throws Exception
	 */
	@Test
	public void test010_checkInvalidAttackConditions() throws Exception {
		System.out.println("Check invalid attack conditions");
		Player currentPlayer = playerService.getCurrentPlayer();

		// Get first country in player list
		Country fromAttackCountry = currentPlayer.getCountryList().get(0);

		// numbers of soldiers on fromAttackCouuntry is set to 4 to ensure that a valid
		// number of
		// dices can be thrown
		fromAttackCountry.setSoldiers(4);
		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromAttackCountry.getId());

		// Get first adjacent country in country's list
		Country toAttackCountry = null;
		while (toAttackCountry == null) {
			for (Integer i : fromCountryAdjacencyList) {
				if (!mapService.getCountryById(i).get().getPlayer().getName().equals(currentPlayer.getName())) {
					toAttackCountry = mapService.getCountryById(i).get();
					System.out.println("Defender country is " + toAttackCountry.getCountryName());
					break;
				}
			}
			if (toAttackCountry == null) {
				System.out.println("No adjacent defender country found. Retrying the set up.");
				setUp();
			}
		}

		// numbers of soldiers on toAttackCouuntry is set to 2 to ensure that a valid
		// number of
		// dices can be thrown
		toAttackCountry.setSoldiers(2);

		// Instantiate playerAttackWrapper
		playerAttackWrapper = new PlayerAttackWrapper(fromAttackCountry, toAttackCountry);

		// Set the number of attacker dices to 4, which is not a valid number of allowed attacker dice
		playerAttackWrapper.setNumDiceAttacker(4);

		// Set the number of defender dices to 1
		playerAttackWrapper.setNumDiceDefender(1);

		// Call the attack function
		currentPlayer.attack(playerService, playerAttackWrapper);

		assertFalse(currentPlayer.validateAttackConditions(playerService));
	}

	/**
	 * Method to load a map. Method first exits from editmapphase by sending command
	 * exitmapedit. Then command to loadmap is sent.
	 *
	 * @param mapName
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

	/**
	 * This method is to retry map loading and player placements
	 * It is called whenever there are no defender country found in the attacker country's adjacency list
	 * @throws Exception on invalid
	 */
	public void retrySetUp() throws Exception {
		createObjects();

		loadValidMap("luca.map");

		addPlayer("Keshav");
		addPlayer("Binsar");

		phaseViewTest.receiveCommand("populatecountries");
		phaseViewTest.receiveCommand("placeall");

		mapService.setState(GameState.ATTACK);

		Player player = playerService.getCurrentPlayer();
	}
}