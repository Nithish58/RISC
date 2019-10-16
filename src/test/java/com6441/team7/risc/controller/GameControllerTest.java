package com6441.team7.risc.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
 * This test class handles gamecontroller testing.
 * It has following fields:
 * <ul>
 * <li>static GameController game
 * <li>static MapLoaderController mapController;
 * <li>static MapService mapService;
 * <li>static CommandPromptView cmdView; 
 * <li>ArrayList<Player> players;
 * <li>static int testNo;
 * <li>String file, loadmapcmd;
 * </ul>
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GameControllerTest {
	static GameController game;
	static MapLoaderController mapController;
	static MapService mapService;
	static CommandPromptView cmdView; 
	ArrayList<Player> players;
	static int testNo; //this is for determining which params to be used for certain test cases
	String file, loadmapcmd;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mapService = new MapService();
		mapController = new MapLoaderController(mapService);
		cmdView = new CommandPromptView(mapController, new GameController(mapController, mapService));
		mapController.setView(cmdView);
		testNo = 0;
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		game = new GameController(mapController, mapService);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
