package com6441.team7.risc.api.model;

public enum  MapCategory {
    CONQUEST("conquest"),
    DOMINATION("domination");

    private String name;

    MapCategory(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
