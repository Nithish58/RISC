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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContinentTest {
	private int id;
	private String name;
	private String expectedName;
	private int continentValue;
	private String color;
	private int hashId;
	
	private Continent continent;
	@BeforeClass
	public static void initClass() {
		System.out.println("Initialize Continent Test");
	}
	
	@Before
	public void beginTest() {
		System.out.println("Begin Test");
	}
	
	@After
	public void endTest() {
		System.out.println("End Test");
	}
	
	@AfterClass
	public static void endClass() {}
	
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
	
	@Test
	public void test3_setContinentValue() {
		id = 43;
		name = "Scandinavia";
		continentValue = 3;
		continent = new Continent(id, name, continentValue);
		continentValue=7;
		continent.setContinentValue(continentValue);
		assertEquals(continentValue, continent.getContinentValue());
	}
	
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
