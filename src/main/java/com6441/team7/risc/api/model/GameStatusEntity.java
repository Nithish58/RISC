package com6441.team7.risc.api.model;

public class GameStatusEntity {
    private MapStatusEntity mapStatusEntity;
    private PlayerStatusEntity playerStatusEntity;
    private StartupStateEntity startupStateEntity;

    public GameStatusEntity(){}

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


    public StartupStateEntity getStartupStateEntity() {
        return startupStateEntity;
    }

    public void setStartupStateEntity(StartupStateEntity startupStateEntity) {
        this.startupStateEntity = startupStateEntity;
    }
}
