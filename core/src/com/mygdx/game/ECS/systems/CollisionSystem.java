package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.ECS.components.CollisionComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileComponent;
import com.mygdx.game.managers.GameStateManager;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.GameStateManager.GSM;


/**
 * This system should determine behavior of entities colliding -> depending on what it collides with
 **/
public class CollisionSystem extends EntitySystem {
    // Prepare arrays for entities
    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> collidingProjectiles;
    private ImmutableArray<Entity> projectiles;

    // Store entities into arrays
    public void addedToEngine(Engine e) {
        this.projectiles = e.getEntitiesFor(Family.all(ProjectileComponent.class).get());
        this.players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
        this.collidingProjectiles = e.getEntitiesFor(Family.all(CollisionComponent.class, ProjectileComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float dt) {
//        // Loop through all colliding projectile and give it context for what it collides with
//        for (int i = 0; i < this.collidingProjectiles.size(); i++) {
//            Entity projectile = collidingProjectiles.get(i); // Get the entity
//            Entity collisionEntity = EM.collisionMapper.get(projectile).collisionEntity; // Get the colliding entity
//
//            // If projectile collides with player -> update players health
//            if (players.contains(collisionEntity, true)) {
//                updateHealth(projectile, collisionEntity); // Update the colliding player's health
//            }
//
//            // Activate the collision function for the projectile
//           EM.projectileMapper.get(projectile).projectileType.collision(projectile);
//        }
//
//        // Check if there are no projectiles -> move on to SWITCH_ROUND state
//        if (projectiles.size() <= 0) {
//            if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PROJECTILE_AIRBORNE)) {
//                GSM.setGameState(GameStateManager.STATE.SWITCH_ROUND);
//            }
//        }
    }

    // Update the player's health
    void updateHealth(Entity projectile, Entity player) {
        EM.healthMapper.get(player).hp -= EM.projectileMapper.get(projectile).damage;
    }
}
