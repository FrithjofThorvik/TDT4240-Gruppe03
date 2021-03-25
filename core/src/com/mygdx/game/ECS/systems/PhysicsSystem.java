package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.VelocityComponent;

public class PhysicsSystem extends EntitySystem {
    // Arrays for storing entity instances for fonts and sprites
    private ImmutableArray<Entity> box2DEntities;

    // PhysicsSystem constructor with Application batch instance
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

    // Update function for
    public void update(float deltaTime) {
        // Loop through all sprite entities, and draw screen
        for (int i = 0; i < box2DEntities.size(); ++i) {
            // Fetch each component
            Entity entity = box2DEntities.get(i);
            Box2DComponent bc = entity.getComponent(Box2DComponent.class);
            PositionComponent pc = entity.getComponent(PositionComponent.class);
            VelocityComponent vc = entity.getComponent(VelocityComponent.class);

            bc.body.applyLinearImpulse(vc.velocity, bc.body.getWorldCenter(),false);
            pc.position = new Vector2(bc.body.getPosition().x, bc.body.getPosition().y);
        }
    }
}
