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
 * @author MSI
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CountryTest {


    /**
     * Reference to country object
     */
    private Country country;
    
    /**
     * 
     */
    @Before
    public void beginTest() {
    	country=new Country(1,"Mauritius");
    }
    
    /**
     * Context: Initialising the country.
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
     * 
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
