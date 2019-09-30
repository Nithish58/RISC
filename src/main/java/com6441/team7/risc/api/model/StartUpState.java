package com6441.team7.risc.api.model;

import com6441.team7.risc.controller.StateContext;

public class StartUpState implements State {
    @Override
    public void alert(StateContext context) {
        System.out.println("start up");
    }
}
