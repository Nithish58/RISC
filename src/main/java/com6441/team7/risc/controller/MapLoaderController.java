package com6441.team7.risc.controller;


import com6441.team7.risc.api.model.Continent;
import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.view.CommandPromptView;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapLoaderController {
    private AtomicInteger continentIdGenerator;
    private StateContext stateContext;
    private CommandPromptView view;
    private MapService mapService;

    public MapLoaderController(StateContext stateContext, CommandPromptView view) {
        this.stateContext = stateContext;
        this.view = view;
        this.mapService = new MapService();
        this.continentIdGenerator = new AtomicInteger();
    }

    public void loadMap() {
        if (stateContext.getState().equalsIgnoreCase("maploader")) {
            String command = view.readCommand();
            if (validateEditMapCommands(command).isPresent()) {
                readFile(validateEditMapCommands(command).get());
            }
        }
    }

    Optional<String> validateEditMapCommands(String s) {
        System.out.println(stateContext.getState());
        String[] commands = StringUtils.split(s, " ");

        if (commands.length != 2) {
            view.displayMessage("The command is not valid");
            return Optional.empty();
        }

        String command = commands[0];
        String path = commands[1];

        if (command.toLowerCase().equals("editmap")) {
            return Optional.of(path);
        }

        view.displayMessage("The command is not valid");
        return Optional.empty();
    }


    Optional<String> readFile(String path) {
        try {
            String file = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
            parseFile(file);
            return Optional.of(file);
        } catch (IOException e) {
            view.displayMessage("The map does not exist, we create a new file named " + path);
            createFile(path);
        }

        return Optional.empty();
    }


    boolean parseFile(String s) {
        String[] parts = StringUtils.split(s, "[");
        if (parts.length != 5) {
            return false;
        }

        readContinentsFromFile(parts[2]);
        readCountriesFromFile(parts[3]);
        readAdjascentCountriesFromFile(parts[4]);

        return true;
    }


    Set<Continent> readContinentsFromFile(String part) {
        String continentInfo = StringUtils.substringAfter(part, "]\n");

        Set<Continent> continentsSet = Optional.of(StringUtils.split(continentInfo, "\n"))
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map(this::createContinentFromFile)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        mapService.addContinents(continentsSet);

        return continentsSet;

    }

    private Optional<Continent> createContinentFromFile(String s) {

        try {
            String[] continentInfo = StringUtils.split(s, " ");

            if (continentInfo.length != 3) {
                throw new InvalidObjectException("continent: " + s + " is not valid for missing info");
            }

            int continentValue = Integer.parseInt(continentInfo[1]);

            Continent continent = new Continent(continentIdGenerator.incrementAndGet(), continentInfo[0]);
            continent.setContinentValue(continentValue);
            continent.setColor(continentInfo[2]);

            return Optional.of(continent);

        } catch (InvalidObjectException e) {
            view.displayMessage(e.getMessage());
            return Optional.empty();
        } catch (NumberFormatException e) {
            view.displayMessage("continent: " + s + " is not valid " + e.getMessage());
            return Optional.empty();
        }
    }


    Set<Country> readCountriesFromFile(String part) {
        String countryInfo = StringUtils.substringAfter(part, "]\n");
        Set<Country> countriesSet = Optional.of(StringUtils.split(countryInfo, "\n"))
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map(this::createCountryFromFile)
                .filter(o -> o.isPresent())
                .map(Optional::get)
                .collect(Collectors.toSet());

        mapService.addCountry(countriesSet);

        return countriesSet;
    }

    private Optional<Country> createCountryFromFile(String s) {
        try {
            String[] countryInfo = StringUtils.split(s, " ");

            if (countryInfo.length != 5) {
                throw new InvalidObjectException("country: " + s + " is not valid for missing information");
            }

            int countryId = Integer.parseInt(countryInfo[0]);
            String countryName = countryInfo[1];
            int continentId = Integer.parseInt(countryInfo[2]);
            int coordinateX = Integer.parseInt(countryInfo[3]);
            int coordinateY = Integer.parseInt(countryInfo[4]);

            if (!mapService.continentIdExist(continentId)) {
                view.displayMessage("country: " + s + " contains invalid continent information");
                return Optional.empty();
            }


            Country country = new Country(countryId, countryName);
            country.setContinentIdentifier(continentId);
            country.setCoordinateX(coordinateX);
            country.setCoordinateY(coordinateY);
            return Optional.of(country);

        } catch (InvalidObjectException e) {
            view.displayMessage(e.getMessage());
        } catch (NumberFormatException e) {
            view.displayMessage("country: " + s + " is not valid " + e.getMessage());
        }

        return Optional.empty();
    }

    Map<Integer, Set<Integer>> readAdjascentCountriesFromFile(String part) {

        String borderInfo = StringUtils.substringAfter(part, "]");

        String[] adjacencyInfo = StringUtils.split(borderInfo, "\n\r");
        Map<Integer, Set<Integer>> adjacencyMap = new HashMap<>();

        Arrays.stream(adjacencyInfo)
                .map(this::createAdjacencyCountries)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(list -> {
                    int countryId = list.get(0);
                    Set<Integer> adjacencyId = new HashSet<>(list.subList(1, list.size()));
                    adjacencyMap.put(countryId, adjacencyId);
                });

        mapService.addNeighboringCountries(adjacencyMap);

        return adjacencyMap;
    }


    private Optional<List<Integer>> createAdjacencyCountries(String s) {
        try {

            List<String> adjacency = Arrays.asList(StringUtils.split(s, " "));

            if (adjacency.size() <= 1) {
                throw new IllegalArgumentException("adjacency: " + s + " is not valid for not having adjacent countries ");
            }

            adjacency.stream().map(Integer::parseInt)
                    .filter(this::isCountryIdNotValid)
                    .findFirst()
                    .ifPresent(invalidId -> {
                        throw new IllegalArgumentException("adjacencyL " + s + " is not valid for the country id does not exist");
                    });

            List<Integer> list = adjacency.stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            return Optional.of(list);


        } catch (NumberFormatException e) {
            view.displayMessage("adjacency: " + s + " is not valid " + e.getMessage());
        } catch (IllegalArgumentException e) {
            view.displayMessage(e.getMessage());
        }

        return Optional.empty();

    }

    private boolean isCountryIdNotValid(int id) {
        return !mapService.countryIdExist(id);
    }


    public void createFile(String path) {

    }

}
