package com6441.team7.risc.controller;

import com6441.team7.risc.api.model.*;
import com6441.team7.risc.view.CommandPromptView;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapLoaderController {
    private AtomicInteger continentIdGenerator;
    private AtomicInteger countryIdGenerator;
    private StateContext stateContext;
    private CommandPromptView view;
    private MapService mapService;

    public MapLoaderController(StateContext stateContext, CommandPromptView view) {
        this.stateContext = stateContext;
        this.view = view;
        this.mapService = new MapService();
        this.continentIdGenerator = new AtomicInteger();
        this.countryIdGenerator = new AtomicInteger();
    }

    public MapService loadMap() {
        if (stateContext.getState().equals("mapLoader")) {
            readCommand();
        }

        return mapService;
    }

    void readCommand() {
        try {
            String command = view.receiveCommand();
            String commandType = convertFormat(StringUtils.split(" ")[0]);

            command = StringUtils.substringAfter(command, "-");
            String[] commands = StringUtils.split(command, "-");


            switch (commandType) {
                case "editcontinent":
                    editContinents(commands);
                    break;
                case "editcountry":
                    editCountries(commands);
                    break;
                case "editneighbor":
                    editNeighbor(commands);
                    break;
                case "showmap":
                    showMap();
                    break;
                case "savemap":
                    break;
                case "editmap":
                    editMap(command);
                    break;
                case "validatemap":
                    validateMap();
                    break;
                default:
                    throw new IllegalArgumentException("cannot recognize this command");
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            view.displayMessage("map editor: " + e.getMessage());
        }

    }

    private void showMap() {
        mapService.printCountryInfo();
        mapService.printContinentInfo();
        mapService.printNeighboringCountryInfo();
    }



    private void validateMap() {

        boolean connected = mapService.isStronlyConnectec();
        if(connected){
            State state = new StartUpState();
            stateContext.setState(state);
            return;
        }

        view.displayMessage("The map is not valid");
    }

    void editContinents(String[] s) {
        Arrays.stream(s).forEach(this::editContinentFromUserInput);
    }

    private void editContinentFromUserInput(String s) {
        try {

            String[] commands = StringUtils.split(s, " ");
            switch (convertFormat(commands[0])) {
                case "add":
                    addContinent(commands);
                    break;
                case "remove":
                    removeContinent(commands);
                    break;
                default:
                    throw new IllegalArgumentException("The editcontinent command " + s + " is not valid.");
            }
        } catch (IndexOutOfBoundsException e) {
            view.displayMessage(e.getMessage());
        }
    }

    private void addContinent(String[] s) {
        try {
            String continentName = convertFormat(s[1]);
            int continentPower = Integer.parseInt(s[2]);

            if (!mapService.continentNameExist(continentName)) {
                Continent continent = new Continent(continentIdGenerator.incrementAndGet(), continentName, continentPower);
                mapService.addContinent(continent);
            }

        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            view.displayMessage("cannot add continent, the command is not valid");
        }
    }

    private void removeContinent(String[] s) {
        try {
            String continentName = convertFormat(s[1]);

            if (mapService.continentNameExist(continentName)) {
                mapService.removeContinentByName(continentName);
            }
            else{
                view.displayMessage("the continent " + continentName + " does not exist.");
            }

        } catch (IndexOutOfBoundsException e) {
            view.displayMessage("cannot remove continent, the command is not valid");
        }

    }


    void editCountries(String[] s){
        Arrays.stream(s).forEach(this::editCountryFromUserInput);
    }

    private void editCountryFromUserInput(String s) {
        try {

            String[] commands = StringUtils.split(s, " ");
            switch (convertFormat(commands[0])) {
                case "add":
                    addCountry(commands);
                    break;
                case "remove":
                    removeCountry(commands);
                    break;
                default:
                    throw new IllegalArgumentException("The editCountry command " + s + " is not valid.");
            }
        } catch (IndexOutOfBoundsException e) {
            view.displayMessage(e.getMessage());
        }
    }

    private void addCountry(String[] s){
        try {
            String countryName = convertFormat(s[1]);
            String continentName = convertFormat(s[2]);

            if (!mapService.countryNameExist(countryName) && mapService.continentNameExist(continentName)) {
                Country country = new Country(countryIdGenerator.incrementAndGet(), countryName, continentName);
                mapService.addCountry(country);
                return;
            }

            if(mapService.countryNameExist(countryName)){
                view.displayMessage("editcountry command: The country already exists");
                return;
            }

            if(mapService.continentNameExist(continentName)){
                view.displayMessage("editcountry command: The continent info is not valid");
            }

        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            view.displayMessage("cannot add continent, the command is not valid");
        }
    }

    private void removeCountry(String[] s){
        try {
            String countryName = convertFormat(s[1]);

            if (mapService.countryNameExist(countryName)) {
                mapService.removeCountryByName(countryName);
                return;
            }

            view.displayMessage("editcountry -remove command: The country does not exist");

        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            view.displayMessage("cannot remove country, editcountry -remove command is not valid");
        }
    }


    void editNeighbor(String[] s) {
        Arrays.stream(s).forEach(this::editNeighborFromUserInput);
    }

    void editNeighborFromUserInput(String s){
        try {
            String[] commands = StringUtils.split(s, " ");
            switch (convertFormat(commands[0])) {
                case "add":
                    addNeighbor(commands);
                    break;
                case "remove":
                    removeNeighbor(commands);
                    break;
                default:
                    throw new IllegalArgumentException("The editCountry command " + s + " is not valid.");
            }
        } catch (IndexOutOfBoundsException e) {
            view.displayMessage(e.getMessage());
        }
    }

    private void addNeighbor(String[] s){
        try {
            String countryName = convertFormat(s[1]);
            String neighborCountry = convertFormat(s[2]);

            if (mapService.countryNameExist(countryName) && mapService.countryNameExist(neighborCountry)) {
                mapService.addNeighboringCountries(countryName, neighborCountry);
                return;
            }

            view.displayMessage("cannot add neighboring countries : the country does not exist");

        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            view.displayMessage("cannot add continent, the command is not valid");
        }
    }

    public void removeNeighbor(String[] s){
        try{
            String countryName = convertFormat(s[1]);
            String neighborCountry = convertFormat(s[2]);

            if (mapService.countryNameExist(countryName) && mapService.countryNameExist(neighborCountry)) {
                mapService.removeNeighboringCountriesByName(countryName, neighborCountry);
                return;
            }

            view.displayMessage("cannot remove neighboring countries : the country does not exist");

        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            view.displayMessage("cannot add continent, the command is not valid");
        }
    }

    Optional<String> editMap(String s) {
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


    Optional<String> readFile(String name) {
        try {
            String file = FileUtils.readFileToString(new File(name), StandardCharsets.UTF_8);

            parseFile(file);
            return Optional.of(file);

        } catch (IOException e) {
            //String file = createFile(name);
        }

        return Optional.empty();
    }


    private void setGameStartUpState() {
        State startup = new StartUpState();
        stateContext.setState(startup);
        view.displayMessage("map is successfully loaded, game starts");
    }


    boolean parseFile(String s) {
        String[] parts = StringUtils.split(s, "[");

        try{
            parseRawContinents(parts[2]);
            parseRawCountries(parts[3]);
            parseRawNeighboringCountries(parts[4]);

         } catch (IndexOutOfBoundsException e){
            view.displayMessage("The file is not");
        }

        return mapService.isStronlyConnectec();

    }


    Set<Continent> parseRawContinents(String part) {
        String continentInfo = StringUtils.substringAfter(part, "]\n");

        Set<Continent> continentSet = Optional.of(StringUtils.split(continentInfo, "\n"))
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map(this::createContinentFromRaw)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        mapService.addContinent(continentSet);

        return continentSet;

    }

    private Optional<Continent> createContinentFromRaw(String s) {

        try {
            String[] continentInfo = StringUtils.split(s, " ");

            String name = convertFormat(continentInfo[0]);
            int continentValue = Integer.parseInt(continentInfo[1]);

            Continent continent = new Continent(continentIdGenerator.incrementAndGet(), name, continentValue);

            return Optional.of(continent);

        } catch (IndexOutOfBoundsException e) {
            view.displayMessage(e.getMessage());
            return Optional.empty();
        } catch (NumberFormatException e) {
            view.displayMessage("continent: " + s + " is not valid " + e.getMessage());
            return Optional.empty();
        }
    }


    Set<Country> parseRawCountries(String part) {
        String countryInfo = StringUtils.substringAfter(part, "]\n");
        Set<Country> countrySet = Optional.of(StringUtils.split(countryInfo, "\n"))
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map(this::createCountryFromRaw)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        mapService.addCountry(countrySet);
        return countrySet;
    }

    private Optional<Country> createCountryFromRaw(String s) {
        try {
            String[] countryInfo = StringUtils.split(s, " ");

            int countryId = Integer.parseInt(countryInfo[0]);
            String countryName = convertFormat(countryInfo[1]);
            int continentId = Integer.parseInt(countryInfo[2]);

            if (!mapService.continentIdExist(continentId)) {
                view.displayMessage("country: " + s + " contains invalid continent information");
                return Optional.empty();
            }

            Country country = new Country(countryId, countryName, continentId);
            return Optional.of(country);

        } catch (IndexOutOfBoundsException e) {
            view.displayMessage(e.getMessage());
        } catch (NumberFormatException e) {
            view.displayMessage("country: " + s + " is not valid " + e.getMessage());
        }

        return Optional.empty();
    }

    Map<Integer, Set<Integer>> parseRawNeighboringCountries(String part) {

        String borderInfo = StringUtils.substringAfter(part, "]");

        String[] adjacencyInfo = StringUtils.split(borderInfo, "\n\r");
        Map<Integer, Set<Integer>> adjacencyMap = new HashMap<>();

        Arrays.stream(adjacencyInfo)
                .map(this::createAdjacencyCountriesFromRaw)
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


    private Optional<List<Integer>> createAdjacencyCountriesFromRaw(String s) {
        try {

            List<String> adjacency = Arrays.asList(StringUtils.split(s, " "));

            throwWhenNoNeighboringCountry(s, adjacency);

            throwWhenNeighbouringCountriesIdInvalid(s, adjacency);

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

    private void throwWhenNeighbouringCountriesIdInvalid(String s, List<String> adjacency) {
        adjacency.stream().map(Integer::parseInt)
                .filter(this::isCountryIdNotValid)
                .findFirst()
                .ifPresent(invalidId -> {
                    throw new IllegalArgumentException("adjacencyL " + s + " is not valid for the country id does not exist");
                });
    }

    private void throwWhenNoNeighboringCountry(String s, List<String> adjacency) {
        if (adjacency.size() <= 1) {
            throw new IllegalArgumentException("adjacency: " + s + " is not valid for not having adjacent countries ");
        }
    }

    private boolean isCountryIdNotValid(int id) {
        return !mapService.countryIdExist(id);
    }


    //    public String createFile(String name) {
//        view.displayMessage("The map does not exist, we create a new file named " + name);
//        File file = new File(name);
//        return file.getName();
//    }
//
//    private void editMap() {
//        filterCommmand();
//    }
//
//    public void filterCommmand() {
//        String command = view.receiveCommand();
//        try{
//            String[] parts = StringUtils.split(command, " ");
//
//            if(convertFormat(parts[0]).equals("editmap")){
//                loadMap();
//            }
//            else if(convertFormat(parts[0]).equals("editcontinent")){
//
//            }
//
//
//        }catch (IndexOutOfBoundsException e){
//            System.out.println("sdfsdf");
//        }
//        catch (IllegalArgumentException e){
//
//        }
//    }
//
    private String convertFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase();
    }

    public MapService getMapService(){
        return mapService;
    }


}
