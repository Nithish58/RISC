package com6441.team7.risc.utils.builder;

import com6441.team7.risc.api.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * the class is used to create PlayerServiceEntity
 */
public class ConcretePlayerServiceBuilder extends AbstractPlayerServiceBuilder {

    /**
     * call playerServiceEntity methods to set playerList
     * @param listPlayers list of players
     */
    @Override
    public void buildListPlayers(ArrayList<Player> listPlayers) {
        playerStatusEntity.setListPlayers(listPlayers);
    }

    /**
     * call playerServiceEntity methods to set current player
     * @param player current player
     */
    @Override
    public void buildCurrentPlayer(Player player) {
        playerStatusEntity.setCurrentPlayer(player);
    }

    /**
     * call playerServiceEntity methods to set current player index
     * @param currentPlayerIndex index of current player
     */
    @Override
    public void buildCurrentPlayerIndex(int currentPlayerIndex) {
        playerStatusEntity.setCurrentPlayerIndex(currentPlayerIndex);
    }

    /**
     * call playerServiceEntity methods to set the command
     * @param command game command
     */
    @Override
    public void buildCommand(String command) {
        playerStatusEntity.setCommand(command);
    }

}
