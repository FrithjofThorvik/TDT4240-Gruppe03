package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * This component should be added and removed from an entity when it collides with another entity
 **/
public class CollisionComponent implements Component {
    public Entity collisionEntity; // The entity colliding with the entity this component attaches to

    public CollisionComponent(Entity collisionEntity) {
        this.collisionEntity = collisionEntity;
    }
}
