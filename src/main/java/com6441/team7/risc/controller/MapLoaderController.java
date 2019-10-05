package com6441.team7.risc.controller;

import com6441.team7.risc.api.exception.*;
import com6441.team7.risc.api.model.*;
import com6441.team7.risc.view.CommandPromptView;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com6441.team7.risc.api.RiscContants.EOL;
import static com6441.team7.risc.api.RiscContants.WHITESPACE;
import static java.util.Objects.isNull;

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
            RiscCommand commandType = RiscCommand.parse(StringUtils.split(" ")[0]);

            command = StringUtils.substringAfter(command, "-");
            String[] commands = StringUtils.split(command, "-");

            switch (commandType) {
                case EDIT_CONTINENT:
                    editContinents(commands);
                    break;
                case EDIT_COUNTRY:
                    editCountries(commands);
                    break;
                case EDIT_NEIGHBOR:
                    editNeighbor(commands);
                    break;
                case SHOW_MAP:
                    showMap();
                    break;
                case SAVE_MAP:
                    saveMap(command);
                    break;
                case EDIT_MAP:
                    editMap(command);
                    break;
                case VALIDATE_MAP:
                    isMapValid();
                    break;
                default:
                    throw new IllegalArgumentException("cannot recognize this command");
            }
        } catch (RiscGameException e) {
                view.displayMessage(e.getMessage());
        } catch (IOException e) {
            view.displayMessage("Cannot read map");
        }
    }

    private void showMap() {
        mapService.printCountryInfo();
        mapService.printContinentInfo();
        mapService.printNeighboringCountryInfo();
    }

    private boolean isMapValid() {
        if(isStronglyConnected()){
            setGameStartUpState();
            return true;
        }
        view.displayMessage("The map is not valid");
        return false;
    }

    private boolean isMapNotValid() {
        return !isMapValid();
    }

    private boolean isStronglyConnected() {
        int totalCountry = mapService.getCountries().size();
        return new KosarajuStrongConnectivityInspector<>(mapService.getDirectedGraph())
                .getStronglyConnectedComponents()
                .stream()
                .map(Graph::vertexSet)
                .map(Set::size)
                .allMatch(num -> num == totalCountry);
    }

    public void saveMap(String command) throws IOException {
        if (isMapNotValid()) {
            throw new MapInvalidException("the map is not valid");
        }
        String filename = command.split(" ")[1];
        StringBuilder stringBuilder =
                new StringBuilder()
                        .append(filename)
                        .append("\r\n")
                        .append("[files]\r\n")
                        .append("[continent]\r\n")
                        .append(getContinentString())
                        .append("[countries]\r\n")
                        .append(getCountryString())
                        .append("[borders]\r\n")
                        .append(getBorderString());

        File file = new File(filename);
        FileUtils.writeStringToFile(file, stringBuilder.toString(), StandardCharsets.UTF_8.name());
    }

    private String getContinentString(){
    StringBuilder stringBuilder = new StringBuilder();
        mapService.getContinents().forEach(continent -> {
            stringBuilder.append(continent.getName()).append(" ");
            stringBuilder.append(continent.getContinentValue()).append(" ");
            stringBuilder.append(continent.getColor()).append("\r\n");

        });

        return stringBuilder.toString();
    }

    private String getCountryString() {
        return mapService.getCountries()
                .stream()
                .map(Country::toString)
                .reduce(String::concat)
                .orElseThrow(RuntimeException::new);
    }

    private String getBorderString(){
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<Integer, Set<Integer>> entry : mapService.getAdjacencyCountriesMap().entrySet()) {
            stringBuilder.append(entry.getKey()).append(WHITESPACE);
            for (Integer integer : entry.getValue()) {
                stringBuilder.append(integer).append(WHITESPACE);
            }
            stringBuilder.append(EOL);
        }
        return stringBuilder.toString();
    }

    void  editContinents(String[] s) {
        Arrays.stream(s).forEach(this::editContinentFromUserInput);
    }

    private void editContinentFromUserInput(String s) {
        try {

            String[] commands = StringUtils.split(s, WHITESPACE);
            switch (convertFormat(commands[0])) {
                case "add":
                    addContinent(commands);
                    break;
                case "remove":
                    removeContinent(commands);
                    break;
                default:
                    throw new ContinentEditException("The editcontinent command " + s + " is not valid.");
            }
        } catch (Exception e) {
            if(e instanceof RiscGameException) {
                view.displayMessage(e.getMessage());
                return;
            }
            view.displayMessage("Unknown exception");
        }
    }

    private void addContinent(String[] s) {
        try {
            String continentName = convertFormat(s[1]);
            int continentPower = Integer.parseInt(s[2]);
            if (mapService.continentNameExist(continentName)) {
                return;
            }
            Continent continent = new Continent(continentIdGenerator.incrementAndGet(), continentName, continentPower);
            continent.setColor("null");
            mapService.addContinent(continent);
        } catch (Exception e) {
            throw new ContinentEditException("edit continent command: cannot add it is not valid", e);
        }
    }

    private void removeContinent(String[] s) {
            String continentName = convertFormat(s[1]);
            if (mapService.continentNameExist(continentName)) {
                mapService.removeContinentByName(continentName);
                return;
            }
            throw new ContinentEditException("edit continent command: cannot remove continent, the command is not valid");
    }


    void editCountries(String[] s){
        Arrays.stream(s).forEach(this::editCountryFromUserInput);
    }

    private void editCountryFromUserInput(String s) {
        String[] commands = StringUtils.split(s, " ");
        switch (convertFormat(commands[0])) {
            case "add":
                addCountry(commands);
                break;
            case "remove":
                removeCountry(commands);
                break;
            default:
                throw new CountryEditException("The editCountry command " + s + " is not valid.");
        }
    }

    private void addCountry(String[] s) {
        String countryName = convertFormat(s[1]);
        String continentName = convertFormat(s[2]);

        if (mapService.countryNameExist(countryName)) {
            view.displayMessage("editcountry command: The country already exists");
            return;
        }
        if (mapService.continentNameNotExist(continentName)) {
            view.displayMessage("editcountry command: The continent info is not valid");
            return;
        }

        Country country = new Country(countryIdGenerator.incrementAndGet(), countryName, continentName);
        int continentId = mapService.findCorrespondingIdByContinentName(continentName).get();
        country.setContinentIdentifier(continentId);
        country.setCoordinateX(0);
        country.setCoordinateY(0);

        mapService.addCountry(country);
    }

    private void removeCountry(String[] s) {
        try {
            String countryName = convertFormat(s[1]);
            if (mapService.countryNameNotExist(countryName)) {
                view.displayMessage("editcountry -remove command: The country does not exist");
                return;
            }
            mapService.removeCountryByName(countryName);
        } catch (Exception e) {
            throw new CountryEditException("cannot remove country, editcountry -remove command is not valid");
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

    Optional<String>  editMap(String s) {
        String[] commands = StringUtils.split(s, " ");

        if (commands.length != 2) {
            view.displayMessage("The command editmap is not valid");
            return Optional.empty();
        }

        String command = commands[0];
        String path = commands[1];

        if (command.toLowerCase(Locale.CANADA).equals("editmap")) {
            return Optional.of(path);
        }

        view.displayMessage("The command editmap is not valid");
        return Optional.empty();
    }


    Optional<String> readFile(String name) {
        try {
            String file = FileUtils.readFileToString(new File(name), StandardCharsets.UTF_8);

            parseFile(file);
            return Optional.of(file);

        } catch (IOException e) {
            createFile(name);
        }

        return Optional.empty();
    }

    void createFile(String name){
        File file = new File(name);
        try{
            FileUtils.writeStringToFile(file, "", StandardCharsets.UTF_8.name());
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private void setGameStartUpState() {
        State startup = new StartUpState();
        stateContext.setState(startup);
        view.displayMessage("map is successfully loaded, game starts");
    }


    boolean parseFile(String s) {
        String[] parts = StringUtils.split(s, "[");

        try{
            if(parts.length != 5){
                throw new MissingInfoException("The file missing information");
            }

        parseRawContinents(parts[2]);
        parseRawCountries(parts[3]);
        parseRawNeighboringCountries(parts[4]);

        } catch (Exception e){
            view.displayMessage(e.getMessage());
            return false;
        }

        return isStronglyConnected();

    }


    Set<Continent> parseRawContinents(String part) {
        String continentInfo = StringUtils.substringAfter(part, "]\r\n");

        Set<Continent> continentSet = Optional.of(StringUtils.split(continentInfo, "\r\n"))
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map(this::createContinentFromRaw)
                .collect(Collectors.toSet());

        mapService.addContinent(continentSet);

        return continentSet;

    }

    private Continent createContinentFromRaw(String s) {

        try {

            String[] continentInfo = StringUtils.split(s, " ");

            if(isNull(continentInfo) || continentInfo.length != 3){
                throw new ContinentParsingException("continent: " + s + " is not valid ");
            }

            String name = convertFormat(continentInfo[0]);
            int continentValue = Integer.parseInt(continentInfo[1]);
            String color = convertFormat(continentInfo[2]);

            Continent continent = new Continent(continentIdGenerator.incrementAndGet(), name, continentValue);
            continent.setColor(color);

            return continent;

        } catch (NumberFormatException e) {
            throw new ContinentParsingException(e);
        }

    }


    Set<Country> parseRawCountries(String part) {
        String countryInfo = StringUtils.substringAfter(part, "]\r\n");
        Set<Country> countrySet = Optional.of(StringUtils.split(countryInfo, "\r\n"))
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map(this::createCountryFromRaw)
                .collect(Collectors.toSet());

        mapService.addCountry(countrySet);
        return countrySet;
    }

    private Country createCountryFromRaw(String s) {
        try {
            String[] countryInfo = StringUtils.split(s, " ");

            if(countryInfo.length != 5){
                throw new CountryParsingException("country: " + s + " is not valid.");
            }

            int countryId = Integer.parseInt(countryInfo[0]);
            String countryName = convertFormat(countryInfo[1]);
            int continentId = Integer.parseInt(countryInfo[2]);
            int coordinateX = Integer.parseInt(countryInfo[3]);
            int coordinateY = Integer.parseInt(countryInfo[4]);

            if (mapService.continentIdNotExist(continentId)) {
                throw new CountryParsingException("country: " + s + " contains invalid continent information");
            }

            Country country = new Country(countryId, countryName, continentId);
            country.setCoordinateX(coordinateX);
            country.setCoordinateY(coordinateY);

            return country;

        } catch (NumberFormatException e) {
           throw new CountryParsingException(e);
        }
    }

    Map<Integer, Set<Integer>> parseRawNeighboringCountries(String part) {

        String borderInfo = StringUtils.substringAfter(part, "]");

        String[] adjacencyInfo = StringUtils.split(borderInfo, "\n\r");
        Map<Integer, Set<Integer>> adjacencyMap = new HashMap<>();

        Arrays.stream(adjacencyInfo)
                .map(this::createAdjacencyCountriesFromRaw)
                .forEach(list -> {
                    int countryId = list.get(0);
                    Set<Integer> adjacencyId = new HashSet<>(list.subList(1, list.size()));
                    adjacencyMap.put(countryId, adjacencyId);
                });

        mapService.addNeighboringCountries(adjacencyMap);

        return adjacencyMap;
    }


    private List<Integer> createAdjacencyCountriesFromRaw(String s) {
            List<String> adjacency = Arrays.asList(StringUtils.split(s, " "));

            throwWhenNoNeighboringCountry(s, adjacency);
            throwWhenNeighbouringCountriesIdInvalid(s, adjacency);

           return adjacency.stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
    }

    private void throwWhenNeighbouringCountriesIdInvalid(String s, List<String> adjacency) {
        adjacency.stream()
                .map(rawInt -> {
                    if(!NumberUtils.isDigits(rawInt)) {
                        throw new NeighborParsingException("adjacency: " + s +" Element " + rawInt + "is not valid");
                    }
                    return Integer.parseInt(rawInt);
                })
                .filter(this::isCountryIdNotValid)
                .findFirst()
                .ifPresent(invalidId -> {
                    throw new NeighborParsingException("adjacency: " + s + " is not valid for the country id does not exist");
                });
    }

    private void throwWhenNoNeighboringCountry(String s, List<String> adjacency) {
        if (adjacency.size() <= 1) {
            throw new NeighborParsingException("adjacency: " + s + " is not valid for not having adjacent countries ");
        }
    }

    private boolean isCountryIdNotValid(int id) {
        return !mapService.countryIdExist(id);
    }

    private String convertFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase(Locale.CANADA);
    }

    public MapService getMapService(){
        return mapService;
    }
}
