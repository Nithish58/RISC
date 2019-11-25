package com6441.team7.risc.api.model;

import java.util.ArrayList;

/**
 * This class is used to used to store the data in playerStatusEntity
 */
public class PlayerStatusEntity {
    /**
     * List of players playing the game
     */
    private ArrayList<Player> listPlayers;


    /**
     * the reference of current player
     */
    private Player currentPlayer;

    /**
     * the current player index
     */
    private int currentPlayerIndex;

    /**
     * the game command of current game phase
     */
    private String command;

    /**
     * default constructor
     */
    public PlayerStatusEntity(){}

    /**
     * constructor
     * @param listPlayers list of players
     * @param currentPlayer current player
     * @param currentPlayerIndex the index of current player
     * @param command the game command of current game phase
     */
    private PlayerStatusEntity(ArrayList<Player> listPlayers, Player currentPlayer,int currentPlayerIndex, String command) {
        this.listPlayers = listPlayers;
        this.currentPlayer = currentPlayer;
        this.currentPlayerIndex = currentPlayerIndex;
        this.command = command;
        
        for(Player p:this.listPlayers) {
        	p.instantiatePlayerCountryListForLoading();
        }
        
    }


    /**
     * get list of players
     * @return listPlayers
     */
    public ArrayList<Player> getListPlayers() {
        return listPlayers;
    }

    /**
     * get current player
     * @return currentPlayer
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * get current player index
     * @return currentPlayerIndex
     */
    public int getCurrentPlayerIndex(){
        return currentPlayerIndex;
    }


    /**
     * get the game command of current game phase
     * @return game command
     */
    public String getCommand() {
        return command;
    }


    /**
     * the class is used to build an instance of PlayerStatusEntity
     */
    public static class PlayerStatusEntityBuilder{
        /**
         * List of players playing the game
         */
        private ArrayList<Player> listPlayers;

        /**
         * the reference of current player
         */
        private Player currentPlayer;

        /**
         * the index of current player
         */
        private int currentPlayerIndex;

        /**
         * the game command of current game phase
         */
        private String command;

        /**
         * create a new PlayerStatusEntityBuilder
         * @return a new object of PlayerStatusEntityBuilder
         */
        public static PlayerStatusEntityBuilder newInstance(){
            return new PlayerStatusEntityBuilder();
        }

        /**
         * build the playerList
         * @param listPlayers list of players
         * @return new object of PlayerStatusEntityBuilder
         */
        public PlayerStatusEntityBuilder playerList(ArrayList<Player> listPlayers){
            this.listPlayers = listPlayers;
            return this;
        }


        /**
         * build the currentPlayer
         * @param currentPlayer current player
         * @return new object of PlayerStatusEntityBuilder
         */
        public PlayerStatusEntityBuilder currentPlayer(Player currentPlayer){
            this.currentPlayer = currentPlayer;
            return this;
        }

        /**
         * build the currentPlayerIndex
         * @param currentPlayerIndex the index of current player
         * @return new object of PlayerStatusEntityBuilder
         */
        public PlayerStatusEntityBuilder currentPlayerIndex(int currentPlayerIndex){
            this.currentPlayerIndex = currentPlayerIndex;
            return this;
        }


        /**
         * build the command
         * @param command the game command of current game phase
         * @return new object of PlayerStatusEntityBuilder
         */
        public PlayerStatusEntityBuilder command(String command){
            this.command = command;
            return this;
        }


        /**
         * build an object of PlayerStatusEntity
         * @return new object of PlayerStatusEntity
         */
        public PlayerStatusEntity build(){
            return new PlayerStatusEntity(listPlayers, currentPlayer,currentPlayerIndex, command);
        }
    }
}
