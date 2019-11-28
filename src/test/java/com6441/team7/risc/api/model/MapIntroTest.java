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

	static
	/**
	 * MapIntro object reference
	 */
	MapIntro mapIntro;
	/**
	 * MapIntro test variable
	 */
	String mapIntroStr = "";

	/**
	 * MapIntro object is instantiated here.
	 * @throws Exception on invalid
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mapIntro = new MapIntro();
	}

	/**
	 * Before every test, map intro is set.
	 * @throws Exception on invalid
	 */
	@Before
	public void setUp() throws Exception {
		mapIntroStr = "intro";
		mapIntro.setMapIntro(mapIntroStr);
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
