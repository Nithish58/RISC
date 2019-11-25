package com6441.team7.risc.api.model;

import java.util.ArrayList;

public class PlayerStatusEntity {
    /**
     * List of players playing the game
     */
    private ArrayList<Player> listPlayers;


    /**
     * the reference of current player
     */
    private Player currentPlayer;

    private int currentPlayerIndex;

    private String command;

    public PlayerStatusEntity(){}
    private PlayerStatusEntity(ArrayList<Player> listPlayers, Player currentPlayer,int currentPlayerIndex, String command) {
        this.listPlayers = listPlayers;
        this.currentPlayer = currentPlayer;
        this.currentPlayerIndex = currentPlayerIndex;
        this.command = command;
        
        for(Player p:this.listPlayers) {
        	p.instantiatePlayerCountryListForLoading();
        }
        
    }


    public ArrayList<Player> getListPlayers() {
        return listPlayers;
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getCurrentPlayerIndex(){
        return currentPlayerIndex;
    }



    public String getCommand() {
        return command;
    }


    public static class PlayerStatusEntityBuilder{
        /**
         * List of players playing the game
         */
        private ArrayList<Player> listPlayers;

        /**
         * the reference of current player
         */
        private Player currentPlayer;
        private int currentPlayerIndex;
        private String command;

        public static PlayerStatusEntityBuilder newInstance(){
            return new PlayerStatusEntityBuilder();
        }

        public PlayerStatusEntityBuilder playerList(ArrayList<Player> listPlayers){
            this.listPlayers = listPlayers;
            return this;
        }

        public PlayerStatusEntityBuilder currentPlayer(Player currentPlayer){
            this.currentPlayer = currentPlayer;
            return this;
        }

        public PlayerStatusEntityBuilder currentPlayerIndex(int currentPlayerIndex){
            this.currentPlayerIndex = currentPlayerIndex;
            return this;
        }



        public PlayerStatusEntityBuilder command(String command){
            this.command = command;
            return this;
        }




        public PlayerStatusEntity build(){
            return new PlayerStatusEntity(listPlayers, currentPlayer,currentPlayerIndex, command);
        }
    }
}
