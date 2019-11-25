package com6441.team7.risc.api.model;

/**
 * This class is used to used to store the state in ReinforceController
 */
public class ReinforceStateEntity {
    /**
     * the boolean value to check if exchangecard phase is over
     */
    private boolean exchangeCardOver;

    /**
     * the number of reinforced armies
     */
    private int reinforcedArmies;


    /**
     * default constructor
     */
    public ReinforceStateEntity(){}

    /**
     * constructor
     * @param exchangeCardOver the boolean value to check if exchangecard phase is over
     * @param reinforcedArmies the number of reinforced armies
     */
    private ReinforceStateEntity(boolean exchangeCardOver, int reinforcedArmies){

        this.exchangeCardOver = exchangeCardOver;
        this.reinforcedArmies = reinforcedArmies;
    }

    /**
     * return the boolean value exchangeCardOver
     * @return exchangeCardOver
     */
    public boolean isExchangeCardOver() {
        return exchangeCardOver;
    }

    /**
     * return the reinforcedArmies
     * @return reinforcedArmies
     */
    public int getReinforcedArmies() {
        return reinforcedArmies;
    }


    /**
     * the class is used to build an instance of ReinforceStateEntityBuilder
     */
    public static class ReinforceStateEntityBuilder{
        /**
         * the boolean value to check if exchangecard phase is over
         */
        private boolean exchangeCardOver;

        /**
         * the number of reinforced armies
         */
        private int reinforcedArmies;

        /**
         * create a new ReinforceStateEntityBuilder
         * @return a new object of ReinforceStateEntityBuilder
         */
        public static ReinforceStateEntityBuilder newInstance(){
            return new ReinforceStateEntityBuilder();
        }

        /**
         * build boolean value isExchangeCardOver
         * @param exchangeCardOver the boolean value to check if exchangecard phase is over
         * @return ReinforceStateEntityBuilder
         */
        public ReinforceStateEntityBuilder isExchangeCardOver(boolean exchangeCardOver){
            this.exchangeCardOver = exchangeCardOver;
            return this;
        }

        /**
         * build int value reinforcedArmies
         * @param reinforcedArmies the number of reinforced armies
         * @return ReinforceStateEntityBuilder
         */
        public ReinforceStateEntityBuilder reinforcedArmies(int reinforcedArmies){
            this.reinforcedArmies = reinforcedArmies;
            return this;
        }

        /**
         * build an object of ReinforceStateEntity
         * @return new object of ReinforceStateEntity
         */
        public ReinforceStateEntity build(){
            return new ReinforceStateEntity(exchangeCardOver, reinforcedArmies);
        }
    }
}
