package com6441.team7.risc.api.wrapperview;

public class StartupStateWrapper {
    public StartupStateWrapper(){}

    /**
     * Boolean that checks if map is loaded.
     * Used to control game flow.
     */
    private boolean boolMapLoaded;
    /**
     * Boolean that checks if atleast 1 player is added.
     * If this is false, does not proceed further and asks addition of at least 1 player.
     * Used to control game flow.
     */
    private boolean boolGamePlayerAdded;
    /**
     * Boolean that checks if all players have been added.
     * Set to true when populate countries called
     * Can no longer add more players after this point.
     * Used to control game flow.
     */
    private boolean boolAllGamePlayersAdded;
    /**
     * Boolean that checks if a country have been successfully populated.
     * If true, allows proceeding to next phase.
     * Else need to populate countries first
     * Used to control game flow.
     */
    private boolean boolCountriesPopulated;
    /**
     * Array of boolean that checks if all armies have been placed on all countries.
     * If true, allows proceeding to next phase; else program keep looping over players until all armies have been placed.
     */

    boolean boolAllCountriesPlaced;


    public boolean isBoolMapLoaded() {
        return boolMapLoaded;
    }

    public void setBoolMapLoaded(boolean boolMapLoaded) {
        this.boolMapLoaded = boolMapLoaded;
    }

    public boolean isBoolGamePlayerAdded() {
        return boolGamePlayerAdded;
    }

    public void setBoolGamePlayerAdded(boolean boolGamePlayerAdded) {
        this.boolGamePlayerAdded = boolGamePlayerAdded;
    }

    public boolean isBoolAllGamePlayersAdded() {
        return boolAllGamePlayersAdded;
    }

    public void setBoolAllGamePlayersAdded(boolean boolAllGamePlayersAdded) {
        this.boolAllGamePlayersAdded = boolAllGamePlayersAdded;
    }

    public boolean isBoolCountriesPopulated() {
        return boolCountriesPopulated;
    }

    public void setBoolCountriesPopulated(boolean boolCountriesPopulated) {
        this.boolCountriesPopulated = boolCountriesPopulated;
    }

    public boolean isBoolAllCountriesPlaced() {
        return boolAllCountriesPlaced;
    }

    public void setBoolAllCountriesPlaced(boolean boolAllCountriesPlaced) {
        this.boolAllCountriesPlaced = boolAllCountriesPlaced;
    }
}
