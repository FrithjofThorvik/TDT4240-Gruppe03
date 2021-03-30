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
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;

/**
 * This system is for processing Box2D physics
 * TODO: Improve gravity system
 **/
public class PhysicsSystem extends EntitySystem {
    // Arrays for storing entity instances for fonts and sprites
    private ImmutableArray<Entity> box2DEntities;

    // Prepare component mappers
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);

    // PhysicsSystem constructor
    public PhysicsSystem() {
    }

    // Get entities matching the given components, and store in respective array
    public void addedToEngine(Engine e) {
        // Store box2D entities
        box2DEntities = e.getEntitiesFor(
                Family.all(
                        SpriteComponent.class,
                        Box2DComponent.class,
                        RenderableComponent.class
                ).get()
        );
    }

    // Update function for PhysicsSystem
    public void update(float deltaTime) {
        // Loop through all sprite entities, and draw screen
        for (int i = 0; i < box2DEntities.size(); ++i) {
            // Fetch each entity
            Entity entity = box2DEntities.get(i);

            // Fetch entity component
            PositionComponent pc = pm.get(entity);
            Box2DComponent b2dc = b2dm.get(entity);

            // Synchronise position component with body position
            pc.position = new Vector2(b2dc.body.getPosition().x, b2dc.body.getPosition().y);
        }
    }
}
