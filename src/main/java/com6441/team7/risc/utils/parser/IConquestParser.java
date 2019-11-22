package com6441.team7.risc.utils.parser;

import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.view.GameView;

import java.io.IOException;
/**
 * This is interface for conquest map file parser. It is used in both MapParserAdapter 
 * and in ConquestParser as part of adapter pattern.
 */
public interface IConquestParser {
	
	/**
	 * stub method to display conquest map.
	 * @param gameView to display map on.
	 * @param mapService details to be pulled from.
	 */
    void showConquestMap(GameView gameView, MapService mapService);
    
    /**
     * stub method to save conquest map to a file.
     * @param fileName name of save file.
     * @param mapService details of map to be pulled from.
     * @return returns true if successfully saved.
     */
    boolean saveConquestMap(String fileName, MapService mapService);
    
    /**
     * stub method to read and parse existing conquest map file.
     * @param filename name of map file.
     * @param gameView to display map on.
     * @param mapService provides map details for use in method.
     * @return returns true if existing map can be successfully read and parsed.
     */
    boolean readConquestMapFile(String filename, GameView gameView, MapService mapService);
}