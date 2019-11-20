package com6441.team7.risc.api.model;

import java.util.List;

public class GameProgress {
    private List<Player> playerList;
    private GameState gameState;
    private int maximumTurn;
    private int gameRound;
    private List<Player> gameResult;
    private Player currentPlayer;

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

  ,

    public List<Player> getPlayerList() {
        return playerList;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getMaximumTurn() {
        return maximumTurn;
    }

    public int getGameRound() {
        return gameRound;
    }

    public void setGameRound(int gameRound) {
        this.gameRound = gameRound;
    }

    public List<Player> getGameResult() {
        return gameResult;
    }

    public void setGameResult(List<Player> gameResult) {
        this.gameResult = gameResult;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
