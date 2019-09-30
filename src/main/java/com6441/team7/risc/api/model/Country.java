package com6441.team7.risc.api.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Country {

    private static AtomicInteger atomicInt = new AtomicInteger();
    private final Integer id;
    private final String name;
    private Integer continentIdentifier;
    private String continentName;
    private Integer coordinateX;
    private Integer coordinateY;
    private Player player;
    private Integer soldiers;


    public Country(String countryName){
        id = atomicInt.incrementAndGet();
        this.name = countryName;
    }

    public Country(Integer id, String countryName) {
        this.id = id;
        this.name = countryName;
    }


    public Integer getId() {
        return id;
    }

    public String getCountryName() {
        return name;
    }


    public Country setContinentIdentifier(Integer continentIdentifier) {
        this.continentIdentifier = continentIdentifier;
        return this;
    }

    public Integer getContinentIdentifier() {
        return continentIdentifier;
    }


    public Country setContinentName(String continentName){
        this.continentName = continentName;
        return this;
    }

    public String getContinentName() {
        return continentName;
    }

    public Integer getCoordinateX() {
        return coordinateX;
    }

    public Country setCoordinateX(Integer coordinateX) {
        this.coordinateX = coordinateX;
        return this;
    }

    public Integer getCoordinateY() {
        return coordinateY;
    }

    public Country setCoordinateY(Integer coordinateY) {
        this.coordinateY = coordinateY;
        return this;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getSoldiers() {
        return soldiers;
    }

    public void setSoldiers(Integer soldiers) {
        this.soldiers = soldiers;
    }

    public void addSoldiers(int number){
        this.soldiers += number;
    }

    public void removeSoldiers(int number){
        this.soldiers -= number;
    }
}
