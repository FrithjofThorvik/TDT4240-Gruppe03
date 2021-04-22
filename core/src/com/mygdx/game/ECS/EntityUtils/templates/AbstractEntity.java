package com.mygdx.game.ECS.EntityUtils.templates;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.ECS.managers.ECSManager;


/**
 *  The template method for creating entities
 *  The functions are overwritten in child objects of this class
 **/
public abstract class AbstractEntity {
    public Entity entity;

    public Entity createEntity() {
        this.entity = new Entity(); // Create a new, blank entity

        // Template functions
        setEntityStats(); // Set variables specific for the entity
        addCoreComponents(); // Add core components that are common among a group of entities (eg. projectiles)
        addClassComponents(); // Add class component that varies within a common group of entities (eg. SplitterProjectile)

        this.addToEngine(); // all entities must be added to engine

        return this.entity;
    }

    // Abstract functions for building out the specified entity
    public abstract void setEntityStats();
    public abstract void addCoreComponents();
    public abstract void addClassComponents();

    // Add the current entity instance to EntityManager's engine
    private void addToEngine() {  ECSManager.getInstance().getEngine().addEntity(this.entity); }
}
