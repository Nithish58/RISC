package com6441.team7.risc.controller;

import com6441.team7.risc.api.model.GameProgress;

public class SaveLoadGameController {
    private IBuilder gameProgressBuilder;

    public void setBuilder(IBuilder builder){
        this.gameProgressBuilder = builder;
    }

    public GameProgress getGameProgress(){
        return gameProgressBuilder.load();
    }
}
