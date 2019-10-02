package com6441.team7.riscTest.gameplayTest;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.controller.GameController;
import com6441.team7.risc.controller.StateContext;
import com6441.team7.risc.view.CommandPromptView;

/**
 * 
 * ReinforcementTest class tests cases relevant with reinforcement phase.
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReinforcementTest {
	static CommandPromptView testView;
	static GameController game;
	static StateContext testState;
	
	String reinforcementCommand;
	Scanner keyboard = new Scanner(System.in);
	
	/**
	 * Method initializeReinforcementTest() is called at the beginning
	 * to create a CommandPromptView object.
	 */
	@BeforeClass
	public static void initializeReinforcementTest() {
		System.out.printf("Initialize reinforcement test%n==========%n");
		testView = new CommandPromptView();
		testState = new StateContext();
		game = new GameController(testState);
	}
	
	/**
	 * beginMethod() is called before every method is performed.
	 */
	@Before
	public void beginMethod() {
		System.out.printf("==========%nBeginning of method%n==========%n");
	}
	
	/**
	 * endMethod() is called after every method is performed.
	 */
	@After
	public void endMethod() {
		System.out.printf("%n%n==========%nEnd of method%n==========%n");
	}
	
	/**
	 * test1_test1_reinforcingArmies() tests reinforcement commands.
	 */
	@Test
	public void test1_reinforcingArmies() {
		System.out.printf("Testing army reinforcements.%nEnter command here: ");
		reinforcementCommand = keyboard.nextLine();
		assertTrue(reinforcementCommand.matches("reinforce [a-zA-Z0-9]+\\s+[0-9]+"));
	}

}
