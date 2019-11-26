package com6441.team7.risc.api.model;

import static org.junit.Assert.*;

import java.util.Objects;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * This class will test Continent class
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContinentTest {
	/**
	 * id of continent in map
	 */
	private int id;

	/**
	 * Continent's name in world map
	 */
	private String name;

	/**
	 * Continent's expected Name in world map
	 */
	private String expectedName;

	/**
	 * Continent's value in world map
	 */
	private int continentValue;

	/**
	 *color of continent in world map
	 */
	private String color;

	/**
	 * Hashid of continent
	 */
	private int hashId;

	/**
	 * Continent object which keep track of each continent
	 */
	private Continent continent;

	/**
	 * To initialize Test for Continent class
	 */
	@BeforeClass
	public static void initClass() {
		System.out.println("Initialize Continent Test");
	}

	/**
	 * Begin of Test for Continent class
	 */
	@Before
	public void beginTest() {
		System.out.println("Begin Test");
	}

	/**
	 * End of Test for Continent class
	 */
	@After
	public void endTest() {
		System.out.println("End Test");
	}


	@AfterClass
	public static void endClass() {
	}

	/**
	 * Context: Initializing the continent with name, value, id
	 * Method call: get name
	 * Evaluation: check id, name
	 */
	@Test
	public void test1_firstConstructor() {
		id = 42;
		name = "Balkan";
		expectedName = name.toLowerCase();
		continent = new Continent(id, name);
		String result = continent.getName();
		assertEquals(id, continent.getId());
		assertTrue(result.equals(expectedName));
	}

	/**
	 * Context: Initializing the continent with name, value, id
	 * Method call: get name
	 * Evaluation: check id, name. and value is correctly for new continent
	 */
	@Test
	public void test2_secondConstructor() {
		//context
		id = 43;
		name = "Scandinavia";
		continentValue = 3;
		expectedName = name.toLowerCase();
		continent = new Continent(id, name, continentValue);

		//Method Call
		String result = continent.getName();

		//Evaluation
		assertEquals(id, continent.getId());
		assertTrue(result.equals(expectedName));
		assertEquals(continentValue, continent.getContinentValue());
	}

	/**
	 * Context: Initializing the continent with name, value, id
	 * Method call: set continent value
	 * Evaluation: check value is correct for new continent
	 */
	@Test
	public void test3_setContinentValue() {
		id = 43;
		name = "Scandinavia";
		continentValue = 3;
		continent = new Continent(id, name, continentValue);
		continentValue = 7;
		continent.setContinentValue(continentValue);
		assertEquals(continentValue, continent.getContinentValue());
	}

	/**
	 * Context: Initializing the continent with name, value, id and color
	 * Method call: set color
	 * Evaluation: check color is correct for new continent
	 */
	@Test
	public void test4_setColor() {
		id = 43;
		name = "Scandinavia";
		continentValue = 3;
		continent = new Continent(id, name, continentValue);
		color = "red";
		continent.setColor(color);
		assertSame(color, continent.getColor());
	}

	/**
	 * Context: Initializing the continent with name, value, id
	 * Method call: getid and Object's hash method
	 * Evaluation: check id is correct for new continent
	 */
	@Test
	public void test5_hash() {
		id = 43;
		name = "Scandinavia";
		continentValue = 3;
		continent = new Continent(id, name, continentValue);

		id = continent.getId();
		hashId = Objects.hash(id);

		assertEquals(hashId, continent.hashCode());
	}

}