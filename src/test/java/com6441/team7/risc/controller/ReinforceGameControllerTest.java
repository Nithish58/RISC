package com6441.team7.risc.controller;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.api.model.Card;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.view.CommandPromptView;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReinforceGameControllerTest {
	static ReinforceGameController reinforceController1, reinforceController2;
	static Player playerDummy1, playerDummy2;
	static StartupGameController startup;
	static MapLoaderController mapController;
	static MapService mapService;
	static CommandPromptView cmdView; 
	static ArrayList<Player> players;
	static AtomicBoolean phase;
	static int testNo; //this is for determining which params to be used for certain test cases
	String file, loadmapcmd;
	String cmd="";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mapService = new MapService();
		mapController = new MapLoaderController(mapService);
		cmdView = new CommandPromptView(mapController, new GameController(mapController, mapService));
		mapController.setView(cmdView);
		players = new ArrayList<>();
		startup = new StartupGameController(mapController, mapService, players);
		phase = new AtomicBoolean(false);
		testNo = 0;
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		URI uri = getClass().getClassLoader().getResource("ameroki.map").toURI(); 
		file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
		mapController.parseFile(file);
		startup.readCommand("loadmap ameroki.map", phase);
		playerDummy1 = new Player("John Doe");
		playerDummy2 = new Player("Jane Doe");
		players.add(playerDummy1);
		players.add(playerDummy2);
		startup.readCommand("populatecountries", phase);
		startup.readCommand("showmap", phase);
		//startup.readCommand("placearmy siberia", phase);
		startup.readCommand("placeall", phase);
		cmd="reinforce siberia 7";
		reinforceController1 = new ReinforceGameController(playerDummy1, mapService, startup, cmd,cmdView);
		reinforceController1.getReinforcedArmiesCount();

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test001_getReinforcedArmiesCount() {
		assertSame(7, reinforceController1.getReinforcedArmiesCountVal());
	}
	
	

}
