package com6441.team7.risc.utils.builder;

/**
 * the class is used to create ReinforceStatusEntity
 */
public class ConcreteReinforceStateBuilder extends AbstractReinforceStateBuilder {

    /**
     * call ReinforceStateEntity methods to set boolean value exchangeCardOver
     * @param exchangeCardOver check if the card has been exchanged
     */
    @Override
    public void buildBooleanExchangeCardOver(Boolean exchangeCardOver) {
        reinforceStateEntity.setExchangeCardOver(exchangeCardOver);
    }

    /**
     * call playerServiceEntity methods to set reinforceArmy
     * @param reinforcedArmy int value of number of reinforced armies
     */
    @Override
    public void buildReinforceArmyNumber(int reinforcedArmy) {
        reinforceStateEntity.setReinforcedArmies(reinforcedArmy);
    }
}
