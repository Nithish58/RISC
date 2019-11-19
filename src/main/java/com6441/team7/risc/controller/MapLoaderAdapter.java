package com6441.team7.risc.controller;

import com6441.team7.risc.api.exception.*;
import com6441.team7.risc.api.model.*;
import com6441.team7.risc.view.GameView;
import com6441.team7.risc.view.PhaseView;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

/**
 * This class handles the map editor commands from user input.
 *
 * It calls the methods in mapService.
 */

public class MapLoaderAdapter implements Controller, IConquestReaderWriter, IDominationReaderWriter{

    private DominateReaderWriter dominateReaderWriter;
    private ConquestReaderWriter conquestReaderWriter;
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


    //add by jenny
    private IBuilder builder;
    public void setGameBuilder(IBuilder builder){
        this.builder = builder;
    }


    public MapLoaderAdapter(MapService mapService) {
        this.mapService = mapService;
        this.view = new PhaseView();
        this.mapCategory = MapCategory.UNKNOWN;
        this.continentIdGenerator = new AtomicInteger();
        this.countryIdGenerator = new AtomicInteger();

    }

    @Override
    public void readCommand(String command) throws Exception {
        RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);

        String[] commands = {};

        if(command.toLowerCase(Locale.CANADA).contains("-add") || command.toLowerCase(Locale.CANADA).contains("-remove")){

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
                showMap();
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


    @Override
    public void readConquestMapFile(String fileName) {
        conquestReaderWriter.readConquestMapFile(fileName);
    }


    @Override
    public void readDominateMapFile(String fileName) {
        dominateReaderWriter.readDominateMapFile(fileName);
    }


    @Override
    public void saveDominateMap(String fileName) throws IOException {
        dominateReaderWriter.saveDominateMap(fileName);
    }

    @Override
    public void saveConquestMap(String fileName) throws IOException {
        conquestReaderWriter.saveConquestMap(fileName);
    }

    @Override
    public void showDominateMap() {
        dominateReaderWriter.showDominateMap();
    }


    @Override
    public void showConquestMap() {
        conquestReaderWriter.showConquestMap();
    }


    /**
     * exit the map editing
     */
    private void exitEditMap(){
        if(mapService.isMapNotValid()) {
            view.displayMessage("Map Not Valid");
        }
        this.mapService.setState(GameState.START_UP);
    }


    /**
     * get the MapService object
     * @return MapService
     */
    public MapService getMapService() {
        return mapService;
    }

    /**
     * set the command view
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
     * @param name
     * @return
     */
    private String convertFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase(Locale.CANADA);
    }


    private void editMap(String s){

        mapService.emptyMap();
        countryIdGenerator.set(0);
        continentIdGenerator.set(0);
        String[] commands = StringUtils.split(s, " ");


        if(commands.length == 3 && convertFormat(commands[0]).equals(RiscCommand.EDIT_MAP.getName()) &&
                convertFormat(commands[2]).equals(MapCategory.CONQUEST.getName())){
            conquestReaderWriter = new ConquestReaderWriter(mapService, view, countryIdGenerator, continentIdGenerator);
            readConquestMapFile(commands[1]);
            validateMap();
            return;
        }

        if(commands.length == 2 && convertFormat(commands[0]).equals(RiscCommand.EDIT_MAP.getName())){
            dominateReaderWriter = new DominateReaderWriter(mapService, view, countryIdGenerator, continentIdGenerator);
            readDominateMapFile(commands[1]);
            validateMap();
            return;
        }

        view.displayMessage("The command editmap is not valid");

    }



    public boolean validateMap(){
        if(mapService.isMapValid()){
            view.displayMessage("map is valid");
            return true;
        }
        else{
            view.displayMessage("map is not valid");
            return false;
        }
    }



    private void saveMap(String command) throws IOException {

        if (mapService.isMapNotValid()) {
            throw new MapInvalidException("the map is not valid, cannot be saved");
        }
        String filename = command.split(" ")[1];

        if(mapCategory.equals(MapCategory.DOMINATION)){
            saveDominateMap(filename);
            return;
        }

        if(mapCategory.equals(MapCategory.CONQUEST)){
            saveConquestMap(filename);
            return;
        }

        view.displayMessage("cannot recognize the map category");
    }


    private void showMap(){
        if(mapCategory.equals(MapCategory.CONQUEST)){
            showConquestMap();
            return;
        }

        if(mapCategory.equals(MapCategory.DOMINATION)){
            showDominateMap();
            return;
        }

        view.displayMessage("cannot recognize the map category");

    }



    private void editContinents(String[] s) {
        Arrays.stream(s).forEach(this::editContinentFromUserInput);

    }

    /**
     * editcontinent command. If command is add, call addcontinent method, if remove, call removecontinent method
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






    private boolean isDominationMap(){
        return mapCategory.equals(MapCategory.DOMINATION);
    }

    private boolean isConquestMap(){
        return mapCategory.equals(MapCategory.CONQUEST);
    }
}
