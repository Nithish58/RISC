package com6441.team7.risc.controller;

import com6441.team7.risc.api.model.GameProgress;

public interface IBuilder {
    void save();
    GameProgress load();
}
