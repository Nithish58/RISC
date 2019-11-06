package com6441.team7.risc.api.model;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runners.MethodSorters;

/**
 * Test class for GameState enum
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GameStateTest {
	/**
	 * an object of GameState class
	 */
	GameState gameState;

	/**
	 * value of different gameState
	 */
	private String name;

	/**
	 * Context: name = reinforce
	 * Method call: enum gamestate
	 * Evaluation: check if name is equivalent to gameState.REINFORCE
	 */
	@Test
	public void test001_Setter(){
		name = "reinforce";
		GameState name = GameState.REINFORCE;
		assertEquals(name,GameState.REINFORCE);
	}

}
