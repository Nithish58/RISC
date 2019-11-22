package com6441.team7.risc.utils.parser;

import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.view.GameView;

import java.io.IOException;
/**
 * This is interface for domination map file parser. It is used in both MapParserAdapter 
 * and in DominationParser as part of adapter pattern.
 * 
 */
public interface IDominationParser {
    boolean saveDominateMap(String fileName, MapService mapService) throws IOException;
    void showDominateMap(MapService mapService);
    boolean readDominateMapFile(String fileName, GameView view, MapService mapService);

}
