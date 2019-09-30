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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapLoaderController {
    private StateContext stateContext;
    private CommandPromptView view;
    private MapService mapService = new MapService();

    public MapLoaderController(StateContext stateContext, CommandPromptView view) {
        this.stateContext = stateContext;
        this.view = view;
    }

    public void loadMap() {
        if(stateContext.getState().equalsIgnoreCase("maploader")){
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
            view.displayMessage("The map does not exist");
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

            Continent continent = new Continent(continentInfo[0]);
            continent.setContinentValue(Integer.parseInt(continentInfo[1]));
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
                .filter(Optional::isPresent)
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

            Country country = new Country(Integer.parseInt(countryInfo[0]), countryInfo[1]);
            country.setContinentIdentifier(Integer.parseInt(countryInfo[2]));
            country.setCoordinateX(Integer.parseInt(countryInfo[3]));
            country.setCoordinateY(Integer.parseInt(countryInfo[4]));
            return Optional.of(country);

        } catch (InvalidObjectException e) {
            view.displayMessage(e.getMessage());
        } catch (NumberFormatException e) {
            view.displayMessage("country: " + s + " is not valid " + e.getMessage());
        }

        return Optional.empty();
    }

    Map<Integer, Set<Integer>> readAdjascentCountriesFromFile(String part){

        String borderInfo = StringUtils.substringAfter(part, "]");

        String[] adjacencyInfo = StringUtils.split(borderInfo, "\n\r");
        Map<Integer, Set<Integer>> adjacencyMap = new HashMap<>();

        for(String s: adjacencyInfo){
            if(createAdjascentCountries(s).isPresent()){
                List<Integer> list = createAdjascentCountries(s).get();
                int countryId = list.get(0);
                Set<Integer> adjacencyId = new HashSet<>(list.subList(1, list.size()));
                adjacencyMap.put(countryId, adjacencyId);
            }

        }

        return adjacencyMap;
    }


    private Optional<List<Integer>> createAdjascentCountries(String s){
        try {

            List<String> adjacency = Arrays.asList(StringUtils.split(s, " "));

            if(adjacency.size() <= 1){
                throw new InvalidObjectException("adjacency: " + s + " is not valid for not having adjacent countries ");
            }

            List<Integer> list = adjacency.stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            return Optional.of(list);


        } catch (InvalidObjectException e) {
            view.displayMessage(e.getMessage());
        } catch (NumberFormatException e) {
            view.displayMessage("adjacency: " + s + " is not valid " + e.getMessage());
        }

        return Optional.empty();

    }

}
