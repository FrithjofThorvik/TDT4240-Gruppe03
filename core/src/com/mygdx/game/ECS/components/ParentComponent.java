package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Add this to entities that are dependent on other entities
 * For example the health font will always display the health of a specific entity, so it should have that entity as a parent
 **/
public class ParentComponent implements Component {
    public Entity parent; // The parent entity

    public ParentComponent(Entity entity) {
        this.parent = entity;
    }
}
