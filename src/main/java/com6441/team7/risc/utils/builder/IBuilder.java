package com6441.team7.risc.utils.builder;

import com6441.team7.risc.api.model.GameProgress;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.view.GameView;

public interface IBuilder {
    void save(MapService mapService, PlayerService playerService, GameView view);
    GameProgress load();
}
