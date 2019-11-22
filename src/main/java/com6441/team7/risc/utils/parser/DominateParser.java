package com6441.team7.risc.utils.parser;

import com6441.team7.risc.api.RiscConstants;
import com6441.team7.risc.api.exception.*;
import com6441.team7.risc.api.model.*;
import com6441.team7.risc.utils.CommonUtils;
import com6441.team7.risc.view.GameView;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com6441.team7.risc.api.RiscConstants.EOL;
import static com6441.team7.risc.api.RiscConstants.NON_EXIST;
import static com6441.team7.risc.api.RiscConstants.WHITESPACE;
import static java.util.Objects.compare;
import static java.util.Objects.isNull;

/**
 * This class parses Domination map files. This is built on adapter pattern to work with
 * conquest and domination map files.
 * 
 *
 */
public class DominateParser implements IDominationParser {
    /**
     * a reference of mapGraph
     */
    private MapGraph mapGraph;

    /**
     * a reference of mapIntro
     */
    private MapIntro mapIntro;

    /**
     * generates id for continents
     */
    private AtomicInteger continentIdGenerator;

    /**
     * generates id for countries
     */
    private AtomicInteger countryIdGenerator;


    public DominateParser(AtomicInteger countryIdGenerator, AtomicInteger continentIdGenerator){
        this.continentIdGenerator = continentIdGenerator;
        this.countryIdGenerator = countryIdGenerator;
        this.mapGraph = new MapGraph();
        this.mapIntro = new MapIntro();
    }

