package com6441.team7.risc.utils.builder;

/**
 * this class is to build and get startUpStateEntity
 */
public class ConcreteStartUpStateBuilder extends AbstractStartUpStateBuilder {

    /**
     * call startupStateEntity methods to set boolean value boolCountriesPopulated
     * @param isBoolCountriesPopulated if all countries been populated
     */
    @Override
    public void buildBoolCountriesPopulated(boolean isBoolCountriesPopulated) {
        startupStateEntity.setBoolCountriesPopulated(isBoolCountriesPopulated);
    }

    /**
     * call startupStateEntity methods to set boolean value isBoolMapLoaded
     * @param isBoolMapLoaded if the map is sucessfully loaded
     */
    @Override
    public void buildBoolMapLoaded(boolean isBoolMapLoaded) {
        startupStateEntity.setBoolMapLoaded(isBoolMapLoaded);
    }

    /**
     * call startupStateEntity methods to set boolean value isBoolAllGamePlayersAdded
     * @param isBoolAllGamePlayersAdded if all game players been added
     */
    @Override
    public void buildBoolAllGamePlayersAdded(boolean isBoolAllGamePlayersAdded) {
        startupStateEntity.setBoolAllGamePlayersAdded(isBoolAllGamePlayersAdded);
    }

    /**
     * call startupStateEntity methods to set boolean value isBoolGamePlayerAdded
     * @param isBoolGamePlayerAdded if at least one game player been added
     */
    @Override
    public void buildBoolGamePlayerAdded(boolean isBoolGamePlayerAdded) {
        startupStateEntity.setBoolGamePlayerAdded(isBoolGamePlayerAdded);
    }

    /**
     * call startupStateEntity methods to set boolean value isBoolAllCountriesPlaced
     * @param isBoolAllCountriesPlaced is the boolean value for isBoolAllCountriesPlaced
     */
    @Override
    public void buildBoolAllCountriesPlaced(boolean isBoolAllCountriesPlaced) {
        startupStateEntity.setBoolAllCountriesPlaced(isBoolAllCountriesPlaced);
    }

    /**
     * call startupStateEntity methods to set boolean value boolArrayCountriesPlaced
     * @param boolArrayCountriesPlaced is the array of bolean values for boolArrayCountriesPlaced
     */
    @Override
    public void buildBoolArrayCountriesPlaced(boolean[] boolArrayCountriesPlaced) {
        startupStateEntity.setBoolArrayCountriesPlaced(boolArrayCountriesPlaced);
    }
}
