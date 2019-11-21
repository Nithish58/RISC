package com6441.team7.risc.controller;

import com6441.team7.risc.api.exception.*;
import com6441.team7.risc.api.model.*;
import com6441.team7.risc.utils.parser.*;
import com6441.team7.risc.view.GameView;
import com6441.team7.risc.view.PhaseView;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

/**
 * This class handles the map editor commands from user input.
 * <p>
 * It calls the methods in mapService.
 */

public class MapLoaderController implements Controller {
    private MapCategory mapCategory;

    /**
     * a reference of mapService
     */
    private MapService mapService;


    /**
     * a reference of gameView
     */
    private GameView view;

    /**
     * generates id for continents
     */
    private AtomicInteger continentIdGenerator;

    /**
     * generates id for countries
     */
    private AtomicInteger countryIdGenerator;

    private MapParserAdapter mapParserAdapter;



    public MapLoaderController(MapService mapService) {
        this.mapService = mapService;
        this.view = new PhaseView();
        this.mapCategory = MapCategory.DOMINATION;
        this.continentIdGenerator = new AtomicInteger();
        this.countryIdGenerator = new AtomicInteger();
        this.mapParserAdapter = new MapParserAdapter(continentIdGenerator, countryIdGenerator);
    }

    @Override
    public void readCommand(String command) throws Exception {
        RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);

        String[] commands = {};

        if (command.toLowerCase(Locale.CANADA).contains("-add") || command.toLowerCase(Locale.CANADA).contains("-remove")) {

            command = StringUtils.substringAfter(command, "-");
            commands = command.split("\\s-");
        }

