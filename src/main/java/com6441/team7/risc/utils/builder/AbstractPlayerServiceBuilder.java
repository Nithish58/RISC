package com6441.team7.risc.utils.builder;

import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.PlayerStatusEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPlayerServiceBuilder {
    protected PlayerStatusEntity playerStatusEntity;

    public void createNewPlayerServiceEntity(){
        this.playerStatusEntity = new PlayerStatusEntity();
    }

    public PlayerStatusEntity getPlayerStatusEntity(){
        return playerStatusEntity;
    }

    public abstract void buildListPlayers(ArrayList<Player> listPlayers);
    public abstract void buildCurrentPlayer(Player player);
    public abstract void buildCurrentPlayerIndex(int currentPlayerIndex);
    public abstract void buildCommand(String command);


}
