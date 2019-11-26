package com6441.team7.risc.utils.builder;

import com6441.team7.risc.api.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ConcretePlayerServiceBuilder extends AbstractPlayerServiceBuilder {

    @Override
    public void buildListPlayers(ArrayList<Player> listPlayers) {
        playerStatusEntity.setListPlayers(listPlayers);
    }

    @Override
    public void buildCurrentPlayer(Player player) {
        playerStatusEntity.setCurrentPlayer(player);
    }

    @Override
    public void buildCurrentPlayerIndex(int currentPlayerIndex) {
        playerStatusEntity.setCurrentPlayerIndex(currentPlayerIndex);
    }

    @Override
    public void buildCommand(String command) {
        playerStatusEntity.setCommand(command);
    }

}
