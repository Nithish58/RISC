package com6441.team7.risc.api.model;

/**
 * This class is used to used to store the data in StartupController
 */
public class StartupStateEntity {

    /**
     * Boolean that checks if a country have been successfully populated.
     * If true, allows proceeding to next phase.
     * Else need to populate countries first
     * Used to control game flow.
     */
    private boolean boolCountriesPopulated;


    /**
     * Boolean that checks if a country have been successfully populated.
     * If true, allows proceeding to next phase.
     * Else need to populate countries first
     * Used to control game flow.
     */
    private boolean boolMapLoaded;

    /**
     * Boolean that checks if all players have been added.
     * Set to true when populate countries called
     * Can no longer add more players after this point.
     * Used to control game flow.
     */
    private boolean boolAllGamePlayersAdded;


    /**
     * Boolean that checks if atleast 1 player is added.
     * If this is false, does not proceed further and asks addition of at least 1 player.
     * Used to control game flow.
     */
    private boolean boolGamePlayerAdded;

    /**
     * check if all countries have been successfully populated
     */
    private boolean boolAllCountriesPlaced;

    /**
     * Array of boolean that checks if all armies have been placed on all countries.
     * If true, allows proceeding to next phase; else program keep looping over players until all armies have been placed.
     */
    private boolean[] boolArrayCountriesPlaced;

    /**
     * constructor
     * @param boolCountriesPopulated Boolean that checks if a country have been successfully populated.
     * @param boolMapLoaded Boolean that checks if map is loaded.
     * @param boolAllGamePlayersAdded Boolean that checks if all players have been added.
     * @param boolGamePlayerAdded Boolean that checks if atleast 1 player is added.
     * @param boolAllCountriesPlaced check if all countries have been successfully populated
     * @param boolArrayCountriesPlaced Array of boolean that checks if all armies have been placed on all countries.
     */
    private StartupStateEntity(boolean boolCountriesPopulated, boolean boolMapLoaded,
                               boolean boolAllGamePlayersAdded, boolean boolGamePlayerAdded,
                               boolean boolAllCountriesPlaced, boolean[] boolArrayCountriesPlaced) {
        this.boolCountriesPopulated = boolCountriesPopulated;
        this.boolMapLoaded = boolMapLoaded;
        this.boolAllGamePlayersAdded = boolAllGamePlayersAdded;
        this.boolGamePlayerAdded = boolGamePlayerAdded;
        this.boolAllCountriesPlaced = boolAllCountriesPlaced;
        this.boolArrayCountriesPlaced = boolArrayCountriesPlaced;
    }

    /**
     * constructor
     */
    public StartupStateEntity(){}

    /**
     * getter boolCountriesPopulated value
     * @return boolCountriesPopulated
     */
    public boolean isBoolCountriesPopulated() {
        return boolCountriesPopulated;
    }

    /**
     * getter boolMapLoaded value
     * @return boolMapLoaded
     */
    public boolean isBoolMapLoaded() {
        return boolMapLoaded;
    }

    /**
     * getter boolAllGamePlayersAdded value
     * @return boolAllGamePlayersAdded
     */
    public boolean isBoolAllGamePlayersAdded() {
        return boolAllGamePlayersAdded;
    }

    /**
     * getter boolGamePlayerAdded value
     * @return boolGamePlayerAdded
     */
    public boolean isBoolGamePlayerAdded() {
        return boolGamePlayerAdded;
    }

    /**
     * getter boolAllCountriesPlaced value
     * @return boolAllCountriesPlaced
     */
    public boolean isBoolAllCountriesPlaced() {
        return boolAllCountriesPlaced;
    }

    /**
     * getter boolArrayCountriesPlaced value
     * @return boolArrayCountriesPlaced
     */
    public boolean[] boolArrayCountriesPlaced() {
        return boolArrayCountriesPlaced;
    }

    public void setBoolCountriesPopulated(boolean boolCountriesPopulated) {
        this.boolCountriesPopulated = boolCountriesPopulated;
    }

    public void setBoolMapLoaded(boolean boolMapLoaded) {
        this.boolMapLoaded = boolMapLoaded;
    }

    public void setBoolAllGamePlayersAdded(boolean boolAllGamePlayersAdded) {
        this.boolAllGamePlayersAdded = boolAllGamePlayersAdded;
    }

    public void setBoolGamePlayerAdded(boolean boolGamePlayerAdded) {
        this.boolGamePlayerAdded = boolGamePlayerAdded;
    }

    public void setBoolAllCountriesPlaced(boolean boolAllCountriesPlaced) {
        this.boolAllCountriesPlaced = boolAllCountriesPlaced;
    }

    public void setBoolArrayCountriesPlaced(boolean[] boolArrayCountriesPlaced) {
        this.boolArrayCountriesPlaced = boolArrayCountriesPlaced;
    }

}
