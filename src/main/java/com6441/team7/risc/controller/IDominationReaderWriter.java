package com6441.team7.risc.controller;

import java.io.IOException;
import java.util.Optional;

public interface IDominationReaderWriter {
    void saveDominateMap(String fileName) throws IOException;
    void showDominateMap();
    void readDominateMapFile(String fileName);

}
