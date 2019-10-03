package com6441.team7.risc.api.model;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class MapServiceTest {
    private MapService mapService;


    @Before
    public void setUp() throws Exception {
        mapService = new MapService();
    }

    @Test
    public void testAddContinentSet() throws Exception{
        Set<Continent> continentSet = createValidContinents();
        mapService.addContinents(continentSet);
        assertEquals(mapService.getContinents().size(), continentSet.size());
    }

    @Test
    public void testAddCountrySet() throws Exception{
        Set<Continent> continentSet = createValidContinents();
        mapService.addContinents(continentSet);

        Set<Country> countrySet = createValidCountries();
        mapService.addCountry(countrySet);

        assertEquals(mapService.getCountries().size(), countrySet.size());
        assertTrue(mapService.countryNameExist("siberia"));

    }

    @Test
    public void testAddNeighboringCountriesSet() throws Exception{
        Set<Continent> continentSet = createValidContinents();
        mapService.addContinents(continentSet);

        Set<Country> countrySet = createValidCountries();
        mapService.addCountry(countrySet);

        Map<Integer, Set<Integer>> map = createValidNeighboringCountries();
        mapService.addNeighboringCountries(map);

        assertEquals(mapService.getAdjacencyCountriesMap().size(), 4);
    }

    @Test
    public void testRemoveCountryFromContinentCountryMap() throws Exception{

    }



    private Map<Integer, Set<Integer>> createValidNeighboringCountries() throws Exception{
        Map<Integer, Set<Integer>> neighboringCountryMap = new HashMap<>();
        Set<Integer> set1 = new HashSet<>(Arrays.asList(2,3));
        Set<Integer> set2 = new HashSet<>(Arrays.asList(3,4));
        Set<Integer> set3 = new HashSet<>(Arrays.asList(1,2));
        Set<Integer> set4 = new HashSet<>(Arrays.asList(1,3));


        neighboringCountryMap.put(1, set1);
        neighboringCountryMap.put(2, set2);
        neighboringCountryMap.put(3, set3);
        neighboringCountryMap.put(4, set4);

        return neighboringCountryMap;

    }

    private Set<Continent> createValidContinents() throws Exception{
        Set<Continent> continentSet = new HashSet<>();
        Continent continent1 = new Continent(1, "AZIO");
        continent1.setContinentValue(5);

        Continent continent2 = new Continent(2, "ameroki");
        continent1.setContinentValue(10);

        Continent continent3 = new Continent(3, "utropa");
        continent1.setContinentValue(10);

        continentSet.add(continent1);
        continentSet.add(continent2);
        continentSet.add(continent3);

        return continentSet;
    }

    private Set<Country> createValidCountries() throws Exception{
        Set<Country> countrySet = new HashSet<>();
        Country country1 = new Country(1, "SIBERIA");
        country1.setContinentIdentifier(1);

        Country country2 = new Country(2, "worrick");
        country1.setContinentIdentifier(1);

        Country country3 = new Country(3, "yazteck");
        country1.setContinentIdentifier(1);

        Country country4 = new Country(4, "kongrolo");
        country1.setContinentIdentifier(2);

        countrySet.add(country1);
        countrySet.add(country2);
        countrySet.add(country3);
        countrySet.add(country4);

        return countrySet;
    }




}