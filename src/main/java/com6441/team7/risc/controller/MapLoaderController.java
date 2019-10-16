package com6441.team7.risc.controller;

import com6441.team7.risc.api.exception.*;
import com6441.team7.risc.api.model.*;
import com6441.team7.risc.view.CommandPromptView;
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

/**
 * This class handles the map editor commands from user input.
 *
 * It calls the methods in mapService.
 */

public class MapLoaderController {
	
	//Modified to public by keshav
    private AtomicInteger continentIdGenerator;
    private AtomicInteger countryIdGenerator;
    
    private MapService mapService;
    private CommandPromptView view;
    private MapGraph mapGraph;
    private MapIntro mapIntro;

    public MapLoaderController(MapService mapService) {
        this.mapService = mapService;
        this.continentIdGenerator = new AtomicInteger();
        this.countryIdGenerator = new AtomicInteger();
        this.mapGraph = new MapGraph();
        this.mapIntro = new MapIntro();
    }


    /**
     * read commands from user input to call different methods. If commands are not recognized, throw an exception
     * @param command
     * @throws IOException
     */
    public void readCommand(String command) throws IOException {

        RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);

        String[] commands = {};

        if(command.contains("-")){
            command = StringUtils.substringAfter(command, "-");
            commands = StringUtils.split(command, "-");
        }

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
            case SHOW_FILE:
                showMap();
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
                validateMap();
                break;
            case EXIT_MAPEDIT:
            	exitEditMap();
                break;
                
