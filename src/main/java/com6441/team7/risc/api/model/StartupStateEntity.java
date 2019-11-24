package com6441.team7.risc.api.model;

public class StartupStateEntity {

    /**
     * Boolean that checks if a country have been successfully populated.
     * If true, allows proceeding to next phase.
     * Else need to populate countries first
     * Used to control game flow.
     */
    private boolean boolCountriesPopulated;
    private boolean boolMapLoaded;
    private boolean boolAllGamePlayersAdded;
    private boolean boolGamePlayerAdded;
    private boolean boolAllCountriesPlaced;
    private boolean[] boolArrayCountriesPlaced;

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

    public StartupStateEntity(){}

    public boolean isBoolCountriesPopulated() {
        return boolCountriesPopulated;
    }

    public boolean isBoolMapLoaded() {
        return boolMapLoaded;
    }

    public boolean isBoolAllGamePlayersAdded() {
        return boolAllGamePlayersAdded;
    }

    public boolean isBoolGamePlayerAdded() {
        return boolGamePlayerAdded;
    }

    public boolean isBoolAllCountriesPlaced() {
        return boolAllCountriesPlaced;
    }

    public boolean[] boolArrayCountriesPlaced() {
        return boolArrayCountriesPlaced;
    }

    public static class StartupStateEntityBuilder{
        private boolean boolCountriesPopulated;
        private boolean boolMapLoaded;
        private boolean boolAllGamePlayersAdded;
        private boolean boolGamePlayerAdded;
        private boolean boolAllCountriesPlaced;
        private boolean[] boolArrayCountriesPlaced;

        public static StartupStateEntityBuilder newInstance(){
            return new StartupStateEntityBuilder();
        }

        public StartupStateEntityBuilder boolCountriesPopulated(boolean boolCountriesPopulated){
            this.boolCountriesPopulated = boolCountriesPopulated;
            return this;
        }

        public StartupStateEntityBuilder boolMapLoaded(boolean boolMapLoaded){
            this.boolMapLoaded = boolMapLoaded;
            return this;
        }

        public StartupStateEntityBuilder boolAllGamePlayersAdded(boolean boolAllGamePlayersAdded){
            this.boolAllGamePlayersAdded = boolAllGamePlayersAdded;
            return this;
        }

        public StartupStateEntityBuilder boolGamePlayerAdded(boolean boolGamePlayerAdded){
            this.boolGamePlayerAdded = boolGamePlayerAdded;
            return this;
        }

        public StartupStateEntityBuilder boolAllCountriesPlaced(boolean boolAllCountriesPlaced){
            this.boolAllCountriesPlaced = boolAllCountriesPlaced;
            return this;
        }

        public StartupStateEntityBuilder boolArrayCountriesPlaced(boolean[] boolArrayCountriesPlaced){
            this.boolArrayCountriesPlaced = boolArrayCountriesPlaced;
            return this;
        }

        public StartupStateEntity build(){
            return new StartupStateEntity(boolCountriesPopulated, boolMapLoaded,
                    boolAllGamePlayersAdded, boolGamePlayerAdded,
                    boolAllCountriesPlaced, boolArrayCountriesPlaced);
        }

    }

}
