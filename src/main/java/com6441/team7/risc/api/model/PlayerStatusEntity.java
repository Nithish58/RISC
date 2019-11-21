package com6441.team7.risc.api.model;

import com6441.team7.risc.utils.builder.PlayerStatusBuilder;
import com6441.team7.risc.utils.builder.PlayerStatusBuilderImp;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatusEntity {
    public PlayerStatusEntity(){}
    /**
     * List of players playing the game
     */
    private ArrayList<Player> listPlayers;


    /**
     * the reference of current player
     */
    private Player currentPlayer;

    private List<String> mapFiles;

    private int numberOfGame;

    private int currentGameNumber;

    private List<Player> results;

    private int maximumDices;


    private PlayerStatusEntity(ArrayList<Player> listPlayers, Player currentPlayer,
                              List<String> mapFiles, int numberOfGame,
                              int currentGameNumber, List<Player> results,
                              int maximumDices) {
        this.listPlayers = listPlayers;
        this.currentPlayer = currentPlayer;
        this.mapFiles = mapFiles;
        this.numberOfGame = numberOfGame;
        this.currentGameNumber = currentGameNumber;
        this.results = results;
        this.maximumDices = maximumDices;
    }


    public ArrayList<Player> getListPlayers() {
        return listPlayers;
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }


    public List<String> getMapFiles() {
        return mapFiles;
    }

    public int getNumberOfGame() {
        return numberOfGame;
    }

    public int getCurrentGameNumber() {
        return currentGameNumber;
    }


    public List<Player> getResults() {
        return results;
    }

    public int getMaximumDices() {
        return maximumDices;
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

        private List<String> mapFiles;

        private int numberOfGame;

        private int currentGameNumber;

        private List<Player> results;

        private int maximumDices;

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

        public PlayerStatusEntityBuilder mapFiles(List<String> mapFiles){
            this.mapFiles = mapFiles;
            return this;
        }

        public PlayerStatusEntityBuilder numberOfGame(int numberOfGame){
            this.numberOfGame = numberOfGame;
            return this;
        }

        public PlayerStatusEntityBuilder result(List<Player> results){
            this.results = results;
            return this;
        }

        public PlayerStatusEntityBuilder maximumDices(int maximumDices){
            this.maximumDices = maximumDices;
            return this;
        }

        public PlayerStatusEntityBuilder currentGameNumber(int currentGameNumber){
            this.currentGameNumber = currentGameNumber;
            return this;
        }


        public PlayerStatusEntity build(){
            return new PlayerStatusEntity(listPlayers, currentPlayer,
                    mapFiles, numberOfGame, currentGameNumber,
                    results, maximumDices);
        }

    }
}
