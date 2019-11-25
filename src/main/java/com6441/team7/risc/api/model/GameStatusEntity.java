package com6441.team7.risc.api.model;

import java.util.List;

/**
 * This class is used to used to store the gameStatusEntity
 */
public class GameStatusEntity {

    /**
     * getter
     * return the list of entities to load in the game
     * @return list
     */
    public List<Object> getEntities() {
        return entities;
    }

    /**
     * setter
     * set the entity
     * @param entities the list of entities
     */
    public void setEntities(List<Object> entities) {
        this.entities = entities;
    }

    /**
     * a list of entities
     */
    private List<Object> entities;
}