            default:
                throw new IllegalArgumentException("cannot recognize this command");
        }

    }

    private void exitEditMap(){
        if(mapService.isMapNotValid()) {
            System.out.println("Map Not Valid");
        }
        this.mapService.setState(GameState.START_UP);
    }

    /**
     * validate if the map is valid
     * @return true if valid false if not
     */
    private boolean validateMap() {

        if(mapService.isMapValid()){
            view.displayMessage("map is valid");
            return true;
        }
        else{
            view.displayMessage("map is not valid");
            return false;
        }
    }

    /**
     * show all countries, all continents, and their neighbors of the map
     */
    public void showMap() {
        mapService.printCountryInfo();
        mapService.printContinentInfo();
        mapService.printNeighboringCountryInfo();
    }


    /**
     * save the map, if the map is valid, it could be saved. if not, will throw an exception
     * @param command
     * @throws IOException
     */
    public void saveMap(String command) throws IOException {
        if (mapService.isMapNotValid()) {
            throw new MapInvalidException("the map is not valid, cannot be saved");
        }
        String filename = command.split(" ")[1];

        String mapIntro = getMapIntroString();
        if(mapIntro.length() == 0){
            mapIntro = filename;
        }
        StringBuilder stringBuilder =
                new StringBuilder()
                        .append(mapIntro)
                        .append(EOL)
                        .append("[files]")
                        .append(EOL)
                        .append(getMapGraphString())
                        .append("[continent]")
                        .append(EOL)
                        .append(getContinentString())
                        .append(EOL)
                        .append("[countries]")
                        .append(EOL)
                        .append(getCountryString())
                        .append(EOL)
                        .append("[borders]")
                        .append(EOL)
                        .append(getBorderString());

        File file = new File(filename);
        FileUtils.writeStringToFile(file, stringBuilder.toString(), StandardCharsets.UTF_8.name());
        view.displayMessage("the map is successfully saved.");
        
    //    mapService.setState(GameState.START_UP);
    }

    private String getMapIntroString(){
        return mapIntro.getMapIntro();
    }

    private String getMapGraphString(){
        return mapGraph.getMapGraph();
    }

    private String getContinentString() {
        StringBuilder stringBuilder = new StringBuilder();
        mapService.getContinents().forEach(continent -> {
            stringBuilder.append(continent.getName()).append(" ");
            stringBuilder.append(continent.getContinentValue()).append(" ");
            stringBuilder.append(continent.getColor()).append(EOL);

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

    private String getBorderString() {
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

    void editContinents(String[] s) {
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
            System.out.println("continent has been successfully added");
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



    void editCountries(String[] s) {
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


    void editNeighbor(String[] s) {
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
                System.out.println("neighbor country been successfully added");
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

    /**
     * handle editmap command from user
     * @param s command
     * @return if map has contents, will return map file name, else return empty
     */
    Optional<String> editMap(String s) {

        mapService.emptyMap();
        continentIdGenerator.set(0);
        countryIdGenerator.set(0);
        String[] commands = StringUtils.split(s, " ");

        if (commands.length != 2) {
            view.displayMessage("The command editmap is not valid");
            return Optional.empty();
        }

        String command = commands[0];
        String path = commands[1];

        if (command.toLowerCase(Locale.CANADA).equals("editmap")) {
            readFile(path);
            return Optional.of(path);
        }

        view.displayMessage("The command editmap is not valid");
        return Optional.empty();
    }


    /**
     * read an existing map
     * @param name the map file name
     * @return if map has contents, will return string, else return empty
     */
    public Optional<String> readFile(String name){
        try {
            URI uri = Paths.get(name).toUri();
            String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8.name());
            parseFile(file);
            return Optional.of(file);

        } catch (IOException|NullPointerException e) {
            createFile(name);
        }

        return Optional.empty();
    }

    /**
     * create a new map if the map file name does not exist
     * @param name
     */
    void createFile(String name) {
        File file = new File(name);
        try {
            FileUtils.writeStringToFile(file, "", StandardCharsets.UTF_8.name());
            System.out.println("a file " + file.getName() + " has been created.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * read existing map and create continent, country and its neighbors.
     * @param s exsting map as a string
     * @return
     */
    boolean parseFile(String s) {
        String[] parts = StringUtils.split(s, "[");

        try {
            if (parts.length != 5) {
                throw new MissingInfoException("The map is not valid");
            }

            parseMapIntro(parts[0]);
            parseMapGraphInfo(parts[1]);
            parseRawContinents(parts[2]);
            parseRawCountries(parts[3]);
            parseRawNeighboringCountries(parts[4]);

            validateMap();

        } catch (Exception e) {
            view.displayMessage(e.getMessage());
            return false;
        }

        return mapService.isStronglyConnected();

    }

    /**
     * add map introduction information
     * @param part
     */
    void parseMapIntro(String part){
        mapIntro.setMapIntro(part);
    }

    /**
     * add map graph information
     * @param part
     */
    void parseMapGraphInfo(String part){

        mapGraph.setMapGraph(StringUtils.substringAfter(part, "]\r\n"));
    }

    /**
     * read continent string from existing map and split it with new line,
     * for each line, call createContinentFromRaw to create new continent.
     * @param part continent string
     * @return
     */
    Set<Continent> parseRawContinents(String part) {
        String continentInfo = StringUtils.substringAfter(part, "]\r\n");

        Set<Continent> continentSet = Optional.of(StringUtils.split(continentInfo, EOL))
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map(this::createContinentFromRaw)
                .collect(Collectors.toSet());

        mapService.addContinent(continentSet);

        return continentSet;

    }

    /**
     * read continent string from the existing map file and save each valid continent to the mapService
     * if the continent is not valid, will throw an exception
     * @param s continent string
     * @return
     */
    private Continent createContinentFromRaw(String s) {

        try {

            String[] continentInfo = StringUtils.split(s, " ");

            if (isNull(continentInfo) || continentInfo.length != 3) {
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


    /**
     * read country string from existing map and split it with new line,
     * for each line, call createCountryFromRaw to create new country.
     * @param
     * @return
     */
    Set<Country> parseRawCountries(String part) {
        String countryInfo = StringUtils.substringAfter(part, "]\r\n");
        Set<Country> countrySet = Optional.of(StringUtils.split(countryInfo, EOL))
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map(this::createCountryFromRaw)
                .collect(Collectors.toSet());

        mapService.addCountry(countrySet);
        return countrySet;
    }

    /**
     * read country string from the existing map file and save each valid country to the mapService
     * if the country is not valid, will throw an exception
     * @param s
     * @return
     */
    private Country createCountryFromRaw(String s) {
        try {
            String[] countryInfo = StringUtils.split(s, " ");

            if (countryInfo.length != 5) {
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

            String continentName = mapService.findCorrespondingNameByContidentId(continentId).get();
            Country country = new Country(countryId, countryName, continentId);
            country.setCoordinateX(coordinateX);
            country.setCoordinateY(coordinateY);
            country.setContinentName(continentName);

            return country;

        } catch (NumberFormatException e) {
            throw new CountryParsingException(e);
        }
    }

    /**
     * read strings from the existing file, save neighboring countries info in the mapServer
     * @param part string for borders(neighboring countries)
     * @return
     */
    Map<Integer, Set<Integer>> parseRawNeighboringCountries(String part) {

        String borderInfo = StringUtils.substringAfter(part, "]");

        String[] adjacencyInfo = StringUtils.split(borderInfo, EOL);
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


    /**
     * read neighboring countries id
     * @param s a line for neighboring countries
     * @return
     */
    private List<Integer> createAdjacencyCountriesFromRaw(String s) {
        List<String> adjacency = Arrays.asList(StringUtils.split(s, " "));

        throwWhenNoNeighboringCountry(s, adjacency);
        throwWhenNeighbouringCountriesIdInvalid(s, adjacency);

        return adjacency.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    /**
     * throw an exception when neighboring countries id is not valid or is not an integer
     * @param s
     * @param adjacency
     */
    private void throwWhenNeighbouringCountriesIdInvalid(String s, List<String> adjacency) {
        adjacency.stream()
                .map(rawInt -> {
                    if (!NumberUtils.isDigits(rawInt)) {
                        throw new NeighborParsingException("adjacency: " + s + " Element " + rawInt + "is not valid");
                    }
                    return Integer.parseInt(rawInt);
                })
                .filter(this::isCountryIdNotValid)
                .findFirst()
                .ifPresent(invalidId -> {
                    throw new NeighborParsingException("adjacency: " + s + " is not valid for the country id does not exist");
                });
    }

    /**
     * throw an exception if the country been read does not have neighboring country
     * @param s
     * @param adjacency
     */
    private void throwWhenNoNeighboringCountry(String s, List<String> adjacency) {
        if (adjacency.size() <= 1) {
            throw new NeighborParsingException("adjacency: " + s + " is not valid for not having adjacent countries ");
        }
    }

    private boolean isCountryIdNotValid(int id) {
        return !mapService.countryIdExist(id);
    }

    /**
     * delete whitespace and lower cases the string
     * @param name
     * @return
     */
    private String convertFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase(Locale.CANADA);
    }

    public MapService getMapService() {
        return mapService;
    }


    public void setView(CommandPromptView view) {
        this.view = view;
    }
    
    //2 SETTER METHODS ADDED BY KESHAV + showmap full methods
    public void setContinentIdGenerator(int num) {
    	this.continentIdGenerator.set(num);
    }
    
    public void setCountryIdGenerator(int num) {
    	this.countryIdGenerator.set(num);
    }

    /**
     * show map information with continents, each country relates to continents and its neighboring countries
     */
    public void showMapFull() {

    	for(Map.Entry<Integer, Set<Integer>> item :
    						mapService.getContinentCountriesMap().entrySet()) {

    		int key=(int) item.getKey();


    		Optional<Continent> optionalContinent=mapService.getContinentById(key);
    		Continent currentContinent= (Continent) optionalContinent.get();

    		System.out.println("\t\t\t\t\t\t\t\t\tContinent "+currentContinent.getName());
    		System.out.println();

    		Set<Integer> value=item.getValue();

    		for(Integer i:value) {
    			//For Each Country In Continent, Get details + Adjacency Countries
    			Optional<Country> optionalCountry=mapService.getCountryById(i);

    			Country currentCountry=optionalCountry.get();
    			String strCountryOutput="";

    			strCountryOutput+=currentCountry.getCountryName();

    			Set<Integer> adjCountryList= mapService.getAdjacencyCountriesMap().get(i);

    			if(adjCountryList != null) {
                    for (Integer j : adjCountryList) {
                        strCountryOutput += " --> " + mapService.getCountryById(j).get().getCountryName();
                    }
                }

    			System.out.println(strCountryOutput+"\n");
    		}

    	}

    }

}
