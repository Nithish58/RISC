package com6441.team7.risc.utils.builder;

import com6441.team7.risc.api.model.StartupStateEntity;

public abstract class AbstractStartUpStateBuilder {
    protected StartupStateEntity startupStateEntity;

    public void createNewStartUpStateEntity(){
        this.startupStateEntity = new StartupStateEntity();
    }

    public StartupStateEntity getStartupStateEntity(){
        return startupStateEntity;
    }

    public abstract void buildBoolCountriesPopulated(boolean isBoolCountriesPopulated);
    public abstract void buildBoolMapLoaded(boolean isBoolMapLoaded);
    public abstract void buildBoolAllGamePlayersAdded(boolean isBoolAllGamePlayersAdded);
    public abstract void buildBoolGamePlayerAdded(boolean isBoolGamePlayerAdded);
    public abstract void buildBoolAllCountriesPlaced(boolean isBoolAllCountriesPlaced);
    public abstract void buildBoolArrayCountriesPlaced(boolean[] boolArrayCountriesPlaced);

}