    @Override
    public boolean saveDominateMap(String fileName, MapService mapService) {
        String mapIntro = getMapIntroString();
        if(mapIntro.length() == 0){
            mapIntro = fileName;
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
                        .append(getContinentString(mapService))
                        .append(EOL)
                        .append("[countries]")
                        .append(EOL)
                        .append(getCountryString(mapService))
                        .append(EOL)
                        .append("[borders]")
                        .append(EOL)
                        .append(getBorderString(mapService));

        File file = new File(fileName);
        try {
            FileUtils.writeStringToFile(file, stringBuilder.toString(), StandardCharsets.UTF_8.name());
            return true;
        }catch (IOException e) {
            return false;
        }

    }

    /**
     * get continents string
     * @return
     * @param mapService
     */
    private String getContinentString(MapService mapService) {
        StringBuilder stringBuilder = new StringBuilder();
        mapService.getContinents().forEach(continent -> {
            stringBuilder.append(continent.getName()).append(" ");
            stringBuilder.append(continent.getContinentValue()).append(" ");
            stringBuilder.append(continent.getColor()).append(EOL);

        });


        return stringBuilder.toString();
    }

    /**
     * get string of countries
     * @return arrays of string
     * @param mapService
     */
    private String getCountryString(MapService mapService) {
        StringBuilder stringBuilder = new StringBuilder();
        mapService.getCountries().forEach(country -> {
            stringBuilder.append(country.toString());
        });

        return stringBuilder.toString();
//        return mapService.getCountries()
//                .stream()
//                .map(Country::toString)
//                .reduce(String::concat)
//                .orElseThrow(RuntimeException::new);
    }

    /**
     * get border
     * @return
     * @param mapService
     */
    private String getBorderString(MapService mapService) {
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


    @Override
    public void showDominateMap(MapService mapService) {
        mapService.printCountryInfo();
        mapService.printContinentInfo();
        mapService.printNeighboringCountryInfo();
    }

    @Override
    public boolean readDominateMapFile(String fileName, GameView view, MapService mapService) {
        String file = CommonUtils.readFile(fileName);

        if(file.equalsIgnoreCase(NON_EXIST)){
            createFile(fileName, view);
            return true;
        }

        return parseFile(file, view, mapService);
    }


    /**
     * create a new map if the map file name does not exist
     * @param name
     */
    private void createFile(String name, GameView view) {

        File file = new File(name);
        try {
            FileUtils.writeStringToFile(file, "", StandardCharsets.UTF_8.name());
            view.displayMessage("a file " + file.getName() + " has been created.");
        } catch (IOException e) {
            view.displayMessage(e.getMessage());
        }
    }

    /**
     * read existing map and create continent, country and its neighbors.
     * @param s exsting map as a string
     * @param view
     * @param mapService
     * @return
     */
    boolean parseFile(String s, GameView view, MapService mapService) {
        String[] parts = StringUtils.split(s, "[");

        try {
            if (parts.length != 5) {
                return false;
            }

            parseMapIntro(parts[0]);
            parseMapGraphInfo(parts[1], mapService);
            parseRawContinents(parts[2], mapService);
            parseRawCountries(parts[3], mapService);
            parseRawNeighboringCountries(parts[4], mapService);

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
     * @param mapService
     */
    void parseMapGraphInfo(String part, MapService mapService){

        mapGraph.setMapGraph(StringUtils.substringAfter(part, "]\r\n"));
    }

    /**
     * read continent string from existing map and split it with new line,
     * for each line, call createContinentFromRaw to create new continent.
     * @param part continent string
     * @param mapService
     * @return
     */
    Set<Continent> parseRawContinents(String part, MapService mapService) {
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

            String[] continentInfo = StringUtils.split(s, RiscConstants.WHITESPACE);

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
     * @param mapService
     * @return
     */
    Set<Country> parseRawCountries(String part, MapService mapService) {
        String countryInfo = StringUtils.substringAfter(part, "]\r\n");
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
     * @param s
     * @param mapService
     * @return
     */
    private Country createCountryFromRaw(String s, MapService mapService) {
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
     * @param mapService
     * @return
     */
    Map<Integer, Set<Integer>> parseRawNeighboringCountries(String part, MapService mapService) {

        String borderInfo = StringUtils.substringAfter(part, "]");

        String[] adjacencyInfo = StringUtils.split(borderInfo, EOL);
        Map<Integer, Set<Integer>> adjacencyMap = new HashMap<>();

        Arrays.stream(adjacencyInfo)
                .map(raw -> createAdjacencyCountriesFromRaw(raw, mapService))
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
     * @param mapService
     * @return
     */
    private List<Integer> createAdjacencyCountriesFromRaw(String s, MapService mapService) {
        List<String> adjacency = Arrays.asList(StringUtils.split(s, WHITESPACE));

        throwWhenNoNeighboringCountry(s, adjacency);
        throwWhenNeighbouringCountriesIdInvalid(s, adjacency, mapService);

        return adjacency.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    /**
     * throw an exception when neighboring countries id is not valid or is not an integer
     * @param s
     * @param adjacency
     * @param mapService
     */
    private void throwWhenNeighbouringCountriesIdInvalid(String s, List<String> adjacency, MapService mapService) {
        adjacency.stream()
                .map(rawInt -> {
                    if (!NumberUtils.isDigits(rawInt)) {
                        throw new NeighborParsingException("adjacency: " + s + " Element " + rawInt + "is not valid");
                    }
                    return Integer.parseInt(rawInt);
                })
                .filter(countryId -> isCountryIdNotValid(countryId, mapService))
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

    /**
     * check country id is not valid
     * @param id id to be checked
     * @param mapService
     * @return true if country id is not valid
     */
    private boolean isCountryIdNotValid(int id, MapService mapService) {
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

    /**
     * set continent id generator
     * @param num number to set
     */
    public void setContinentIdGenerator(int num) {
        this.continentIdGenerator.set(num);
    }

    /**
     * set country id generator
     * @param num number to set
     */
    public void setCountryIdGenerator(int num) {
        this.countryIdGenerator.set(num);
    }

    /**
     * get mapIntro in string
     * @return
     */
    private String getMapIntroString(){
        return mapIntro.getMapIntro();
    }

    /**
     * get maptGraph in strings
     * @return
     */
    private String getMapGraphString(){
        return mapGraph.getMapGraph();
    }
}
