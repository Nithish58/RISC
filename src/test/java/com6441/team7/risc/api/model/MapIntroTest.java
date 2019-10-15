package com6441.team7.risc.api.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * Test class for storing basic information of map.
 *
 */
public class MapIntroTest {
	static MapIntro mapIntro;
	String mapIntroStr = "";

	/**
	 * MapIntro object is instantiated here.
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mapIntro = new MapIntro();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		mapIntroStr = "intro";
		mapIntro.setMapIntro(mapIntroStr);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test if getMapIntro returns the same value 
	 * as the one set with setMapIntro
	 */
	@Test
	public void test() {
		assertEquals(mapIntroStr, mapIntro.getMapIntro());
	}

}
