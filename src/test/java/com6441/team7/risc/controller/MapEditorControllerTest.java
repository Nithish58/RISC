package com6441.team7.risc.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.view.CommandPromptView;
import com6441.team7.risc.controller.MapLoaderController;
import com6441.team7.risc.controller.StateContext;

/**
 * 
 * MapEditorTest class tests cases relevant with the controller components of the map editor.
 * @author Binsar Hutapea
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class MapEditorControllerTest {

	private CommandPromptView testCmdView;
	private StateContext testState;
	private MapLoaderController testMapLoader;
	private String editorCommand;
	private Boolean expectedResult;
	String testMap;

	/**
	 * Set up parameters for editor commands
	 */
	public MapEditorControllerTest(String editorCommand, Boolean expectedResut ) {
		this.editorCommand = editorCommand;
		this.expectedResult = expectedResut;
	}
	/**
	 * editorCommands contains list of commands for param input.
	 */
	@Parameterized.Parameters
	public static Collection editorCommands() {
		return Arrays.asList(new Object[][]{
			{"editcontinent -add Asia 7", true},
			{"editcontinent -add Australia 8", true},
			{"editcontinent -remove Asia", true},
			{"editcontinent -add Asia 7 -remove Australia", true},
			{"editcontinent -delete Asia", false},
			{"showmap", true},
			{"validatemap", true}
		});
	}
		
	/**
	 * beginMethod() is called before every method is performed.
	 */
	@Before
	public void beginMethod() {
		System.out.printf("==========%nBeginning of method%n==========%n");
		System.out.printf("Initialize map load test%n==========%n");
		testCmdView = new CommandPromptView();
		testState = new StateContext();
		testMapLoader = new MapLoaderController(testState, testCmdView);
	}
	
	/**
	 * endMethod() is called after every method is performed.
	 */
	@After
	public void endMethod() {
		System.out.printf("%n%n==========%nEnd of method%n==========%n");
	}
	
	
	/**
	 * test1_readFile() tests command to load map from file.
	 */

	@Test
	public void test1_readFile() throws Exception{
		System.out.printf("%nTesting readFile method.%n");
		//readFile contains the file content and will check if the file exists.
		Optional<String> readFile = testMapLoader.readFile("/home/binsar/MACS/Fall_2019/Advanced_Programming_Practices/RISK_project/RISC/src/test/resources/ameroki.map");
		assertTrue(readFile.isPresent());
		assertFalse(readFile.get().isEmpty());
	}
	
	
	/**
	 * test2_editMap() tests the map editor commands from parameterized commands taken from a list.
	 */
	@Test
	public void test2_editMap() throws Exception{
		System.out.printf("Testing map editor commands.%n");
		String inputCommand = "editmap "+"/home/binsar/MACS/Fall_2019/Advanced_Programming_Practices/RISK_project/RISC/src/test/resources/ameroki.map";
		Optional<String> inputMap = testMapLoader.editMap(inputCommand);
		assertEquals(expectedResult, inputMap.isPresent());
		//testMapLoader object calls method to load a map
		System.out.println(editorCommand);
		Optional<String> editMap = testMapLoader.editMap(editorCommand);
		assertEquals(expectedResult, editMap.isPresent());
		
	}
	

	/**
	 * test4_saveMap() tests if map can be saved.
	 */
	@Ignore
	@Test
	public void test4_saveMap() {
		System.out.printf("%nTesting map saving%n");

	}
	

	/**
	 * test5_validateContinent() tests if continent is valid.
	 */
	@Ignore
	@Test
	public void test5_validateContinent() {
		System.out.printf("%nTesting continent validation%n");

	}

}
