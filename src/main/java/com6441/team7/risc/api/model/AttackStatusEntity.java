package com6441.team7.risc.api.model;

import java.util.concurrent.atomic.AtomicBoolean;

public class AttackStatusEntity {
    private AtomicBoolean boolDefenderDiceRequired;

    public AttackStatusEntity(){}

    private AttackStatusEntity(AtomicBoolean boolDefenderDiceRequired){
        this.boolDefenderDiceRequired = boolDefenderDiceRequired;
    }

    public AtomicBoolean getBoolDefenderDiceRequired() {
        return boolDefenderDiceRequired;
    }

    public static class AttackStatusEntityBuilder{
        private AtomicBoolean boolDefenderDiceRequired;

        public static AttackStatusEntityBuilder newInstance(){
            return new AttackStatusEntityBuilder();
        }

        public AttackStatusEntityBuilder boolDefenderDiceRequired(AtomicBoolean boolDefenderDiceRequired){
            this.boolDefenderDiceRequired = boolDefenderDiceRequired;
            return this;
        }

        public AttackStatusEntity build(){
            return new AttackStatusEntity(boolDefenderDiceRequired);
        }

    }
}
