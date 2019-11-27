package com6441.team7.risc.utils.builder;

import com6441.team7.risc.api.model.StartupStateEntity;

/**
 * this class is used to create startUpStateEntity
 */
public abstract class AbstractStartUpStateBuilder {

    /**
     * a reference of startUpStateEntity
     */
    protected StartupStateEntity startupStateEntity;

    /**
     * create an object of startUpStateEntity
     */
    public void createNewStartUpStateEntity(){
        this.startupStateEntity = new StartupStateEntity();
    }

    /**
     * get the object of startUpStateEntity
     * @return startUpStateEntity
     */
    public StartupStateEntity getStartupStateEntity(){
        return startupStateEntity;
    }

    /**
     * build BoolCountriesPopulated
     * @param isBoolCountriesPopulated if all countries been populated
     */
    public abstract void buildBoolCountriesPopulated(boolean isBoolCountriesPopulated);

    /**
     * build buildBoolMapLoaded
     * @param isBoolMapLoaded if the map is sucessfully loaded
     */
    public abstract void buildBoolMapLoaded(boolean isBoolMapLoaded);

    /**
     * build BoolAllGamePlayersAdded
     * @param isBoolAllGamePlayersAdded if all game players been added
     */
    public abstract void buildBoolAllGamePlayersAdded(boolean isBoolAllGamePlayersAdded);

    /**
     * build buildBoolGamePlayerAdded
     * @param isBoolGamePlayerAdded if at least one game player been added
     */
    public abstract void buildBoolGamePlayerAdded(boolean isBoolGamePlayerAdded);

    /**
     * build buildBoolAllCountriesPlaced
     * @param isBoolAllCountriesPlaced is the boolean value for isBoolAllCountriesPlaced
     */
    public abstract void buildBoolAllCountriesPlaced(boolean isBoolAllCountriesPlaced);
    
    /**
     * 
     * @param boolArrayCountriesPlaced is the array of bolean values for boolArrayCountriesPlaced
     */
    public abstract void buildBoolArrayCountriesPlaced(boolean[] boolArrayCountriesPlaced);

}
