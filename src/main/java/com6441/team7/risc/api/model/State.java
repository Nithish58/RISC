package com6441.team7.risc.api.model;

import com6441.team7.risc.controller.StateContext;

public interface State {
    void alert(StateContext controller);
    String toString();
}
