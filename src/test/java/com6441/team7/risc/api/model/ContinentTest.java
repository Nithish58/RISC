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
	 * Testing firstConstructor method of first Continent class
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
	 * Testing second_Constructor method of first Continent class
	 */
	@Test
	public void test2_secondConstructor() {
		id = 43;
		name = "Scandinavia";
		continentValue = 3;
		expectedName = name.toLowerCase();
		continent = new Continent(id, name, continentValue);
		String result = continent.getName();
		assertEquals(id, continent.getId());
		assertTrue(result.equals(expectedName));
		assertEquals(continentValue, continent.getContinentValue());
	}

	/**
	 *Testig setContinentValue method
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
	 *Testing color setting method
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
	 * Testing the hashcode method of continent
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