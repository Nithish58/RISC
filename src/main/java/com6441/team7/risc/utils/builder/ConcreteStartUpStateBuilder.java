package com6441.team7.risc.utils.builder;

public class ConcreteStartUpStateBuilder extends AbstractStartUpStateBuilder {

    @Override
    public void buildBoolCountriesPopulated(boolean isBoolCountriesPopulated) {
        startupStateEntity.setBoolCountriesPopulated(isBoolCountriesPopulated);
    }

    @Override
    public void buildBoolMapLoaded(boolean isBoolMapLoaded) {
        startupStateEntity.setBoolMapLoaded(isBoolMapLoaded);
    }

    @Override
    public void buildBoolAllGamePlayersAdded(boolean isBoolAllGamePlayersAdded) {
        startupStateEntity.setBoolAllGamePlayersAdded(isBoolAllGamePlayersAdded);
    }

    @Override
    public void buildBoolGamePlayerAdded(boolean isBoolGamePlayerAdded) {
        startupStateEntity.setBoolGamePlayerAdded(isBoolGamePlayerAdded);
    }

    @Override
    public void buildBoolAllCountriesPlaced(boolean isBoolAllCountriesPlaced) {
        startupStateEntity.setBoolAllCountriesPlaced(isBoolAllCountriesPlaced);
    }

    @Override
    public void buildBoolArrayCountriesPlaced(boolean[] boolArrayCountriesPlaced) {
        startupStateEntity.setBoolArrayCountriesPlaced(boolArrayCountriesPlaced);
    }
}
