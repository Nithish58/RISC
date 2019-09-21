package com6441.team7.risc.api.model;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.Scanner;

import static org.junit.Assert.*;

public class MapLoaderTest {

    private MapLoader mapLoader;

    @Before
    public void setUp() throws Exception {
        mapLoader = new MapLoader();
    }

    @Test
    public void loadValidMap() {
        ClassLoader classLoader = getClass().getClassLoader();
        String mapPath = classLoader.getResource("test.map").getPath();
        String data = mapPath + "\n";
        InputStream stdin = System.in;

        Optional<MapService> mapService = mockScanner(data, stdin);
        assertTrue(mapService.isPresent());
        assertTrue(mapService.get().isStrongConnected());

    }


    @Test
    public void loadNotStronglyConnectedMap() {
        ClassLoader classLoader = getClass().getClassLoader();
        String mapPath = classLoader.getResource("notStronglyConnectedMap.map").getPath();
        String data = mapPath + "\n";
        InputStream stdin = System.in;
        Optional<MapService> mapService = mockScanner(data, stdin);
        assertFalse(mapService.isPresent());
    }


    @Test
    public void loadInValidFileMissingTerritoriesInfo(){
        ClassLoader classLoader = getClass().getClassLoader();
        String mapPath = classLoader.getResource("missTerritoriesInfo.map").getPath();
        String data = mapPath + "\n";
        InputStream stdin = System.in;
        Optional<MapService> mapService = mockScanner(data, stdin);
        assertFalse(mapService.isPresent());

    }


    private Optional<MapService> mockScanner(String data, InputStream stdin){
        try{
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            Scanner scanner = new Scanner(System.in);
            return mapLoader.loadMap(scanner);

        }finally {
            System.setIn(stdin);

        }
    }
}