package com6441.team7.risc.controller;

import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.view.GameView;
import com6441.team7.risc.view.PhaseView;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ConquestReaderWriterTest {
    private ConquestReaderWriter conquestReaderWriter;


    @Before
    public void setUp() throws Exception {
        MapService mapService = new MapService();
        GameView view = new PhaseView();
         conquestReaderWriter = new ConquestReaderWriter(mapService, view, new AtomicInteger(0), new AtomicInteger(0));
    }


    /**
     * read existing map from the directory given by its map name
     * The test will pass if it ables to read and parses the map file and returns true
     * @throws Exception
     */
    @Test
    public void readValidFile() throws Exception{
        URI uri = getClass().getClassLoader().getResource("conquest_test.map").toURI();
        String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
        conquestReaderWriter.parseFile(file);
        conquestReaderWriter.showConquestMap();
        assertTrue(conquestReaderWriter.getMapService().isMapValid());
    }

    @Test
    public void readValidFile2() throws Exception{
        URI uri = getClass().getClassLoader().getResource("aden.map").toURI();
        String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
        conquestReaderWriter.parseFile(file);
        conquestReaderWriter.showConquestMap();
        assertTrue(conquestReaderWriter.getMapService().isMapValid());
    }

    @Test
    public void readValidFile3() throws Exception{
        URI uri = getClass().getClassLoader().getResource("Africa.map").toURI();
        String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
        conquestReaderWriter.parseFile(file);
        conquestReaderWriter.showConquestMap();
        assertTrue(conquestReaderWriter.getMapService().isMapValid());
    }
}