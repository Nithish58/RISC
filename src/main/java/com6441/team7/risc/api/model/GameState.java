package com6441.team7.risc.api.model;

public enum GameState {
    LOAD_MAP("loadmap"),
    START_UP("startup"),
    REINFORCE("reinforce"),
    FORTIFY("fortify");

    private String name;

    GameState(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }


}
