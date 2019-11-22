package com6441.team7.risc.utils.parser;

import com6441.team7.risc.api.RiscConstants;
import com6441.team7.risc.api.exception.ContinentParsingException;
import com6441.team7.risc.api.exception.CountryParsingException;
import com6441.team7.risc.api.exception.MissingInfoException;
import com6441.team7.risc.api.exception.NeighborParsingException;
import com6441.team7.risc.api.model.*;
import com6441.team7.risc.utils.CommonUtils;
import com6441.team7.risc.view.GameView;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com6441.team7.risc.api.RiscConstants.COMMA;
import static com6441.team7.risc.api.RiscConstants.EOL;
import static com6441.team7.risc.api.RiscConstants.NON_EXIST;
import static java.util.Objects.isNull;

/**
 * ConquestParser class for parsing conquest map files. 
 * It implements Adapter pattern with an adapter to both read and write conquest map files.
 *
 */
public class ConquestParser implements IConquestParser {
    private MapGraph mapGraph;
    /**
     * generates id for continents
     */
    private AtomicInteger continentIdGenerator;

    /**
     * generates id for countries
     */
    private AtomicInteger countryIdGenerator;

    /**
     * Constructor for class
     * @param countryIdGenerator integer to keep track of country
     * @param continentIdGenerator integer to keep track of continent
     */
    public ConquestParser(AtomicInteger countryIdGenerator, AtomicInteger continentIdGenerator) {
        this.continentIdGenerator = continentIdGenerator;
        this.countryIdGenerator = countryIdGenerator;
        this.mapGraph = new MapGraph();
    }

    /**
     * shows conquest map file on view
     * @param view GameView where map is displayed
     * @param mapService Map details are pulled from
     */
    @Override
    public void showConquestMap(GameView view, MapService mapService) {
        view.displayMessage(getMapGraphString());
        view.displayMessage(getContinentString(mapService));
        view.displayMessage(getTerritoryString(mapService));
    }

    /**
     * Method to save conquest map file from mapservice and with given file name
     * @param fileName Name of file to be saved in.
     * @param mapService details of map to get from
     * @return returns true if successfully saved conquest map file.
     */
    @Override
    public boolean saveConquestMap(String fileName, MapService mapService) {
        StringBuilder stringBuilder =
                new StringBuilder()
                        .append(getMapGraphString())
                        .append(getContinentString(mapService))
                        .append(getTerritoryString(mapService))
                        .append("\n");

        File file = new File(fileName);
        try {
            FileUtils.writeStringToFile(file, stringBuilder.toString(), StandardCharsets.UTF_8.name());
            return true;
        }catch (IOException e) {
            return false;
        }

    }

    /**
     * get maptGraph in strings
     *
     * @return returns map graph in string format
     */
    private String getMapGraphString() {

        return "[Map]" + "\n" + mapGraph.getMapGraph();
    }


    /**
     * get continents string
     * @param mapService provides map details for use in method
     * @return returns continents in string format
     */
    private String getContinentString(MapService mapService) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[continents]");
        stringBuilder.append("\n");


        mapService.getContinents().forEach(continent -> {
            stringBuilder.append(continent.getName()).append("=");
            stringBuilder.append(continent.getContinentValue()).append(EOL);

        });

