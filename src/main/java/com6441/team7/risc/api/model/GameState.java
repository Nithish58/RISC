package com6441.team7.risc.api.model;

/**
 * This class stores different game states
 */
public enum GameState {
	/**
	 * Load game
	 */
    LOAD_GAME("loadgame"),
    /**
     * Load map
     */
    LOAD_MAP("loadmap"),
    /**
     * Start up
     */
    START_UP("startup"),
    /**
     * Reinforce
     */
    REINFORCE("reinforce"),
    /**
     * Attack
     */
    ATTACK("attack"),
    /**
     * Fortify
     */
    FORTIFY("fortify");
	
	/**
	 * value of different gamestates
	 */
    public String name;
    
    /**
     * Setter for GameState Value
     * @param name set this gamestate as current state
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
