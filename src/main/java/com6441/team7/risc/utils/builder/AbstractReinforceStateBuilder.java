package com6441.team7.risc.utils.builder;

import com6441.team7.risc.api.model.ReinforceStateEntity;

/**
 * this class is used to build reinforceState
 */
public abstract class AbstractReinforceStateBuilder {

    /**
     * the reference of reinforceStateEntity
     */
    protected ReinforceStateEntity reinforceStateEntity;

    /**
     * create new object of reinforceStateEntity
     */
    public void createNewReinforceStateEntity(){
        this.reinforceStateEntity = new ReinforceStateEntity();
    }

    /**
     * get the object of reinforceStateEntity
     * @return reinforceStateEntity
     */
    public ReinforceStateEntity getReinforceStateEntity(){
        return reinforceStateEntity;
    }

    /**
     * build boolean value booleanExchangeCardOver
     * @param exchangeCardOver check if the card has been exchanged
     */
    public abstract void buildBooleanExchangeCardOver(Boolean exchangeCardOver);

    /**
     * build reinforcedArmy
     * @param reinforcedArmy int value of number of reinforced armies
     */
    public abstract void buildReinforceArmyNumber(int reinforcedArmy);
}
