package com6441.team7.risc.api.model;

/**
 * This class stores different game states
 */
public enum GameState {
    LOAD_GAME("loadgame"),
    LOAD_MAP("loadmap"),
    START_UP("startup"),
    REINFORCE("reinforce"),
    ATTACK("attack"),
    FORTIFY("fortify");
/**
 * value of different gamestates
 */
    public String name;
/**
 * Setter for GameState Value
 * @param name
 */
    GameState(String name){
        this.name = name;
    }

    /**
     * default constructor
     */
    GameState(){}
/**
 * Getter method for Gamestate value
 * @return state value
 */
    public String getName(){
        return name;
    }


}
