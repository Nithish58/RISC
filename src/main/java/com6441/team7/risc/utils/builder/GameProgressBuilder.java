package com6441.team7.risc.utils.builder;

import com6441.team7.risc.api.model.GameProgress;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.utils.builder.IBuilder;
import com6441.team7.risc.view.GameView;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class GameProgressBuilder implements IBuilder {

    private GameProgress gameProgress;

    public GameProgressBuilder(){
        this.gameProgress = new GameProgress();
    }

    private void creatFile(GameView view) {
    }

    @Override
    public void save(MapService mapService, PlayerService playerService, GameView view) {
        creatFile(view);
        gameProgress.saveGame(mapService, playerService, view);
    }

    @Override
    public GameProgress load() {
        return null;
    }
}
