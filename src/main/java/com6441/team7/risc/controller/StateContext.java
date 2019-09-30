package com6441.team7.risc.controller;

import com6441.team7.risc.api.model.MapLoaderState;
import com6441.team7.risc.api.model.State;

public class StateContext {

    private State currentState;
    public StateContext(){
        currentState = new MapLoaderState();
    }

    public void setState(State state){
        currentState = state;
        alert();
    }

    public void alert(){
        currentState.alert(this);
    }
}
