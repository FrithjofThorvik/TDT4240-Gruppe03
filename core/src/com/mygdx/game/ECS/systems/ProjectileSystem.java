package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;

import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.CollisionComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.ProjectileAlgorithms.ProjectileType;

import com.mygdx.game.Application;

import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileComponent;
import com.mygdx.game.managers.GameStateManager;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.GameStateManager.GSM;


/**
 * This system should control a projectile after it has been created/fired
 * TODO: Implement projectile functionality
 **/
public class ProjectileSystem extends EntitySystem {
    // Prepare arrays for entities
    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> collidingProjectiles;
    private ImmutableArray<Entity> projectiles;

    // Will be called automatically by the engine
    public void addedToEngine(Engine e) {
        this.projectiles = e.getEntitiesFor(Family.all(ProjectileComponent.class).get());
        this.players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
        this.collidingProjectiles = e.getEntitiesFor(Family.all(CollisionComponent.class, ProjectileComponent.class).get());
        this.projectiles = e.getEntitiesFor(Family.all(ProjectileComponent.class).get());
    }

    // Update function for ProjectileSystem
    public void update(float deltaTime) {
        if (this.projectiles.size() > 0) {
            // Loop through projectile entities
            for (int i = 0; i < this.projectiles.size(); ++i) {
                // Get projectile
                Entity projectile = this.projectiles.get(i);

                if (EM.projectileMapper.has(projectile)) {
                    checkMidAir(projectile);
                    checkOutOfBounds(projectile);
                    checkCollision(projectile);
                }

            }
        }
    }

    public void checkOutOfBounds(Entity projectile) {
        float posX = EM.positionMapper.get(projectile).position.x;
        if (posX > Application.camera.viewportWidth || posX < 0) // Remove projectile if it is out of bounds
            projectile.removeAll();
    }

    public void checkCollision(Entity projectile) {
        if (EM.collisionMapper.has(projectile)) {
            Entity collisionEntity = EM.collisionMapper.get(projectile).collisionEntity; // Get the colliding entity

            // If projectile collides with player -> update players health
            if (players.contains(collisionEntity, true)) {
                EM.healthMapper.get(collisionEntity).hp -= EM.projectileMapper.get(projectile).damage;
            }

            // If projectile collides with something other than projectile -> call the projectile collision function
            if (!projectiles.contains(collisionEntity, true)) {
                // Activate the collision function for the projectile
                EM.projectileMapper.get(projectile).projectileType.collision(projectile);
            }

        }
    }

    public void checkMidAir(Entity projectile) {
        Box2DComponent entityBox2D = EM.b2dMapper.get(projectile);
        // Checks if a projectile has reached peak height and activates it's midAir function
        // TODO -> make it so that this only happens once
        // Check if the projectile is on it's way down for the first time and call it's midAir function
        if ((entityBox2D.body.getLinearVelocity().y <= 0 && !EM.projectileMapper.get(projectile).midAirReached)) {
            EM.projectileMapper.get(projectile).midAirReached = true; // Set the flag that this projectile has reached midAir
            EM.projectileMapper.get(projectile).projectileType.midAir(projectile); // Activate the function
        }
    }
}
