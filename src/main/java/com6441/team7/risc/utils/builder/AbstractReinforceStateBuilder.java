package com6441.team7.risc.utils.builder;

import com6441.team7.risc.api.model.ReinforceStateEntity;

public abstract class AbstractReinforceStateBuilder {
    protected ReinforceStateEntity reinforceStateEntity;

    public void createNewReinforceStateEntity(){
        this.reinforceStateEntity = new ReinforceStateEntity();
    }

    public ReinforceStateEntity getReinforceStateEntity(){
        return reinforceStateEntity;
    }

    public abstract void buildBooleanExchangeCardOver(Boolean exchangeCardOver);
    public abstract void buildReinforceArmyNumber(int reinforcedArmy);
}
