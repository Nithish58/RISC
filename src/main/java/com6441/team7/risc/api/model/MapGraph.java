package com6441.team7.risc.api.model;

import java.util.List;

public class MapGraph {
    private List<String> mapGraph;

    MapGraph(List<String> mapGraph){
        this.mapGraph = mapGraph;
    }

    public List<String> getMapGraph() {
        return mapGraph;
    }

    public void setMapGraph(List<String> mapGraph) {
        this.mapGraph = mapGraph;
    }
}
