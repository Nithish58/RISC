package com6441.team7.risc.api.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com6441.team7.risc.controller.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.api.wrapperview.PlayerAttackWrapper;
import com6441.team7.risc.controller.MapLoaderAdapter;
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
	MapLoaderAdapter mapLoaderAdapter;
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
	 * Before every test is performed, the following are performed:
	 * Calling createObjects() method
	 * load a map using the loadValidMap() method
	 * Add two players
	 * Populate countries and place all of players' soldiers to the map
	 *
	 * @throws Exception on invalid
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
	 * Testing the single attack method.
	 * It tests the regular attack function (i.e.: without the allout param)
	 * The expected result is the number of either side's soldiers decrease by 1,
	 * depending on which side loses in the dice roll
	 * @throws Exception on invalid
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

		// Get a country in the adjacency list that does not belong to the attacker
		// If any of the opposing country does not exist in the adjacency list,
		// the setUp method will be run until any opposing country is found in the adjacency list 
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
	 * @throws Exception on invalid
	 */
	@Test
	public void test002_attackAllOut() throws Exception {
		System.out.println("Attack all out");
		Player currentPlayer = playerService.getCurrentPlayer();

		// Get first country in player list
		Country fromAttackCountry = currentPlayer.getCountryList().get(0);

		// numbers of soldiers on fromAttackCountry is set to 100 to ensure that a valid
		// number of
		// dices can be thrown
		fromAttackCountry.setSoldiers(100);
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

		// numbers of soldiers on toAttackCouuntry is set to 5 to ensure that a valid
		// number of
		// dices can be thrown
		toAttackCountry.setSoldiers(5);

		// This is for checking the condition after attack
		boolean isTrue = false;

		// Instantiate playerAttackWrapper
		playerAttackWrapper = new PlayerAttackWrapper(fromAttackCountry, toAttackCountry);

		//Set the allout condition to true
		playerAttackWrapper.setBooleanAllOut();

		// Call the attack function
		currentPlayer.attack(playerService, playerAttackWrapper);

		// If either one of the countries' loses a soldier, isTrue is set to true
		if (currentPlayer.isAttackerLastManStanding() || currentPlayer.defenderPushedOut())
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
	 * This checks if the attacker owns all the territory
	 * and is declared the winner
	 * @throws Exception on invalid on invalid value
	 */
	@Test
	public void test005_checkPlayerWinTheGame() throws Exception{
		System.out.println("Check if the attacker owns all countries and wins the entire game");
		Player currentPlayer = playerService.getCurrentPlayer();
		System.out.println("Attacker country list size: " + currentPlayer.getCountryList().size());
		System.out.println("Total country list  size: " + mapService.getCountries().size());

		// Get first country in player list
		Country fromAttackCountry = currentPlayer.getCountryList().get(0);
		
		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromAttackCountry.getId());

		Set<Country> countryList = mapService.getCountries();
		
		// Get a country in the adjacency list that does not belong to the attacker
		// If any of the opposing country does not exist in the adjacency list,
		// the setUp method will be run until any opposing country is found in the adjacency list 
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
		
		//Traverse through list of countries that belong to the defender
		//Every of the defender's countries that is not being targeted by the attacker
		//has the ownership of it transferred to the attacker
		for (Player p : playerService.getPlayerList()) {

			if (!p.getName().equals(fromAttackCountry.getPlayer().getName()))
				for (Country c : p.getCountryList()) {
					if (!c.getCountryName().equals(toAttackCountry.getCountryName())) {
					toAttackCountry.getPlayer().getCountryList().remove(c.getId());
					currentPlayer.getCountryList().add(c);
					System.out.println("Transfer "+c.getCountryName()+ " from "+p.getName());
					System.out.println();
					System.out.println(currentPlayer.getName()+" has "+currentPlayer.getCountryList().size()+" num of countries");
					}
					else
						System.out.println("Same country. It's "+c.getCountryName());
				}

		}
	
		
		// numbers of soldiers on fromAttackCouuntry is set to 1000 to ensure that a valid
		// number of
		// dices can be thrown and allout attack completes
		fromAttackCountry.setSoldiers(1000);
		
		toAttackCountry.setSoldiers(5);
		
		// Instantiate playerAttackWrapper
		playerAttackWrapper = new PlayerAttackWrapper(fromAttackCountry, toAttackCountry);

		// Call the attack function
		System.out.println(currentPlayer.getCountryList().size());
		System.out.println(playerService.getMapService().getCountries().size());
		currentPlayer.attack(playerService, playerAttackWrapper);

		
		//Regardless of the attack result, the ownership of the defender's last remaining country
		//will be transfered to the attacker
		toAttackCountry.getPlayer().getCountryList().remove(toAttackCountry.getCountryName());
		currentPlayer.getCountryList().add(toAttackCountry);
		
		System.out.println("Attacker country list size after transfer: " + currentPlayer.getCountryList().size());
		System.out.println("Total country list  size after: " + mapService.getCountries().size());

		// This is for checking the condition after attack
		boolean isTrue = false;

		System.out.println("Attacker country list size: " + currentPlayer.getCountryList().size());
		System.out.println("Total country list  size: " + mapService.getCountries().size());
		// If either one of the countries' loses a soldier, isTrue is set to true
		if (currentPlayer.checkPlayerWin())
			isTrue = true;

		assertTrue(isTrue);
	}

	/**
	 * Tests validate attack conditions
	 *
	 * @throws Exception on invalid
	 */
	@Test
	public void test006_validateAttackConditions() throws Exception {
		System.out.println("Validate attack conditions");
		Player currentPlayer = playerService.getCurrentPlayer();

		// Get first country in player list
		Country fromAttackCountry = currentPlayer.getCountryList().get(0);

		// numbers of soldiers on fromAttackCouuntry is set to 4 to ensure that a valid
		// number of
		// dices can be thrown
		fromAttackCountry.setSoldiers(25);
		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromAttackCountry.getId());

		// Get a country in the adjacency list that does not belong to the attacker
		// If any of the opposing country does not exist in the adjacency list,
		// the setUp method will be run until any opposing country is found in the adjacency list 
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

		// numbers of soldiers on toAttackCountry is set to 2 to ensure that a valid
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
	 * Testing if the attacker only has one soldier left in his/her country
	 * The test passes if the method isAttackerLastManStanding returns true
	 * @throws Exception on invalid value
	 */
	@Test
	public void test007_checkLastManStanding() throws Exception {
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

		// Get a country in the adjacency list that does not belong to the attacker
		// If any of the opposing country does not exist in the adjacency list,
		// the setUp method will be run until any opposing country is found in the adjacency list 
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
	 * @throws Exception on invalid 
	 */
	@Test
	public void test008_checkIsDefenderPushedOut() throws Exception {
		System.out.println("Attack all out");
		Player currentPlayer = playerService.getCurrentPlayer();

		// Get first country in player list
		Country fromAttackCountry = currentPlayer.getCountryList().get(0);

		// numbers of soldiers on fromAttackCouuntry is set to 100 to ensure that a valid
		// number of
		// dices can be thrown
		fromAttackCountry.setSoldiers(100);
		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromAttackCountry.getId());

		// Get a country in the adjacency list that does not belong to the attacker
		// If any of the opposing country does not exist in the adjacency list,
		// the setUp method will be run until any opposing country is found in the adjacency list 
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

		// numbers of soldiers on toAttackCouuntry is set to 1 to ensure that a valid
		// number of
		// dices can be thrown
		toAttackCountry.setSoldiers(1);

		// This is for checking the condition after attack
		boolean isTrue = false;

		// Instantiate playerAttackWrapper
		playerAttackWrapper = new PlayerAttackWrapper(fromAttackCountry, toAttackCountry);

		// Set the attack condition to allout
		playerAttackWrapper.setBooleanAllOut();

		// Set the number of attacker dices to 3
		playerAttackWrapper.setNumDiceAttacker(3);

		// Set the number of defender dices to 1
		playerAttackWrapper.setNumDiceDefender(1);

		// Call the attack function
		currentPlayer.attack(playerService, playerAttackWrapper);

		// If either one of the countries' loses a soldier, isTrue is set to true
		if (currentPlayer.defenderPushedOut())
			isTrue = true;

		assertTrue(isTrue);
	}
	
	/**
	 * Tests if an attack condition is not valid
	 * In this test, the attacker's dice is set to above the allowed maximum
	 * The test passes if the validateAttackConditions() method returns false
	 * @throws Exception on invalid
	 */
	@Test
	public void test009_checkInvalidNumDice() throws Exception {
		System.out.println("Check invalid dice number");
		Player currentPlayer = playerService.getCurrentPlayer();

		// Get first country in player list
		Country fromAttackCountry = currentPlayer.getCountryList().get(0);

		// numbers of soldiers on fromAttackCouuntry is set to 4 to ensure that a valid
		// number of
		// dices can be thrown
		fromAttackCountry.setSoldiers(4);
		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromAttackCountry.getId());

		// Get a country in the adjacency list that does not belong to the attacker
		// If any of the opposing country does not exist in the adjacency list,
		// the setUp method will be run until any opposing country is found in the adjacency list 
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
	 * Tests if an attack condition is not valid
	 * In this test, the attacker's dice is set to above the allowed maximum
	 * The test passes if the validateAttackConditions() method returns false
	 * @throws Exception on invalid
	 */
	@Test
	public void test010_checkInvalidNumSoldiers() throws Exception {
		System.out.println("Check invalid number of soldiers");
		Player currentPlayer = playerService.getCurrentPlayer();

		// Get first country in player list
		Country fromAttackCountry = currentPlayer.getCountryList().get(0);

		// numbers of soldiers on fromAttackCouuntry is set to 4 to ensure that a valid
		// number of
		// dices can be thrown
		fromAttackCountry.setSoldiers(1);
		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromAttackCountry.getId());

		// Get a country in the adjacency list that does not belong to the attacker
		// If any of the opposing country does not exist in the adjacency list,
		// the setUp method will be run until any opposing country is found in the adjacency list 
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

		// Set the number of attacker dices to 3, which is a valid number of allowed attacker dice
		playerAttackWrapper.setNumDiceAttacker(3);

		// Set the number of defender dices to 1
		playerAttackWrapper.setNumDiceDefender(1);

		// Call the attack function
		currentPlayer.attack(playerService, playerAttackWrapper);

		assertFalse(currentPlayer.validateAttackConditions(playerService));
	}
	
	/**
	 * Tests if country adjacency is invalid
	 * The test passes if the validateAttackConditions returns false
	 * @throws Exception on invalid
	 */
	@Test
	public void test011_checkInvalidCountryAdjacency() throws Exception {
		System.out.println("Check invalid country adjacency");
		Player currentPlayer = playerService.getCurrentPlayer();

		// Get first country in player list
		Country fromAttackCountry = currentPlayer.getCountryList().get(0);

		// numbers of soldiers on fromAttackCountry is set to 3 to ensure that a valid
		// number of
		// dices can be thrown
		fromAttackCountry.setSoldiers(3);
		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromAttackCountry.getId());

		//Set toAttackCountry to same country as fromAttackCountry
		Country toAttackCountry = fromAttackCountry;

		// numbers of soldiers on toAttackCountry is set to 5 to ensure that a valid
		// number of
		// dices can be thrown
		toAttackCountry.setSoldiers(5);

		// Instantiate playerAttackWrapper
		playerAttackWrapper = new PlayerAttackWrapper(fromAttackCountry, toAttackCountry);

		// Set the number of attacker dices to 3, which is a valid number of allowed attacker dice
		playerAttackWrapper.setNumDiceAttacker(3);

		// Set the number of defender dices to 1
		playerAttackWrapper.setNumDiceDefender(1);

		// Call the attack function
		currentPlayer.attack(playerService, playerAttackWrapper);

		assertFalse(currentPlayer.validateAttackConditions(playerService));
		System.out.println(phaseViewTest.getStrDisplayMessage());
	}
	
	/**
	 * Tests invalid country ownership
	 * The test passes if validateAttackConditions returns false
	 * @throws Exception on invalid
	 */
	@Test
	public void test012_invalidCountryOwnership() throws Exception {
		System.out.println("Validate attack conditions");
		Player currentPlayer = playerService.getCurrentPlayer();

		// Get first country in player list
		Country fromAttackCountry = currentPlayer.getCountryList().get(0);

		// numbers of soldiers on fromAttackCouuntry is set to 4 to ensure that a valid
		// number of
		// dices can be thrown
		fromAttackCountry.setSoldiers(25);
		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromAttackCountry.getId());

		// Get a country in the adjacency list that does not belong to the attacker
		// If any of the opposing country does not exist in the adjacency list,
		// the setUp method will be run until any opposing country is found in the adjacency list 
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

		// numbers of soldiers on toAttackCountry is set to 25 to ensure that a valid
		// number of
		// dices can be thrown
		toAttackCountry.setSoldiers(25);	
		//The owner of the defender's country is set to the attacker
		toAttackCountry.setPlayer(fromAttackCountry.getPlayer());

		// Instantiate playerAttackWrapper
		// Swapped attacker and defender so that ownership changes, but all other conditions are valid
		//such as adjacency num dice and num of soldiers
		playerAttackWrapper = new PlayerAttackWrapper(toAttackCountry, fromAttackCountry);

		// Set the number of attacker dices to 3
		playerAttackWrapper.setNumDiceAttacker(3);

		// Set the number of defender dices to 1
		playerAttackWrapper.setNumDiceDefender(1);

		// Call the attack function
		currentPlayer.attack(playerService, playerAttackWrapper);

		assertFalse(currentPlayer.validateAttackConditions(playerService));
	}
	
	/**
	 * Tests if attackmove is required after conquering a country
	 * The test passes if the result returns true
	 * @throws Exception on invalid
	 */
	@Test
	public void test013_validateMoveAfterConquer() throws Exception{
		System.out.println("Validate move after conquer");
		Player currentPlayer = playerService.getCurrentPlayer();

		// Get first country in player list
		Country fromAttackCountry = currentPlayer.getCountryList().get(0);

		// numbers of soldiers on fromAttackCouuntry is set to 1000 to ensure that a valid
		// number of
		// dices can be thrown and increasing chance of defender being defeated after 1 attack
		fromAttackCountry.setSoldiers(1000);
		Set<Integer> fromCountryAdjacencyList = mapService.getAdjacencyCountries(fromAttackCountry.getId());

		// Get a country in the adjacency list that does not belong to the attacker
		// If any of the opposing country does not exist in the adjacency list,
		// the setUp method will be run until any opposing country is found in the adjacency list 
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

		// numbers of soldiers on toAttackCouuntry is set to 1 to ensure that a valid
		// number of
		// dices can be thrown and increasing chance of defender being defeated after 1 attack
		toAttackCountry.setSoldiers(1);

		// This is for checking the condition after attack
		boolean isTrue = false;

		// Instantiate playerAttackWrapper
		playerAttackWrapper = new PlayerAttackWrapper(fromAttackCountry, toAttackCountry);
		
		// Set the attack condition to allout
		playerAttackWrapper.setBooleanAllOut();

		// Set the number of attacker dices to 3
		playerAttackWrapper.setNumDiceAttacker(3);

		// Set the number of defender dices to 1
		playerAttackWrapper.setNumDiceDefender(1);

		// Call the attack function
		currentPlayer.attack(playerService, playerAttackWrapper);

		// If attackmove is required, isTrue is set to true
		if (currentPlayer.getBoolAttackMoveRequired())
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

		phaseViewTest.receiveCommand("loadmap " + mapName); // Load ameroki map
	}

	/**
	 * Method that sends command to add a player
	 *
	 * @param name of player
	 */
	public void addPlayer(String name) {
		phaseViewTest.receiveCommand("gameplayer -add " + name+" human");
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

		mapLoaderAdapter = new MapLoaderAdapter(mapService);
		startupGameController = new StartupGameController(mapLoaderAdapter, playerService);
		reinforceGameController = new ReinforceGameController(playerService);
		fortifyGameController = new FortifyGameController(playerService);
		attackController = new AttackGameController(playerService);

		controllerList.add(mapLoaderAdapter);
		controllerList.add(startupGameController);
		controllerList.add(reinforceGameController);
		controllerList.add(fortifyGameController);
		controllerList.add(attackController);

		phaseViewTest.addController(controllerList);

		mapLoaderAdapter.setView(phaseViewTest);
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