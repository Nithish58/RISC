package com6441.team7.risc.utils.builder;

public class ConcreteReinforceStateBuilder extends AbstractReinforceStateBuilder {

    @Override
    public void buildBooleanExchangeCardOver(Boolean exchangeCardOver) {
        reinforceStateEntity.setExchangeCardOver(exchangeCardOver);
    }

    @Override
    public void buildReinforceArmyNumber(int reinforcedArmy) {
        reinforceStateEntity.setReinforcedArmies(reinforcedArmy);
    }
}