        return stringBuilder.toString();
    }

    /**
     * get string of countries
     * @param mapService provides map details for use in method
     * @return arrays of string
     */
    private String getTerritoryString(MapService mapService) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[Territories]");
        stringBuilder.append("\n");


        mapService.getCountries().forEach(country -> {
            stringBuilder.append(country.getCountryName()).append(COMMA);
            stringBuilder.append(country.getCoordinateX()).append(COMMA);
            stringBuilder.append(country.getCoordinateY()).append(COMMA);
            stringBuilder.append(country.getContinentName()).append(COMMA);


            for (Map.Entry<Integer, Set<Integer>> entry : mapService.getAdjacencyCountriesMap().entrySet()) {

                if(entry.getKey().equals(country.getId())){

                    int size = 0;

                    for (Integer integer : entry.getValue()) {
                        stringBuilder.append(mapService.findCorrespondingNameByCountryId(integer).get());
                        size ++;

                        if(size != entry.getValue().size()){
                            stringBuilder.append(COMMA);
                        }
                    }
                    stringBuilder.append("\n");
                }
                
            }
            
        });
        
        return stringBuilder.toString();
    }


    /**
     * Method for trying to read and parse conquest map file
     * @param filename name of conquest map file. Must include its extension.
     * @param view to display the result of what happened when trying to read and parse.
     * @param mapService provides map details for use in method
     * @return returns boolean value true if map can be successfully used in game
     */
    @Override
    public boolean readConquestMapFile(String filename, GameView view, MapService mapService) {
        String rawFileContent = CommonUtils.readFile(filename);

        if(rawFileContent.equalsIgnoreCase(NON_EXIST)){
            createFile(filename, view);
            return true;
        }
        return parseFile(rawFileContent, view, mapService);
    }

    /**
     * if there is no existing map file, new map file with given name is created.
     * @param fileName name of map file to be created 
     * @param view result to be displayed when creating file
     */
    private void createFile(String fileName, GameView view) {
        File file = new File(fileName);
        try {
            FileUtils.writeStringToFile(file, "", StandardCharsets.UTF_8.name());
            view.displayMessage("a file " + file.getName() + " has been created.");
        } catch (IOException e) {
            view.displayMessage(e.getMessage());
        }
    }

   /**
    * read existing map and create continent, country and its neighbors.
    * @param rawFileContent sting that is to be split and divided up
    * @param view result to be displayed when parsing
    * @param mapService provides map details for use in method
    * @return returns true is map is successfully parsed
    */
    boolean parseFile(String rawFileContent, GameView view, MapService mapService) {

        String[] parts = StringUtils.split(rawFileContent.replaceAll("\r",StringUtils.EMPTY), "[");

        try {
            if (parts.length != 3) {
                return false;
            }

            parseMapGraphInfo(parts[0]);
            parseRawContinents(parts[1], mapService);
            parseRawCountries(parts[2], mapService);
            parseRawNeighboringCountries(parts[2], mapService);

        } catch (Exception e) {
            view.displayMessage(e.getMessage());
            return false;
        }

        return mapService.isStronglyConnected();

    }


    /**
     * add map graph information
     *
     * @param part intro part of map file
     */
    void parseMapGraphInfo(String part) {

        mapGraph.setMapGraph(StringUtils.substringAfter(part, "]\n"));
    }


    /**
     * read continent string from existing map and split it with new line,
     * for each line, call createContinentFromRaw to create new continent.
     * @param mapService continent's details are saved in mapService
     * @param part continent names in string format
     * @return returns set of continents that have been successfully parsed
     */
    Set<Continent> parseRawContinents(String part, MapService mapService) {
        String continentInfo = StringUtils.substringAfter(part, "]\n");

        Set<Continent> continentSet = Optional.of(StringUtils.split(continentInfo, "\n"))
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
     * @throws ContinentParsingException invalid continent format
     * @param continentString name of continent in string format
     * @return returns continent object created from string
     */
    private Continent createContinentFromRaw(String continentString) {

        try {

            String[] continentInfo = StringUtils.split(continentString, RiscConstants.ASSIGNMENT);

            if (isNull(continentInfo) || continentInfo.length != 2) {
                throw new ContinentParsingException("continent: " + continentString + " is not valid ");
            }

            String name = convertFormat(continentInfo[0]);
            int continentValue = Integer.parseInt(continentInfo[1]);

            return new Continent(continentIdGenerator.incrementAndGet(), name, continentValue);

        } catch (NumberFormatException e) {
            throw new ContinentParsingException(e);
        }

    }

    /**
     * read country string from existing map and split it with new line,
     * for each line, call createCountryFromRaw to create new country.
     * @param mapService saves countries details to mapService
     * @param part countries names list in string format
     * @return set of countries object that are parsed successfully
     */
    Set<Country> parseRawCountries(String part, MapService mapService) {
        String countryInfo = StringUtils.substringAfter(part, "\n");
        Set<Country> countrySet = Optional.of(StringUtils.split(countryInfo, EOL))
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map(rawCountry -> createCountryFromRaw(rawCountry, mapService))
                .collect(Collectors.toSet());

        mapService.addCountry(countrySet);
        return countrySet;
    }

    /**
     * read country string from the existing map file and save each valid country to the mapService
     * if the country is not valid, will throw an exception
     * @param mapService provides map details for use in method
     * @param countryString countries name list in string format 
     * @throws CountryParsingException when country cannot be parsed.
     * @return returns country thats been parsed successfully
     */
    private Country createCountryFromRaw(String countryString, MapService mapService) {
        try {
            String[] countryInfo = StringUtils.split(countryString, RiscConstants.COMMA);

            if (countryInfo.length < 4) {
                throw new CountryParsingException("territories: " + countryString + " is not valid.");
            }

            int countryId = countryIdGenerator.incrementAndGet();
            String countryName = convertFormat(countryInfo[0]);
            int coordinateX = Integer.parseInt(countryInfo[1]);
            int coordinateY = Integer.parseInt(countryInfo[2]);
            String continentName = convertFormat(countryInfo[3]);


            if (mapService.continentNameNotExist(continentName)) {
                throw new CountryParsingException("territory: " + countryString + " contains invalid continent information");
            }

            int continentId = mapService.findCorrespondingIdByContinentName(continentName).get();

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
     * read strings from the existing file, save neighboring countries info in the mapService
     * @param mapService neighboring countries' map details are saved.
     * @param part string for borders(neighboring countries)
     * @return returns a pair of keys - country and values - its neighbors, where neighbors are a set.
     */
    Map<Integer, Set<Integer>> parseRawNeighboringCountries(String part, MapService mapService) {

        part = StringUtils.substringAfter(part, "]\n");
        String[] adjacencyInfo = StringUtils.split(part, "\n");
        Map<Integer, Set<Integer>> adjacencyMap = new HashMap<>();

        Arrays.stream(adjacencyInfo)
                .map(info -> createAdjacencyCountriesFromRaw(info, mapService))
                .forEach(list -> {
                    int countryId = list.get(0);
                    Set<Integer> adjacencyId = new HashSet<>(list.subList(1, list.size()));
                    adjacencyMap.put(countryId, adjacencyId);
                });

        mapService.addNeighboringCountries(adjacencyMap);

        return adjacencyMap;
    }


    /**
     * read neighboring countries id from file and returns them as List of integers
     * @param mapService provides map details for use in method
     * @param s a line for neighboring countries
     * @return List of integers of neighboring countries
     */
    private List<Integer> createAdjacencyCountriesFromRaw(String s, MapService mapService) {

        List<String> adjacency = Arrays.asList(StringUtils.split(s, COMMA));
        List<Integer> list = new ArrayList<>();

        throwWhenNoNeighboringCountry(s, adjacency);
        throwWhenNeighbouringCountriesIdInvalid(s, adjacency.subList(4, adjacency.size()), mapService);


        list.add(mapService.findCorrespondingIdByCountryName(adjacency.get(0)).get());

        for (String country : adjacency.subList(4, adjacency.size())) {
            list.add(mapService.findCorrespondingIdByCountryName(country).get());

        }
        return list;

    }

    /**
     * check condition for valid country ID and throws NeighborParsingException when invalid
     * @param s name of country whose ID need to be validated
     * @param adjacency list of countries names in string format
     * @param mapService provides map details for use in method
     * @throws NeighborParsingException throws this exception when country has invalid country ID
     */
    private void throwWhenNeighbouringCountriesIdInvalid(String s, List<String> adjacency, MapService mapService) {
        for (String country : adjacency) {
            if (!mapService.findCorrespondingIdByCountryName(country).isPresent()) {
                throw new NeighborParsingException(s + " is not valid");
            }
        }
    }

    /**
     * Check country entry in conquest map file and see if there are neighbor countries
     * @param s Name of country whose neighbors need to be validated
     * @param adjacency entry of country in map file
     * @throws NeighborParsingException When country has no neighbors
     */
    private void throwWhenNoNeighboringCountry(String s, List<String> adjacency) {
        if (adjacency.size() < 4) {
            throw new NeighborParsingException(s + " is not valid");
        }
    }


    /**
     * delete whitespace and lower cases the string
     *
     * @param name string which need to be formatted
     * @return returns coverted string
     */
    private String convertFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase(Locale.CANADA);
    }


}