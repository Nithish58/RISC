package com6441.team7.riscTest.gameplayTest;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.*;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.controller.GameController;
import com6441.team7.risc.controller.StateContext;
import com6441.team7.risc.view.CommandPromptView;

/**
 * 
 * FortificationTest class tests cases relevant with fortification phase.
 * 
 */
@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FortificationTest {
	static CommandPromptView testView;
	static GameController game;
	static StateContext testState;
	
	String fortificationCommand;
	Scanner keyboard = new Scanner(System.in);
	
	/**
	 * Method initializeFortificationTest() is called at the beginning
	 * to create a CommandPromptView object.
	 */
	@BeforeClass
	public static void initializeFortificationTest() {
		System.out.printf("Initialize fortification test%n==========%n");
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
	 * test1_test1_fortifyingArmies() tests fortification commands.
	 */
	@Test
	public void test1_fortifyingArmies() {
		System.out.printf("Testing army fortification commands.%nEnter command here: ");
		fortificationCommand = keyboard.nextLine();
		assertTrue(fortificationCommand.matches("fortify [a-zA-Z0-9]+\\s+[]a-zA-Z0-9]+\\s+[0-9]+"));
	}

}