        switch (commandType) {
            case EDIT_MAP:
                editMap(command);
                break;
            case EDIT_CONTINENT:
                editContinents(commands);
                break;
            case EDIT_COUNTRY:
                editCountries(commands);
                break;
            case EDIT_NEIGHBOR:
                editNeighbors(commands);
                break;
            case SHOW_MAP:
                mapParserAdapter.showMap(mapCategory, view, mapService);
                break;
            case SAVE_MAP:
                saveMap(command);
                break;
            case VALIDATE_MAP:
                validateMap();
                break;
            case EXIT_MAPEDIT:
                exitEditMap();
                break;
            case EXIT:
                endGame();
                break;

            default:
                throw new IllegalArgumentException("cannot recognize this command");
        }
    }


    /**
     * exit the map editing
     */
    private void exitEditMap() {
        if (mapService.isMapNotValid()) {
            view.displayMessage("Map Not Valid");
        }
        this.mapService.setState(GameState.LOAD_GAME);
    }


    /**
     * get the MapService object
     *
     * @return MapService
     */
    public MapService getMapService() {
        return mapService;
    }

    /**
     * set the command view
     *
     * @param view view to this view
     */
    public void setView(GameView view) {
        this.view = view;
    }


    /**
     * end the game
     * called when only 1 player is present.
     */
    private void endGame() {
        view.displayMessage("Game Ends");
        System.exit(0);
    }

    /**
     * delete whitespace and lower cases the string
     *
     * @param name
     * @return
     */
    private String convertFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase(Locale.CANADA);
    }


    private void editMap(String s) {

        emptyMap();
        String[] commands = StringUtils.split(s, " ");


        if (containsConquestInCommand(commands)) {

            if (isConquestMapReadable(commands[1], view, mapService)) {
                mapCategory = MapCategory.CONQUEST;
                if (validateMap()) {
                    view.displayMessage("conquest map format: map is valid");
                }
            }
            return;
        }


        if (mapParserAdapter.readDominateMapFile(commands[1], view, mapService)) {
            mapCategory = MapCategory.DOMINATION;
            if(validateMap()){
                view.displayMessage("domination map format: map is valid");
            }
            return;
        }

        emptyMap();

        if (mapParserAdapter.readConquestMapFile(commands[1], view, mapService)) {
            mapCategory = MapCategory.CONQUEST;
            view.displayMessage("conquest map format: map is valid");
            return;
        }

        view.displayMessage("cannot recognize the map format");

    }

    private boolean notContainsConquestInCommand(String[] commands) {
        return commands.length == 2;
    }

    private boolean containsConquestInCommand(String[] commands) {
        return commands.length == 3 && convertFormat(commands[2]).equals(MapCategory.CONQUEST.getName());
    }

    private boolean isConquestMapReadable(String fileName, GameView view, MapService mapService) {
        return mapParserAdapter.readConquestMapFile(fileName, view, mapService);
    }


    private void emptyMap() {
        mapService.emptyMap();
        countryIdGenerator.set(0);
        continentIdGenerator.set(0);
    }


    public boolean validateMap() {
        if (mapService.isMapValid()) {
            view.displayMessage("map is valid");
            return true;
        } else {
            view.displayMessage("map is not valid");
            return false;
        }
    }


    private void saveMap(String command) {

        if (mapService.isMapNotValid()) {
            throw new MapInvalidException("the map is not valid, cannot be saved");
        }

        try {
            String[] commands = command.split(" ");
            String filename = commands[1];

            if (commands.length == 3 && commands[2].equalsIgnoreCase(MapCategory.CONQUEST.getName())) {
                if (mapParserAdapter.saveConquestMap(filename, mapService)) {
                    view.displayMessage("successfully save in conquest format");
                    return;
                }

                view.displayMessage("sorry, can not be saved");
                return;

            }


            if (mapParserAdapter.saveDominateMap(filename, mapService)) {
                view.displayMessage("sucessfully save in domination format");
                return;
            }

            view.displayMessage("sorry, cannot be saved");


        } catch (Exception e) {
            view.displayMessage("cannot save: " + e.getMessage());
        }


    }


    private void editContinents(String[] s) {
        Arrays.stream(s).forEach(this::editContinentFromUserInput);

    }

    /**
     * editcontinent command. If command is add, call addcontinent method, if remove, call removecontinent method
     *
     * @param s editcontinent command
     */
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
            if (e instanceof RiscGameException) {
                view.displayMessage(e.getMessage());
                return;
            }
            view.displayMessage("Unknown exception");
        }
    }

    /**
     * add continent. if success, add the continent to the mapService. if not, throw an exception
     *
     * @param s command arrays
     */
    private void addContinent(String[] s) {
        try {
            String continentName = convertFormat(s[1]);
            int continentPower = Integer.parseInt(s[2]);
            if (mapService.continentNameExist(continentName)) {
                view.displayMessage("the continent already exisits");
                return;
            }

            int continentNum = mapService.getContinents().size();
            Continent continent;

            if (continentNum != 0) {
                int largestId = mapService.getContinents().stream()
                        .max(Comparator.comparing(Continent::getId))
                        .get().getId();

                continent = new Continent(largestId + 1, continentName, continentPower);
            } else {
                continent = new Continent(continentIdGenerator.incrementAndGet(), continentName, continentPower);
            }

            continent.setColor("null");
            mapService.addContinent(continent);
            view.displayMessage("continent has been successfully added");
        } catch (Exception e) {
            throw new ContinentEditException("edit continent command: cannot add it is not valid", e);
        }
    }

    /**
     * remove continent. if success, remove the continent to the mapService. if not, throw an exception
     *
     * @param s command arrays
     */
    private void removeContinent(String[] s) {
        String continentName = convertFormat(s[1]);
        if (mapService.continentNameExist(continentName)) {
            mapService.removeContinentByName(continentName);
            view.displayMessage("successfully removes the continent");
            return;
        }
        throw new ContinentEditException("cannot remove continent");
    }


    private void editCountries(String[] s) {
        Arrays.stream(s).forEach(this::editCountryFromUserInput);
    }

    /**
     * handle editcountry command. if command is add, call addCountry method.
     * if command is remove, call removeCountry method.
     * else throw an exception
     *
     * @param s command
     */
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
                throw new ContinentEditException("The editCountry command " + s + " is not valid.");
        }
    }

    /**
     * receive remove country commands
     *
     * @param s string array remove country
     */
    private void removeCountry(String[] s) {
        try {
            String countryName = convertFormat(s[1]);
            if (mapService.countryNameNotExist(countryName)) {
                view.displayMessage("editcountry -remove command: The country does not exist");
                return;
            }
            mapService.removeCountryByName(countryName);
            view.displayMessage("the country is successfully removed");
        } catch (Exception e) {
            throw new CountryEditException("cannot remove country, editcountry -remove command is not valid");
        }
    }

    /**
     * addcountry command. If valid, add the country to the mapService.
     * if the country exist already, send an error message to view.
     * if the continent does not exist, send an error message to view.
     *
     * @param s command array
     */
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

        int countryNum = mapService.getCountries().size();

        Country country;
        if (countryNum == 0) {
            country = new Country(countryIdGenerator.incrementAndGet(), countryName, continentName);

        } else {
            int largestId = mapService.getCountries().stream()
                    .max(Comparator.comparing(Country::getId))
                    .get().getId();
            country = new Country(largestId + 1, countryName, continentName);
        }

        int continentId = mapService.findCorrespondingIdByContinentName(continentName).get();
        country.setContinentIdentifier(continentId);
        country.setCoordinateX(0);
        country.setCoordinateY(0);

        mapService.addCountry(country);
        view.displayMessage("the country is successfully added");
    }

    void editNeighbors(String[] s) {
        Arrays.stream(s).forEach(this::editNeighborFromUserInput);
    }

    /**
     * handle editNeighbor commands
     *
     * @param s eidtneighbor command
     */
    void editNeighborFromUserInput(String s) {
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

    /**
     * handle add neighbor commands
     *
     * @param s the string array for removing neighbor commands
     */
    private void addNeighbor(String[] s) {
        try {
            String countryName = convertFormat(s[1]);
            String neighborCountry = convertFormat(s[2]);

            if (mapService.countryNameExist(countryName) && mapService.countryNameExist(neighborCountry)) {
                mapService.addNeighboringCountries(countryName, neighborCountry);
                view.displayMessage("neighbor country been successfully added");
                return;
            }

            view.displayMessage("cannot add neighboring countries : the country does not exist");

        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            view.displayMessage("cannot add continent, the command is not valid");
        }
    }

    /**
     * handle remove neighbor commands
     *
     * @param s the string array for removing neighbor commands
     */
    public void removeNeighbor(String[] s) {
        try {
            String countryName = convertFormat(s[1]);
            String neighborCountry = convertFormat(s[2]);

            if (mapService.countryNameExist(countryName) && mapService.countryNameExist(neighborCountry)) {
                mapService.removeNeighboringCountriesByName(countryName, neighborCountry);
                view.displayMessage("neighbor country been successfully removed");
                return;
            }

            view.displayMessage("cannot remove neighboring countries : the country does not exist");

        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            view.displayMessage("cannot add continent, the command is not valid");
        }
    }


    private boolean isDominationMap() {
        return mapCategory.equals(MapCategory.DOMINATION);
    }

    private boolean isConquestMap() {
        return mapCategory.equals(MapCategory.CONQUEST);
    }

    public void setContinentIdGenerator(int i) {
        continentIdGenerator.set(i);
    }

    public void setCountryIdGenerator(int i) {
        countryIdGenerator.set(i);
    }

    public void readFile(String path) {
        editMap("editmap " + path);
    }
}