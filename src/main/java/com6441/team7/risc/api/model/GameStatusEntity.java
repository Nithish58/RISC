package com6441.team7.risc.api.model;

public class GameStatusEntity {
    private MapStatusEntity mapStatusEntity;
    private PlayerStatusEntity playerStatusEntity;

    public MapStatusEntity getMapStatusEntity() {
        return mapStatusEntity;
    }

    public void setMapStatusEntity(MapStatusEntity mapStatusEntity) {
        this.mapStatusEntity = mapStatusEntity;
    }

    public PlayerStatusEntity getPlayerStatusEntity() {
        return playerStatusEntity;
    }

    public void setPlayerStatusEntity(PlayerStatusEntity playerStatusEntity) {
        this.playerStatusEntity = playerStatusEntity;
    }
}
