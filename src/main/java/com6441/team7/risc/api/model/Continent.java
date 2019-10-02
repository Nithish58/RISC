package com6441.team7.risc.api.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Continent {
    private final int id;
    private final String name;
    private int continentValue;
    private String color;

    public Continent(int id, String name) {
        this.id = id;
        this.name = name.toLowerCase();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Continent continent = (Continent) o;
        return id == continent.id &&
                Objects.equals(name, continent.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
