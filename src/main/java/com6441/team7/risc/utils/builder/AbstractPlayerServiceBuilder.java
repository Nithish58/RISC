package com6441.team7.risc.utils.builder;

import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.PlayerStatusEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * the class is used to build PlayerStatusEntity
 */
public abstract class AbstractPlayerServiceBuilder {

    /**
     * the reference of playerStatusEntity
     */
    protected PlayerStatusEntity playerStatusEntity;

    /**
     * create new object of playerStatusEntity
     */
    public void createNewPlayerServiceEntity(){
        this.playerStatusEntity = new PlayerStatusEntity();
    }

    /**
     * get the object of playerStatusEntity
     * @return the player status entity
     */
    public PlayerStatusEntity getPlayerStatusEntity(){
        return playerStatusEntity;
    }

    /**
     * build the list of players
     * @param listPlayers list of players
     */
    public abstract void buildListPlayers(ArrayList<Player> listPlayers);

    /**
     * build the current player
     * @param player current player
     */
    public abstract void buildCurrentPlayer(Player player);

    /**
     * build current player index
     * @param currentPlayerIndex index of current player
     */
    public abstract void buildCurrentPlayerIndex(int currentPlayerIndex);

    /**
     * build the command
     * @param command game command
     */
    public abstract void buildCommand(String command);


}
