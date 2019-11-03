package com6441.team7.risc.api.model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * This is the test class for storing player information.
 *
 */
public class PlayerTest {
	static Player testPlayer;
	String testName;
	int testArmies;
	List<Card> testCardList;
	int tradeInTimes;
	static final int CARD_CATEGORY_NUMBER = 3;
	int armyNum;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Setting player's name.
	 * Instantiating a player object.
	 * Setting number of armies.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		testName = "Player One";
		testPlayer = new Player(testName);
		armyNum = 3;
		testPlayer.setArmies(armyNum);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Testing player name
	 */
	@Test
	public void test001_getName() {
		assertEquals(testName, testPlayer.getName());
	}
	
	/**
	 * Testing getArmies
	 */
	@Test
	public void test002_getArmies() {
		assertEquals(armyNum, testPlayer.getArmies());
	}

	/**
	 * Testing attacker dice roll
	 */
	@Test
	public void test004_rollAttackerDice() {
		assertNotEquals(0, testPlayer.rollAttackerDice(3));
	}
}
