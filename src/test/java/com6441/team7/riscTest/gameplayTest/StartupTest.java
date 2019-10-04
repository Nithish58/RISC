package com6441.team7.riscTest.gameplayTest;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.*;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.controller.MapLoaderController;
import com6441.team7.risc.controller.StateContext;
import com6441.team7.risc.view.CommandPromptView;

/**
 * 
 * StartupTest class tests cases relevant with startup phase.
 * 
 */
@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StartupTest {
	static CommandPromptView testView;
	StateContext testState;
	MapLoaderController testMapLoader;
	String testMap, playerManagementCommand, armyPlacementCommand;
	Scanner keyboard = new Scanner(System.in);
	
	/**
	 * Method initializeStartupTest() is called at the beginning
	 * to create a CommandPromptView object.
	 */
	@BeforeClass
	public static void initializeStartupTest() {
		System.out.printf("Initialize startup test%n==========%n");
		testView = new CommandPromptView();
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
	 * test1_loadMap() tests if map can be loaded.
	 */
	@Test
	public void test1_loadMap() {
		System.out.printf("Testing map load.%nEnter command here: ");
		//testMap = testView.readCommand();
		assertTrue(testMap.matches("loadmap [a-zA-Z0-9]+"));
		testView.displayMessage(testMap);
	}
	
	/**
	 * test3_saveMap() tests player management commands.
	 */
	@Test
	public void test2_managePlayers() {
		System.out.printf("Testing player management.%nEnter command here: ");
		playerManagementCommand = keyboard.nextLine();
	}
	
	/**
	 * test3_saveMap() tests army placement commands.
	 */
	@Test
	public void test3_placingArmies() {
		System.out.printf("Testing army placements.%nEnter command here: ");
		playerManagementCommand = keyboard.nextLine();
	}

}
