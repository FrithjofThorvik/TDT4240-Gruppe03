package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileComponent;
import com.mygdx.game.ECS.components.RenderComponent;
import com.mygdx.game.ECS.components.SpriteComponent;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.utils.B2DConstants.PPM;


/**
 * This system is for processing Box2D physics
 **/
public class PhysicsSystem extends EntitySystem {
    // Arrays for storing entity instances for fonts and sprites
    private ImmutableArray<Entity> box2DEntities;
    private ImmutableArray<Entity> projectiles;

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
        this.projectiles = e.getEntitiesFor(Family.all(ProjectileComponent.class).get());
    }

    // Update function for PhysicsSystem
    public void update(float dt) {
        if (this.box2DEntities.size() > 0) {
            // Loop through all sprite entities, and draw screen
            for (int i = 0; i < this.box2DEntities.size(); ++i) {
                // Fetch each entity
                Entity entity = this.box2DEntities.get(i);

                // Fetch entity component
                PositionComponent entityPosition = EM.positionMapper.get(entity);
                Box2DComponent entityBox2D = EM.b2dMapper.get(entity);

                // Synchronise position component with body position
                entityPosition.position = new Vector2(entityBox2D.body.getPosition().x * PPM, entityBox2D.body.getPosition().y * PPM);

                // Checks if a projectile has reached peak height and activates it's midAir function
                // TODO -> make it so that this only happens once
                if (projectiles.contains(entity, true)) {
                    // Check if the projectile is on it's way down for the first time and call it's midAir function
                    if ((entityBox2D.body.getLinearVelocity().y <= 0) && !EM.projectileMapper.get(entity).midAirReached) {
                        EM.projectileMapper.get(entity).midAirReached = true; // Set the flag that this projectile has reached midAir
                        //EM.projectileMapper.get(entity).projectileType.midAir(entity); // Activate the function
                    }
                }
            }
        }
    }
}
