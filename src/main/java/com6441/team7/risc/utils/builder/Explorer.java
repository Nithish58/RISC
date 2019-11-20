package com6441.team7.risc.utils.builder;

import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.view.GameView;

public class Explorer {
    private IBuilder gameBuilder;

    public Explorer(IBuilder builder){
        this.gameBuilder = builder;
    }

    public void saveGame(MapService mapService, PlayerService playerService, GameView view){
        gameBuilder.save(mapService, playerService, view);

    }

    public IBuilder getGameBuilder(){
        return gameBuilder;
    }

    public void setGameBuilder(IBuilder builder){
        this.gameBuilder = builder;
    }


}
