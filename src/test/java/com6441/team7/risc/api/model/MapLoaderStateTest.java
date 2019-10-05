package com6441.team7.risc.api.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.api.model.MapLoaderState;
import com6441.team7.risc.controller.StateContext;

/**
 * 
 * MapStateTest class tests the methods in MapLoaderState class.
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MapLoaderStateTest {

	static MapLoaderState testMapLoaderState;
	static StateContext testStateContext;
	
	@BeforeClass
	public static void initMapStateTest() {
	System.out.printf("Initialize map load test%n==========%n");
	testMapLoaderState = new MapLoaderState();
	testStateContext = new StateContext();
	}
	
	/**
	 * beginMethod() is called before every method is performed.
	 */
	@Before
	public void beginMethod() {
		System.out.printf("==========%nBeginning of method%n==========%n");
	}
	
	/**
	 * endMethod() is called after every method is performed.
	 */
	@After
	public void endMethod() {
		System.out.printf("%n%n==========%nEnd of method%n==========%n");
	}
	
	@Test
	public void test1_toString() {
		String mapLoader = testMapLoaderState.toString();
		System.out.println(mapLoader);
		assertEquals("mapLoader", mapLoader);
	}

}
