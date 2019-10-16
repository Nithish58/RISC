package com6441.team7.risc.controller;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.view.CommandPromptView;

public class FortifyGameControllerTest {
	static MapService mapService;
	static private CommandPromptView cmdView;
	Player player1;
	fortifyGameController fortifyController;
	fortifyGameController skippedFortifyController;
	Optional<Country> fromCountry;
	Optional<Country> toCountry;
	GameState nextState;
	String fortifyCommand1, fortifyCommand2, file;
	static MapLoaderController mapCtrl;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mapService = new MapService();
		mapCtrl = new MapLoaderController(mapService);
		cmdView = new CommandPromptView(mapCtrl, new GameController(mapCtrl, mapService));
		mapCtrl.setView(cmdView);

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		URI uri = getClass().getClassLoader().getResource("ameroki.map").toURI(); 
		file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
		player1 = new Player("Player_1");
		fortifyController = new fortifyGameController(player1, mapService);
		skippedFortifyController = new fortifyGameController(player1, mapService);
		//the following two lines are to be changed
		mapCtrl.parseFile(file);
		nextState = GameState.REINFORCE;
		
		
		fortifyCommand1 = "fortify Siberia Worrick 1";
		fortifyCommand2 = "fortify none";
		//fortifyController.readCommand(fortifyCommand1);
		skippedFortifyController.readCommand(fortifyCommand2);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void test001_fortifyCountry() {
		//assertEquals(1, fortifyController.);
	}
	
	@Test
	public void test002_skipFortification() {
		assertEquals(nextState, skippedFortifyController.fortifyState);
	}

}
