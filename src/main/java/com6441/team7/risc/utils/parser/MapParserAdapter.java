package com6441.team7.risc.utils.parser;

import com6441.team7.risc.api.model.MapCategory;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.view.GameView;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * MapParserAdapter class is part of two-way adapter pattern that enables to work 
 * with both conquest and domination map files.
 */
public class MapParserAdapter implements IConquestParser, IDominationParser{
	
	/**
	 * 
	 */
    private IConquestParser conquestParser;
    /**
     * 
     */
    private IDominationParser dominationParser;

    /**
     * Constructor for this class, creates new conquest and domination parsers.
     * @param countryIdGenerator generates id for countries incrementally
     * @param continentIdGenerator generates id for continent incrementally
     */
    public MapParserAdapter(AtomicInteger continentIdGenerator, AtomicInteger countryIdGenerator) {
        this.conquestParser = new ConquestParser(countryIdGenerator, continentIdGenerator);
        this.dominationParser = new DominateParser(countryIdGenerator, continentIdGenerator);
    }

    /**
     * shows conquest map file on view
     * @param gameView where map is displayed
     * @param mapService Map details are pulled from
     */
    @Override
    public void showConquestMap(GameView gameView, MapService mapService) {
        conquestParser.showConquestMap(gameView, mapService);
    }

    /**
     * Method to save conquest map file from mapservice and with given file name
     * @param fileName Name of file to be saved in.
     * @param mapService details of map to get from
     * @return returns true if successfully saved conquest map file.
     */
    @Override
    public boolean saveConquestMap(String fileName, MapService mapService) {
        return conquestParser.saveConquestMap(fileName, mapService);
    }

    /**
     * Method for trying to read and parse conquest map file
     * @param filename name of conquest map file. Must include its extension.
     * @param gameView to display the result of what happened when trying to read and parse.
     * @param mapService provides map details for use in method
     * @return returns boolean value true if map can be successfully used in game
     */
    @Override
    public boolean readConquestMapFile(String filename, GameView gameView, MapService mapService) {
        return conquestParser.readConquestMapFile(filename, gameView, mapService);
    }

    /**
     * Method to save domination map file from mapService and with given file name
     * @param fileName Name of file to be saved in.
     * @param mapService details of map to get from
     * @return returns true if successfully saved domination map file.
     */
    @Override
    public boolean saveDominateMap(String fileName, MapService mapService) throws IOException {
        return dominationParser.saveDominateMap(fileName, mapService);
    }

    /**
     * shows domination map file on view
     * @param mapService Map details are pulled from
     */
    @Override
    public void showDominateMap(MapService mapService) {
        dominationParser.showDominateMap(mapService);
    }

    /**
     * Method for trying to read and parse domination map file
     * @param fileName name of domination map file. Must include its extension.
     * @param view to display the result of what happened when trying to read and parse.
     * @param mapService provides map details for use in method
     * @return returns boolean value true if map can be successfully used in game
     */
    @Override
    public boolean readDominateMapFile(String fileName, GameView view, MapService mapService) {
        return dominationParser.readDominateMapFile(fileName, view, mapService);
    }

    /**
     * Displays map on view regardless of domination or conquest. If map is not one of them, displays message.
     * @param mapCategory enum CONQUEST, DOMINATION or UNKNOWN categories of map.
     * @param view display map to view
     * @param mapService details of map to be displayed are goten from mapService
     */
    public void showMap(MapCategory mapCategory, GameView view, MapService mapService){
        if(mapCategory.equals(MapCategory.CONQUEST)){
            showConquestMap(view, mapService);
            return;
        }

        if(mapCategory.equals(MapCategory.DOMINATION)){
            showDominateMap(mapService);
            return;
        }
        view.displayMessage("Unrecognizable the map category");

    }
}
