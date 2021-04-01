package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.CollisionComponent;
import com.mygdx.game.ECS.components.HealthComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.ProjectileDamageComponent;

import java.util.HashMap;

/**
 * This system should determine behavior of entities colliding -> depending on what collides with what
 **/
public class CollisionSystem extends EntitySystem {
    // Prepare arrays for entities
    private ImmutableArray<Entity> projectiles;
    private ImmutableArray<Entity> collidingPlayers;
    private ImmutableArray<Entity> collidingProjectiles;

    // Prepare component mappers
    private final ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);
    private final ComponentMapper<ProjectileDamageComponent> pdm = ComponentMapper.getFor(ProjectileDamageComponent.class);
    private final ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);
    private final ComponentMapper<CollisionComponent> cm = ComponentMapper.getFor(CollisionComponent.class);

    // Store entities into arrays
    public void addedToEngine(Engine e) {
        this.projectiles = e.getEntitiesFor(Family.all(ProjectileDamageComponent.class).get());
        this.collidingPlayers = e.getEntitiesFor(Family.all(CollisionComponent.class, PlayerComponent.class).get());
        this.collidingProjectiles = e.getEntitiesFor(Family.all(CollisionComponent.class, ProjectileDamageComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float dt) {

        // Check if projectile collides with a player and update health accordingly
        checkProjectilePlayerCollision();

        // Loop through and destroy projectiles that collides
        for (int i = 0; i < collidingProjectiles.size(); i++) {
            Entity projectile = collidingProjectiles.get(i);
            Body body = b2dm.get(projectile).body;
            body.getWorld().destroyBody(body);
            projectile.removeAll();
            // TODO: The projectile might want to do something when it collides, like exploding/splitting into pieces
        }

    }

    public void checkProjectilePlayerCollision() {
        // Loop through all colliding players
        for (int i = 0; i < this.collidingPlayers.size(); i++) {
            Entity player = collidingPlayers.get(i); // Get the entity
            Entity collisionEntity = cm.get(player).collisionEntity; // Get the colliding entity

            // If player collides with projectile -> update health and destroy projectile
            if (projectiles.contains(collisionEntity, true)) {
                hm.get(player).hp -= pdm.get(collisionEntity).damage;
            }
        }
    }
}
