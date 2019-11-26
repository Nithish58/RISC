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


    public void setListPlayers(ArrayList<Player> listPlayers) {
        this.listPlayers = listPlayers;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public void setCommand(String command) {
        this.command = command;
    }


}
