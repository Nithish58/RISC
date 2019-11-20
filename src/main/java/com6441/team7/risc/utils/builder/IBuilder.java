package com6441.team7.risc.utils.builder;

import com6441.team7.risc.api.model.GameProgress;

public interface IBuilder {
    void save();
    GameProgress load();
}
