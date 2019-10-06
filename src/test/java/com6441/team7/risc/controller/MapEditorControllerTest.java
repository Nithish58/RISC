package com6441.team7.risc.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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

	static private CommandPromptView testCmdView;
	static private StateContext testState;
	static private MapLoaderController testMapLoader;
	private String editorCommand;
	private Integer expectedResult;
	String testMap;

	/**
	 * Set up parameters for editor commands
	 */
	public MapEditorControllerTest(String editorCommand, int expectedResut ) {
		this.editorCommand = editorCommand;
		this.expectedResult = expectedResut;
	}
	/**
	 * editorCommands contains list of commands for param input.
	 */
	@Parameterized.Parameters
	public static Collection editorCommands() {
		return Arrays.asList(new Object[][]{
			{"editcontinent -add Nord_Asia 1", 7},
			{"editcontinent -add Southeast_Asia 1 -add Northeast_Asia 1", 8},
			{"editcontinent -remove Nr", 6},
			{"editcontinent -add South_Asia 7 -remove Nr", 7},
			{"editcountry -add Nordenstan Nord_Asia", 43},
			{"editcountry -add Columbia ameroki", 44},
			{"editcountry -add Eurasia_Kingdom Nord_Asia -add Sotoa Northeast_Asia", 46},
			{"editcountry -remove worrick", 45},
			{"editcountry -remove Osea", 45},
		});
	}
	

	@BeforeClass
	public static void beginClass() {
		testCmdView = new CommandPromptView();
		testState = new StateContext();
		testMapLoader = new MapLoaderController(testState, testCmdView);
		
	}
	
	/**
	 * beginMethod() is called before every method is performed.
	 */
	@Before
	public void beginMethod() {
		System.out.printf("==========%nBeginning of method%n==========%n");
		System.out.println("Number of continents before test: "+testMapLoader.getMapService().getContinents().size());
		System.out.println("Number of countries before test: "+testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * endMethod() is called after every method is performed.
	 */
	@After
	public void endMethod() {
		System.out.printf("%n%n==========%nEnd of method%n==========%n");
		System.out.println("Number of continents after test: "+testMapLoader.getMapService().getContinents().size());
		System.out.println("Number of countries after test: "+testMapLoader.getMapService().getCountries().size());
	}
	
	
	/**
	 * test1_readFile() tests command to load map from file.
	 */

	@Test
	public void test1_readFile() throws Exception{
		System.out.printf("%nTesting readFile method.%n");
		URI uri = getClass().getClassLoader().getResource("ameroki.map").toURI(); 
		//readFile contains the file content and will check if the file exists.
		String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
		boolean readFile = testMapLoader.parseFile(file);
		System.out.println(testMapLoader.getMapService().getContinentCountriesMap());
		assertTrue(readFile);
		assertFalse(!readFile);
	}
	
	/**
	 * test2_editMap() tests the editmap command.
	 */
	@Test
	public void test2_editMap() throws Exception{
		System.out.printf("Testing editmap command.%n");
		URI uri = getClass().getClassLoader().getResource("ameroki.map").toURI(); 
		String inputCommand = "editmap "+uri;
		System.out.println(inputCommand);
		//Execute editmap command.
		Optional<String> inputMap = testMapLoader.editMap(inputCommand);
		assertTrue(inputMap.isPresent());		
	}
	
	/**
	 * test3_editMapCommands() tests the map editor commands from parameterized commands taken from a list.
	 */
	@Test
	public void test3_editCommands() throws Exception{
		System.out.printf("Testing map editor commands.%n");
		System.out.println(testMapLoader.getMapService().getContinentCountriesMap());
		System.out.println(testMapLoader.getMapService().getCountries());
		System.out.println(editorCommand);
		String editorOption = StringUtils.substringBefore(editorCommand, "-");
		editorCommand = StringUtils.substringAfter(editorCommand, "-");
		String[] editorCommands = StringUtils.split(editorCommand, "-");
		if (editorOption.contains("editcontinent")) {
		testMapLoader.editContinents(editorCommands);
		assertSame(expectedResult, testMapLoader.getMapService().getContinents().size());
		}
		if (editorOption.contains("editcountry")) {
		testMapLoader.editCountries(editorCommands);
		assertSame(expectedResult, testMapLoader.getMapService().getCountries().size());
		}
	}
	
	/**
	 * test4_validateMap() tests if map is valid.
	 */
	@Ignore
	@Test
	public void test5_validateMap() {
		System.out.printf("%nTesting map validation%n");
	}
	
	/**
	 * test4_saveMap() tests if map can be saved.
	 */
	@Ignore
	@Test
	public void test4_saveMap() {
		System.out.printf("%nTesting map saving%n");
		try {
			testMapLoader.saveMap("savemap");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
