package com6441.team7.risc.controller;

import java.io.IOException;

public interface IConquestReaderWriter {
    void showConquestMap();
    void saveConquestMap(String fileName) throws IOException;
    void readConquestMapFile(String s);
}
