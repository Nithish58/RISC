package com6441.team7.risc.api.model;

import com6441.team7.risc.controller.StateContext;

public class MapLoaderState implements State {

    @Override
    public void alert(StateContext context) {
        System.out.println("map loader state");
    }
}
