package com6441.team7.risc.api.model;

import com6441.team7.risc.api.wrapperview.FortifyStateWrapper;
import com6441.team7.risc.api.wrapperview.ReinforceStateWrapper;

public class ControllerStatusEntity {
    private StartupStateEntity startupStateEntity;
    private ReinforceStateWrapper reinforceStateWrapper;
    private FortifyStateWrapper fortifyStateWrapper;


    public ControllerStatusEntity(){}

    private ControllerStatusEntity(StartupStateEntity startupStateEntity,
                                   ReinforceStateWrapper reinforceStateWrapper,
                                   FortifyStateWrapper fortifyStateWrapper){
        this.startupStateEntity = startupStateEntity;
        this.reinforceStateWrapper = reinforceStateWrapper;
        this.fortifyStateWrapper = fortifyStateWrapper;
    }

    public StartupStateEntity getStartupStateEntity() {
        return startupStateEntity;
    }

    public ReinforceStateWrapper getReinforceStateWrapper() {
        return reinforceStateWrapper;
    }

    public FortifyStateWrapper getFortifyStateWrapper() {
        return fortifyStateWrapper;
    }

    public static class ControllerStatusEntityBuilder{

        private StartupStateEntity startupStateEntity;
        private ReinforceStateWrapper reinforceStateWrapper;
        private FortifyStateWrapper fortifyStateWrapper;


        public static ControllerStatusEntityBuilder newInstance(){
            return new ControllerStatusEntityBuilder();
        }

        public ControllerStatusEntityBuilder startupStateWrapper(StartupStateEntity startupStateEntity){
            this.startupStateEntity = startupStateEntity;
            return this;
        }

        public ControllerStatusEntityBuilder reinforceStateWrapper(ReinforceStateWrapper reinforceStateWrapper){
            this.reinforceStateWrapper = reinforceStateWrapper;
            return this;
        }

        public ControllerStatusEntityBuilder fortifyStateWrapper(FortifyStateWrapper fortifyStateWrapper){
            this.fortifyStateWrapper = fortifyStateWrapper;
            return this;
        }

        public ControllerStatusEntity build(){
            return new ControllerStatusEntity(startupStateEntity, reinforceStateWrapper, fortifyStateWrapper);
        }





    }


}
