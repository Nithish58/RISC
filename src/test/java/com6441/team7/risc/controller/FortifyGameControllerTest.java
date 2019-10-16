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
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.view.CommandPromptView;

/**
 * 
 * This test class handles fortification phase
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FortifyGameControllerTest {
	static MapService mapService;
	static private CommandPromptView cmdView;
	Player player1;
	FortifyGameController fortifyController;
	FortifyGameController skippedFortifyController;
	Country fromCountry;
	Country toCountry;
	GameState nextState;
	String fortifyCommand1, fortifyCommand2, file;
	private int expectedSoldiers;
	static MapLoaderController mapCtrl;
	static int testNo;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mapService = new MapService();
		mapCtrl = new MapLoaderController(mapService);
		cmdView = new CommandPromptView(mapCtrl, new GameController(mapCtrl, mapService));
		mapCtrl.setView(cmdView);
		testNo = 0;

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		URI uri = getClass().getClassLoader().getResource("ameroki.map").toURI(); 
		file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
		player1 = new Player("Player_1");
		
		// (BY KESHAV) fortifyController = new fortifyGameController(player1, mapService); 
		//skippedFortifyController = new fortifyGameController(player1, mapService);
		//  (BY KESHAV) the following two lines are to be changed
		
		mapCtrl.parseFile(file);
		nextState = GameState.REINFORCE;
		
		
		fortifyCommand1 = "fortify Siberia Worrick 1";
		fortifyCommand2 = "fortify none";
		expectedSoldiers = 1;
		fromCountry = mapService.getCountryByName("siberia").get();
		fromCountry.setSoldiers(3);
		toCountry = mapService.getCountryByName("worrick").get();
		switch(testNo) {
		case 0:
			fortifyController.readCommand(fortifyCommand1);
			break;
		case 1:
			skippedFortifyController.readCommand(fortifyCommand2);
			break;
		}
	}

	@After
	public void tearDown() throws Exception {
		testNo++;
	}

	@Test
	public void test001_fortifyCountry() {
		assertSame(expectedSoldiers, toCountry.getSoldiers());
		assertEquals(nextState, fortifyController.fortifyState);
	}
	
	@Test
	public void test002_skipFortification() {
		assertEquals(nextState, skippedFortifyController.fortifyState);
	}

}
