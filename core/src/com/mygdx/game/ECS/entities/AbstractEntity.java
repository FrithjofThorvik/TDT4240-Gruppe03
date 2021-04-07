package com.mygdx.game.ECS.entities;

import com.badlogic.ashley.core.Entity;

import static com.mygdx.game.managers.EntityManager.EM;

/**
 *  ...
 **/
public abstract class AbstractEntity {
    public Entity entity;

    public Entity createEntity() {
        this.entity = new Entity();

        setEntityStats();
        addCoreComponents();
        addClassComponents();
        this.addToEngine();

        return this.entity;
    }

    // Abstract functions for building out the specified entity
    public abstract void setEntityStats();
    public abstract void addCoreComponents();
    public abstract void addClassComponents();

    // Add the current entity instance to EntityManager's engine
    private void addToEngine() { EM.engine.addEntity(this.entity); };
}
