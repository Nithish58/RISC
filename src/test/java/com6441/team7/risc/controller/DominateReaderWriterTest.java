package com6441.team7.risc.controller;

import com6441.team7.risc.api.exception.ContinentParsingException;
import com6441.team7.risc.api.exception.CountryParsingException;
import com6441.team7.risc.api.exception.NeighborParsingException;
import com6441.team7.risc.api.model.Continent;
import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.view.GameView;
import com6441.team7.risc.view.PhaseView;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

public class DominateReaderWriterTest {

    private DominateReaderWriter dominateReaderWriter;
    private MapLoaderAdapter mapLoaderAdapter;


    @Before
    public void setUp() throws Exception {
        MapService mapService = new MapService();
        GameView view = new PhaseView();
        mapLoaderAdapter = new MapLoaderAdapter(mapService);
        dominateReaderWriter = new DominateReaderWriter(mapService, view);
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
        dominateReaderWriter.parseFile(file);
        assertTrue(dominateReaderWriter.getMapService().isMapValid());
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

        dominateReaderWriter.parseFile(file);
        assertTrue(dominateReaderWriter.getMapService().isMapValid());
    }




    @Test
    public void readInvalidFileWithInvalidContinentId() throws Exception{
        URI uri = getClass().getClassLoader().getResource("jenny.map").toURI();
        String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
        dominateReaderWriter.parseFile(file);
        assertFalse(dominateReaderWriter.getMapService().isMapValid());
    }

}