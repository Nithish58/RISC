package com6441.team7.risc.controller;

import com6441.team7.risc.api.exception.*;
import com6441.team7.risc.api.model.*;
import com6441.team7.risc.view.GameView;
import com6441.team7.risc.view.PhaseView;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com6441.team7.risc.api.RiscConstants.EOL;
import static com6441.team7.risc.api.RiscConstants.WHITESPACE;
import static java.util.Objects.isNull;

public class DominateReaderWriter implements IDominationReaderWriter {


    @Override
    public void editDominationContinent(String command) {

    }

    @Override
    public void editDominationCountry(String command) {

    }

    @Override
    public void editDominationNeighbor(String command) {

    }
}
