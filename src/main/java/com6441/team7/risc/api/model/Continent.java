package com6441.team7.risc.api.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Continent {
    private static AtomicInteger atomicInt = new AtomicInteger();
    private final int id;
    private final String name;
    private int continentValue;
    private String color;

    public Continent(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Continent(String name) {
        this.id = atomicInt.incrementAndGet();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Continent setContinentValue(int continentValue) {
        this.continentValue = continentValue;
        return this;
    }

    public int getContinentValue() {
        return continentValue;
    }

    public Continent setColor(String color) {
        this.color = color;
        return this;
    }

    public String getColor() {
        return color;
    }

    public int getId() {
        return id;
    }
}
