package com6441.team7.riscTest.mapTest;

import java.util.Scanner;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.view.CommandPromptView;
import com6441.team7.risc.controller.MapLoaderController;
import com6441.team7.risc.controller.StateContext;

/**
 * 
 * MapEditorTest class tests cases relevant with performing commands in the map editor
 * 
 */
@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MapEditorTest {

	static CommandPromptView testView;
	static StateContext testState;
	static MapLoaderController testMapLoader;
	String testMap;
	Scanner keyboard = new Scanner(System.in);
	
	/**
	 * Method initialize() is called at the beginning
	 * to instantiate a CommandPromptView object.
	 */
	@BeforeClass
	public static void initializeEditorTest() {
		System.out.printf("Initialize map load test%n==========%n");
		testView = new CommandPromptView();
		testState = new StateContext();
		//testMapLoader = new MapLoaderController(testState);
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
	 * test1_inputMap() checks if map name input is not empty.
	 */
	@Test
	public void test1_inputMap() {
		System.out.printf("Testing map input.%nInput map name: ");
		//testMap = testView.readCommand();
		assertFalse(testMap.isEmpty());
		testView.displayMessage(testMap);
	}
	
	/**
	 * test2_executeCommand() tests if every entered command is valid.
	 */

	@Test
	public void test2_executeCommand() {
		System.out.printf("%nTesting editor commands. Input commands here:%n");
		String mapCommand = keyboard.nextLine();
		assertTrue(mapCommand.matches("editcontinent -add [a-zA-Z]+\\s+[0-9]+\\s+-remove [a-zA-Z]+"));
	}

	/**
	 * test3_saveMap() tests if map can be saved.
	 */
	@Ignore
	@Test
	public void test3_saveMap() {
		System.out.printf("%nTesting map saving%n");

	}
	
	/**
	 * test4_editMap() tests if map can be editted.
	 */
	@Ignore
	@Test
	public void test4_editMap() {
		System.out.printf("%nTesting map editing%n");

	}
	
	/**
	 * test5_validateMap() tests if map is valid.
	 */
	@Ignore
	@Test
	public void test5_validateMap() {
		System.out.printf("%nTesting map validation%n");

	}
	
	/**
	 * test6_validateContinent() tests if continent is valid.
	 */
	@Ignore
	@Test
	public void test6_validateContinent() {
		System.out.printf("%nTesting continent validation%n");

	}

}
