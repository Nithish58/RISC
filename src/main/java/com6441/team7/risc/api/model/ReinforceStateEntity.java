package com6441.team7.risc.api.model;

public class ReinforceStateEntity {
    private boolean exchangeCardOver;
    private int reinforcedArmies;


    public ReinforceStateEntity(){}

    private ReinforceStateEntity(boolean exchangeCardOver, int reinforcedArmies){

        this.exchangeCardOver = exchangeCardOver;
        this.reinforcedArmies = reinforcedArmies;
    }

    public boolean isExchangeCardOver() {
        return exchangeCardOver;
    }

    public int getReinforcedArmies() {
        return reinforcedArmies;
    }

    public static class ReinforceStateEntityBuilder{
        private boolean exchangeCardOver;
        private int reinforcedArmies;


        public static ReinforceStateEntityBuilder newInstance(){
            return new ReinforceStateEntityBuilder();
        }

        public ReinforceStateEntityBuilder isExchangeCardOver(boolean exchangeCardOver){
            this.exchangeCardOver = exchangeCardOver;
            return this;
        }

        public ReinforceStateEntityBuilder reinforcedArmies(int reinforcedArmies){
            this.reinforcedArmies = reinforcedArmies;
            return this;
        }

        public ReinforceStateEntity build(){
            return new ReinforceStateEntity(exchangeCardOver, reinforcedArmies);
        }
    }
}
