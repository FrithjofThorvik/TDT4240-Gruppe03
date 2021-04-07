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
        addCoreComponents(this.entity);
        addClassComponents(this.entity);
        this.addToEngine(this.entity);

        return this.entity;
    }

    // Abstract functions for building out the specified entity
    public abstract void setEntityStats();
    public abstract void addCoreComponents(Entity entity);
    public abstract void addClassComponents(Entity entity);

    // Add the current entity instance to EntityManager's engine
    private void addToEngine(Entity entity) { EM.engine.addEntity(entity); };
}
