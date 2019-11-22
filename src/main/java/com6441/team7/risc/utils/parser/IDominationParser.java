package com6441.team7.risc.utils.parser;

import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.view.GameView;

import java.io.IOException;
/**
 * This is interface for domination map file parser. It is used in both MapParserAdapter 
 * and in DominationParser as part of adapter pattern.
 */
public interface IDominationParser {
	
	/**
     * stub method to save domination map to a file.
     * @param fileName name of save file.
     * @param mapService details of map to be pulled from.
     * @throws IOException throws when unable to save map file of given name.
     * @return returns true if successfully saved.
     */
    boolean saveDominateMap(String fileName, MapService mapService) throws IOException;
    
    /**
	 * stub method to display conquest map.
	 * @param mapService details to be pulled from.
	 */
    void showDominateMap(MapService mapService);
    
    /**
     * stub method to read and parse existing conquest map file.
     * @param fileName name of map file.
     * @param view to display map on.
     * @param mapService provides map details for use in method.
     * @return returns true if existing map can be successfully read and parsed.
     */
    boolean readDominateMapFile(String fileName, GameView view, MapService mapService);

}
