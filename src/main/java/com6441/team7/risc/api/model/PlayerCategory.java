package com6441.team7.risc.api.model;

public enum PlayerCategory {
    AGGRESSIVE("aggressive"),
    RANDOM("random"),
    CHEATER("cheater"),
    BENEVOLENT("benevolent"),
    HUMAN("human");

    private String name;

    PlayerCategory(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }


}
