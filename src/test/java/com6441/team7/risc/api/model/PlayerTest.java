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
	 * Setting player's name. Instantiating a player object. Setting number of
	 * armies.
	 * 
	 * @throws Exception
	 */
	/*
	 * @Before public void setUp() throws Exception { testName = "Player One";
	 * testPlayer = new Player(testName); armyNum = 3;
	 * testPlayer.setArmies(armyNum);
	 * 
	 * }
	 * 
	 * @After public void tearDown() throws Exception { }
	 * 
	 *//**
		 * Testing player name
		 */
	/*
	 * @Test public void test001_getName() { assertEquals(testName,
	 * testPlayer.getName()); }
	 * 
	 *//**
		 * Testing getArmies
		 */
	/*
	 * @Test public void test002_getArmies() { assertEquals(armyNum,
	 * testPlayer.getArmies()); }
	 * 
	 *//**
		 * Testing attacker dice roll
		 *//*
			 * @Test public void test003_rollAttackerDice() { assertNotEquals(0,
			 * testPlayer.rollAttackerDice(3)); }
			 */

	/**
	 * Testing attack method
	 */
	@Test
	public void test004_attack() {

	}

	/**
	 * Testing the single attack
	 */
	@Test
	public void test005_attackSingle() {

	}

	/**
	 * Testing attack until soldiers from either attacker or defender is out
	 */
	@Test
	public void test006_attackAllOut() {

	}

	/**
	 * Testing rolling attacker's dice
	 */
	@Test
	public void test007_rollAttackerDice() {

	}

	/**
	 * testing rolling defender's dice
	 */
	@Test
	public void test008_rollDefenderDice() {

	}

	/**
	 * Testing result of deciding battle
	 */
	@Test
	public void test009_decideBattleResult() {

	}

	/**
	 * Testing validate attack conditions
	 */
	@Test
	public void test010_validateAttackConditions() {

	}

	/**
	 * Testing if attacker country actually belongs to the attacker
	 */
	@Test
	public void test010_checkCountryBelongToAttacker() {

	}

	/**
	 * Testing whether the 2 countries are owned by different players
	 */
	@Test
	public void test011_checkCountryHostility() {

	}

	/**
	 * testing the number of soldiers for the attacker
	 */
	@Test
	public void test012_checkNumAttackingSoldiers() {

	}

	/**
	 * Testing if attacker throws a valid number of dices
	 */
	@Test
	public void test013_checkAttackerMaxDiceNumValidity(){

	}



}
