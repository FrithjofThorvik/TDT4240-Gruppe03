package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.CollisionComponent;
import com.mygdx.game.ECS.components.HealthComponent;
import com.mygdx.game.ECS.components.ParentComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.BouncyComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileDamageComponent;
import com.mygdx.game.managers.GameStateManager;

import static com.mygdx.game.managers.GameStateManager.GSM;


/**
 * This system should determine behavior of entities colliding -> depending on what collides with what
 **/
public class CollisionSystem extends EntitySystem {
    // Prepare arrays for entities
    private ImmutableArray<Entity> projectiles;
    private ImmutableArray<Entity> collidingPlayers;
    private ImmutableArray<Entity> collidingProjectiles;
    private ImmutableArray<Entity> collidingProjectilesChild;

    // Prepare component mappers
    private final ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);
    private final ComponentMapper<ProjectileDamageComponent> pdm = ComponentMapper.getFor(ProjectileDamageComponent.class);
    private final ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);
    private final ComponentMapper<CollisionComponent> cm = ComponentMapper.getFor(CollisionComponent.class);
    private final ComponentMapper<BouncyComponent> bm = ComponentMapper.getFor(BouncyComponent.class);

    // Store entities into arrays
    public void addedToEngine(Engine e) {
        this.projectiles = e.getEntitiesFor(Family.all(ProjectileDamageComponent.class).get());
        this.collidingPlayers = e.getEntitiesFor(Family.all(CollisionComponent.class, PlayerComponent.class).get());
        this.collidingProjectiles = e.getEntitiesFor(Family.all(CollisionComponent.class, ProjectileDamageComponent.class).get());
        this.collidingProjectilesChild = e.getEntitiesFor(Family.all(CollisionComponent.class, ProjectileDamageComponent.class, ParentComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float dt) {
        // Check if projectile collides with a player and update health accordingly
        checkProjectilePlayerCollision();

        // Loop through and destroy projectiles that collides
        for (int i = 0; i < collidingProjectiles.size(); i++) {
            Entity currentProjectile = collidingProjectiles.get(i);
            System.out.println(bm.has(currentProjectile));
            if (bm.has(currentProjectile)) {
                bm.get(currentProjectile).numberOfBounces -= 1;
                if (bm.get(currentProjectile).numberOfBounces == 0) {
                    currentProjectile.removeAll();
                }
            } else {
                currentProjectile.removeAll();
            }

            // On last projectile, switch GameState
            if (i + 1 == collidingProjectiles.size())
                GSM.setGameState(GameStateManager.STATE.SWITCH_ROUND);

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
