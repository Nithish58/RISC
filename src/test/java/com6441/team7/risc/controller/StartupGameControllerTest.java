package com6441.team7.risc.controller;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.view.CommandPromptView;

/**
 * 
 * This test class handles the startup phase of the game.
 * It has following fields:
 * <ul>
 * <li>startupGameController startup
 * <li>static MapLoaderController mapController
 * <li>static MapService mapService
 * <li>static CommandPromptView cmdView
 * <li>ArrayList<Player> players
 * <li>GameController game
 * <li>static int testNo
 * <li>String file
 * <li>loadmapcmd
 * </ul>
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StartupGameControllerTest {
	StartupGameController startup;
	static MapLoaderController mapController;
	static MapService mapService;
	static CommandPromptView cmdView; 
	static GameController game;
	ArrayList<Player> players;
	String file, loadmapcmd;

	/**
	 * Upon the creation of the class, MapService, MapLoaderController,
	 * GameController, and CommandPromptView objects are instantiated.
	 * The MapController and GameController objects also set the game view to the CommandPromptView object.
	 * @throws Exception on invalid value
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mapService = new MapService();
		mapController = new MapLoaderController(mapService);
		game = new GameController(mapController, mapService);
		cmdView = new CommandPromptView(mapController, game);
		mapController.setView(cmdView);
		game.setView(cmdView);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Before any test method is invoked, the followings are defined:
	 * <ul>
	 * <li>URI variable <i>uri</i> retrieve map file from the resource directory.
	 * <li><i>file</i> reads the file retrieved by <i>uri</i> to String using UTF-8.
	 * <li><i>mapController</i> parses <i>file</i> using the <i>parseFile()</i> method.
	 * <li><i>startup</i> is an retrieved using the getStartupController() method
	 * in the game object.
	 * <li><i>loadmapcmd contains the loadmap command.</i>
	 *  </ul>
	 * @throws Exception on invalid value
	 */
	@Before
	public void setUp() throws Exception {
		URI uri = getClass().getClassLoader().getResource("ameroki.map").toURI(); 
		file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
		mapController.parseFile(file);
		startup = game.getStartupController();
		loadmapcmd = "loadmap ameroki.map"; //this loads the ameroki.map file for testing
	}
	/**
	 * This method is executed after every test method is performed.
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * This test case edit loadmap command with 'loadmap ameroki.map' as the 
	 * test param. It is passed if it returns Optional.of(map file)
	 */
	@Test
	public void test001_loadmap() {
		assertEquals(Optional.of("ameroki.map"),startup.loadMap(loadmapcmd));
	}

}
