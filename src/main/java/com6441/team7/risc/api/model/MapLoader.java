package com6441.team7.risc.api.model;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class MapLoader {
    private static Logger logger = LogManager.getLogger(MapLoader.class);

    public Optional<MapService> loadMap(Scanner scanner) {
        try {

            System.out.println("Welcome to the game! Please choose one of the map:");
            System.out.println("test.map");
            System.out.println("enter the map you would like to open:");
            String path = scanner.nextLine();
            return getMapServiceFromMapFile(path);

        } catch (IOException e) {
            return createOwnMap(scanner);
        } catch (NumberFormatException | OBJECT_NOT_EXIST e) {
            logger.error(e.getMessage());
            System.exit(126);
        }
        return Optional.empty();
    }

    private Optional<MapService> getMapServiceFromMapFile(String path) throws IOException {
        String data = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
        return buildGameMap(data);
    }


    private Optional<MapService> createOwnMap(Scanner scanner) {

        System.out.println("The map does not exist, Would you like to create a map?(y/n)");


        String reply = scanner.nextLine();
        if (!"y".equalsIgnoreCase(reply)) {
            return Optional.empty();
        }


        System.out.println("enter the name of the new created map");
        String fileName = scanner.nextLine();

        System.out.println("Please enter the contents you would like to write? To exit, On UNIX and Mac OS, press CTRL+D, and on windows, press CTRL+Z");
        StringBuilder data = new StringBuilder();

        while (scanner.hasNextLine()) {
            data.append(scanner.nextLine());
            data.append("\n");
        }
        File file = new File(fileName);
        try {
            FileUtils.writeStringToFile(file, data.toString(), StandardCharsets.UTF_8.name());
            return getMapServiceFromMapFile(fileName);
        } catch (IOException e) {
            logger.error("Cannot read file {}", fileName, e);
            System.exit(126);
        }


        return Optional.empty();
    }


    private Optional<MapService> buildGameMap(String data) {
        try{
            String[] result = StringUtils.split(data, "[");

            createMapIntro(result[0]);
            Set<Continent> continents = createContinents(result[1]);
            Set<Country> countries = createCountries(continents, result[2]);

            MapService mapService = new MapService();
            mapService.setContinents(continents);
            mapService.setCountries(countries);
            countries.forEach(country -> mapService.addEdge(country.getName(), country.getAdjacentCountryName()));


            if(validateGameMap(mapService)){
                return Optional.of(mapService);
            }
            else{
                System.out.println("invalid map");
                return Optional.empty();
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("invalid map");
            return Optional.empty();
        }


    }


    private MapIntro createMapIntro(String s) {
        String[] data = split(s, "\n");

        List<String> info = Arrays.stream(data)
                .map(text -> StringUtils.split(text, "=")[1])
                .collect(Collectors.toList());

        MapIntro mapIntro = new MapIntro();
        mapIntro.setImgName(info.get(0))
                .setIsWrap(info.get(1))
                .setScroll(info.get(2))
                .setAuthor(info.get(3))
                .setWarn(info.get(4));

        //      logger.info(info.toString());
        return mapIntro;
    }

    private Continent createContinent(String data) {
        String[] str = StringUtils.split(data, "=");
        return new Continent(str[0], Integer.parseInt(str[1]));
    }

    private Set<Continent> createContinents(String s) {
        Set<Continent> continents = Optional.ofNullable(split(s, "\n"))
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map(this::createContinent)
                .collect(toSet());
        //     logger.info(continents.toString());
        return continents;
    }

    private String[] split(String s, String seperator) {
        s = StringUtils.substringAfter(s, "]");
        return StringUtils.split(s, seperator);
    }

    private boolean validateGameMap(MapService mapService) {
        boolean result = mapService.isStrongConnected();

        if(result){
            System.out.println("The map is strongly connected");
        } else{
            System.out.println("The map is not strongly connected");
        }
        return result;
    }

    private Set<Country> createCountries(Set<Continent> continentSet, String data) {
        return Optional.ofNullable(split(data, "\n"))
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map(this::createCountryFromString)
                .map(country -> validateCountryContinentData(country, continentSet))
                .collect(toSet());
    }

    private Country createCountryFromString(String rawCountry) {
        List<String> countryDatum = Arrays.asList(StringUtils.split(rawCountry, ","));
        Country country = new Country();
        country.setName(countryDatum.get(0))
                .setCordinateX(Integer.parseInt(countryDatum.get(1)))
                .setCordinateY(Integer.parseInt(countryDatum.get(2)))
                .setContinent(countryDatum.get(3));
        if (countryDatum.size() > 4) {
            country.setAdjacentCountryName(countryDatum.subList(4, countryDatum.size()));
        }


        return country;
    }

    private Country validateCountryContinentData(Country country, Set<Continent> continentSet) {
        String continentName = country.getContinent().getName().toLowerCase();
        boolean continentNameValid = continentSet
                .stream()
                .map(Continent::getName)
                .map(String::toLowerCase)
                .collect(toSet())
                .contains(continentName);
        if (continentNameValid) {
            return country;
        }
        throw new OBJECT_NOT_EXIST("The Continent does not exist");
    }
}
