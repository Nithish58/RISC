package com6441.team7.risc.controller;

import static org.junit.Assert.*;

import java.util.Optional;

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

public class FortifyGameControllerTest {
	MapService mapService;
	Player player1;
	fortifyGameController fortifyController;
	fortifyGameController skippedFortifyController;
	Optional<Country> fromCountry;
	Optional<Country> toCountry;
	GameState nextState;
	String fortifyCommand1, fortifyCommand2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		player1 = new Player("Player_1");
		mapService = new MapService();
		fortifyController = new fortifyGameController(player1, mapService);
		skippedFortifyController = new fortifyGameController(player1, mapService);
		//fortifyController.fortify();
		nextState = GameState.REINFORCE;
		
		fortifyCommand1 = "fortify country_A country_B 1";
		fortifyCommand2 = "fortify none";
		fortifyController.readCommand(fortifyCommand1);
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
