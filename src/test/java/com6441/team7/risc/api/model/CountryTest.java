package com6441.team7.risc.api.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CountryTest {
    private Integer id;
    private String name;
    private String expectedName;
    private Integer continentIdentifier;
    private String continentName;
    private Integer coordinateX;
    private Integer coordinateY;
    private Player player;
    private int soldiers = 0;
    
    private Country country;

    @BeforeClass
    public static void initClass() {}
    
    @Before
    public void beginTest() {}
    
    @After
    public void endTest() {}
    
	@Test
	public void test1_firstConstructor() {
		id = 1;
		name = "North Asia";
	}

}
