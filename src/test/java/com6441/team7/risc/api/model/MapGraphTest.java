package com6441.team7.risc.api.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * This is a test class for MapGraph
 *
 */
public class MapGraphTest {
	static //declare object class
	MapGraph mapGraph;
	//declare variable for testing
	String testGraph = "";
	
	/**
	 * Instantiate a MapGraph object
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mapGraph = new MapGraph();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	/**
	 * Assigning a value to the variable.
	 * Invoke setMapGraph with testGraph as param.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		testGraph = "Test";
		mapGraph.setMapGraph(testGraph);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test if getMapGraph returns the same value 
	 * as the one set with setMapGraph
	 */
	@Test
	public void test() {
		assertEquals(testGraph, mapGraph.getMapGraph());
	}

}
