package com6441.team7.risc.utils.parser;

import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.view.GameView;

import java.io.IOException;

public interface IConquestParser {
    void showConquestMap(GameView gameView, MapService mapService);
    boolean saveConquestMap(String fileName, MapService mapService);
    boolean readConquestMapFile(String filename, GameView gameView, MapService mapService);
}