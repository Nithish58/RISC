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


    public void setExchangeCardOver(boolean exchangeCardOver) {
        this.exchangeCardOver = exchangeCardOver;
    }

    public void setReinforcedArmies(int reinforcedArmies) {
        this.reinforcedArmies = reinforcedArmies;
    }


}
