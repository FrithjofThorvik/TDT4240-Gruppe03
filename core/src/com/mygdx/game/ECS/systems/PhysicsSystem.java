package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.RenderComponent;
import com.mygdx.game.ECS.components.SpriteComponent;


/**
 * This system is for processing Box2D physics
 **/
public class PhysicsSystem extends EntitySystem {
    // Arrays for storing entity instances for fonts and sprites
    private ImmutableArray<Entity> box2DEntities;

    // Prepare component mappers
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);

    // Get entities matching the given components, and store in respective array
    public void addedToEngine(Engine e) {
        // Store box2D entities
        this.box2DEntities = e.getEntitiesFor(
                Family.all(
                        SpriteComponent.class,
                        Box2DComponent.class,
                        RenderComponent.class
                ).get()
        );
    }

    // Update function for PhysicsSystem
    public void update(float dt) {
        if (this.box2DEntities.size() > 0) {
            // Loop through all sprite entities, and draw screen
            for (int i = 0; i < this.box2DEntities.size(); ++i) {
                // Fetch each entity
                Entity entity = this.box2DEntities.get(i);

                // Fetch entity component
                PositionComponent entityPosition = this.pm.get(entity);
                Box2DComponent entityBox2D = this.b2dm.get(entity);

                // Synchronise position component with body position
                entityPosition.position = new Vector2(entityBox2D.body.getPosition().x, entityBox2D.body.getPosition().y);
            }
        }
    }
}
