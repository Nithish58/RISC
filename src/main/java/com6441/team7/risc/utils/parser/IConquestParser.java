package com6441.team7.risc.utils.parser;

import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.view.GameView;

import java.io.IOException;
/**
 * This is interface for conquest map file parser. It is used in both MapParserAdapter 
 * and in ConquestParser as part of adapter pattern.
 * 
 */
public interface IConquestParser {
    void showConquestMap(GameView gameView, MapService mapService);
    boolean saveConquestMap(String fileName, MapService mapService);
    boolean readConquestMapFile(String filename, GameView gameView, MapService mapService);
}