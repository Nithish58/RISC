package com6441.team7.risc.api.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Method that tests different methods of Country class
 * 
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CountryTest {


    /**
     * Reference to country object
     */
    private Country country;

    /**
     * This method is called before every test method is performed.
     * Here, a country is defined before every test.
     */
    @Before
    public void beginTest() {
        country=new Country(1,"Mauritius");
    }

    /**
     * Context: Initializing the country.
     * Method call: setting soldiers to 10.
     * Evaluation: check if number of soldiers in country =10
     */
    @Test public void test001_setterGetterSoldiers(){

        //Methodcall
        country.setSoldiers(10);

        // Evaluation
        assertEquals(country.getSoldiers().intValue(),10);

    }

    /**
     * Context: Initializing the country, set the soldiers 10 and create number of solders to add
     * Method call: adding 5 new soldiers to country
     * Evaluation: check if the number of solders becomes 15
     */
    @Test public void test002_SoldierAddition() {
        //Context
        country.setSoldiers(10);
        int numSoldiersToAdd=5;
        //Methodcall
        country.addSoldiers(numSoldiersToAdd);
        //Evaluation
        assertEquals(country.getSoldiers().intValue(),15);

    }

    /**
     * Context: Initialising the country, set the soldiers 10 and create number of solders to add
     * Method call: Removing 5 new soldiers to country
     * Evaluation: check if the number of solders becomes 5
     */
    @Test public void test003_soldierRemoval() {
        //Context
        country.setSoldiers(10);
        int numSoldiersToRemove=5;

        //Method call
        country.removeSoldiers(numSoldiersToRemove);
        //Evaluation
        assertEquals(country.getSoldiers().intValue(),5);

    }


}