package com6441.team7.risc.utils.parser;

import com6441.team7.risc.api.model.MapCategory;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.view.GameView;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * MapParserAdapter class is part of two-way adapter pattern that enables to work 
 * with both conquest and domination map files.
 * 
 *
 */
public class MapParserAdapter implements IConquestParser, IDominationParser{
    private IConquestParser conquestParser;
    private IDominationParser dominationParser;

    public MapParserAdapter(AtomicInteger continentIdGenerator, AtomicInteger countryIdGenerator) {
        this.conquestParser = new ConquestParser(countryIdGenerator, continentIdGenerator);
        this.dominationParser = new DominateParser(countryIdGenerator, continentIdGenerator);
    }

    @Override
    public void showConquestMap(GameView gameView, MapService mapService) {
        conquestParser.showConquestMap(gameView, mapService);
    }

    @Override
    public boolean saveConquestMap(String fileName, MapService mapService) {
        return conquestParser.saveConquestMap(fileName, mapService);
    }

    @Override
    public boolean readConquestMapFile(String filename, GameView gameView, MapService mapService) {
        return conquestParser.readConquestMapFile(filename, gameView, mapService);
    }

    @Override
    public boolean saveDominateMap(String fileName, MapService mapService) throws IOException {
        return dominationParser.saveDominateMap(fileName, mapService);
    }

    @Override
    public void showDominateMap(MapService mapService) {
        dominationParser.showDominateMap(mapService);
    }

    @Override
    public boolean readDominateMapFile(String fileName, GameView view, MapService mapService) {
        return dominationParser.readDominateMapFile(fileName, view, mapService);
    }

    public void showMap(MapCategory mapCategory, GameView view, MapService mapService){
        if(mapCategory.equals(MapCategory.CONQUEST)){
            showConquestMap(view, mapService);
            return;
        }

        if(mapCategory.equals(MapCategory.DOMINATION)){
            showDominateMap(mapService);
            return;
        }
        view.displayMessage("cannot recognize the map category");

    }
}
