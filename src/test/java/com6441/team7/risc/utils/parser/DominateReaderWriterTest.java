package com6441.team7.risc.utils.parser;

import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.controller.MapLoaderController;
import com6441.team7.risc.utils.parser.DominateParser;
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

public class DominateReaderWriterTest {

    private DominateParser dominateReaderWriter;
    private GameView view;
    private MapService mapService;

    /**
     * generates id for countries
     */
    private AtomicInteger countryIdGenerator;


    @Before
    public void setUp() throws Exception {
        mapService = new MapService();
        view = new PhaseView();
        AtomicInteger continentIdGenerator = new AtomicInteger(0);
        AtomicInteger countryIdGenerator = new AtomicInteger(0);
        dominateReaderWriter = new DominateParser(continentIdGenerator, countryIdGenerator);
    }


    /**
     * read existing map from the directory given by its map name
     * The test will pass if it ables to read and parses the map file and returns true
     * @throws Exception
     */
    @Test
    public void readValidFile() throws Exception{
        URI uri = getClass().getClassLoader().getResource("ameroki.map").toURI();
        String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
        assertTrue(dominateReaderWriter.parseFile(file, view, mapService));
    }

    /**
     * read a map does not exist given by its map name
     * The test should be able to create a new map file and return true
     * @throws Exception
     */
    @Test
    public void readNewCreatedFile() throws Exception{
        URI uri = getClass().getClassLoader().getResource("test.map").toURI();
        String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);

        assertTrue(dominateReaderWriter.parseFile(file, view, mapService));
    }




    @Test
    public void readInvalidFileWithInvalidContinentId() throws Exception{
        URI uri = getClass().getClassLoader().getResource("jenny.map").toURI();
        String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
        assertFalse(dominateReaderWriter.parseFile(file, view, mapService));
    }

}