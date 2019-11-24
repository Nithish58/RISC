package com6441.team7.risc.api.model;

public class ReinforceStateEntity {
    private boolean exchangeCardOver;

    public ReinforceStateEntity(){}

    private ReinforceStateEntity(boolean exchangeCardOver){
        this.exchangeCardOver = exchangeCardOver;
    }

    public boolean isExchangeCardOver() {
        return exchangeCardOver;
    }


    public static class ReinforceStateEntityBuilder{
        private boolean exchangeCardOver;

        public static ReinforceStateEntityBuilder newInstance(){
            return new ReinforceStateEntityBuilder();
        }

        public ReinforceStateEntityBuilder isExchangeCardOver(boolean exchangeCardOver){
            this.exchangeCardOver = exchangeCardOver;
            return this;
        }

        public ReinforceStateEntity build(){
            return new ReinforceStateEntity(exchangeCardOver);
        }
    }
}
