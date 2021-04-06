package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileComponent;

import static com.mygdx.game.managers.EntityManager.EM;


/**
 * This system should control a projectile after it has been created/fired
 * TODO: Implement projectile functionality
 **/
public class ProjectileSystem extends EntitySystem {
    // Prepare arrays for entities
    private ImmutableArray<Entity> projectiles;

    // Will be called automatically by the engine
    public void addedToEngine(Engine e) {
        this.projectiles = e.getEntitiesFor(Family.all(ProjectileComponent.class).get());
    }

    // Update function for ProjectileSystem
    public void update(float deltaTime) {
        if (this.projectiles.size() > 0) {
            // Loop through projectile entities
            for (int i = 0; i < this.projectiles.size(); ++i) {
                // Get projectile
                Entity projectile = this.projectiles.get(i);
                float posX = EM.positionMapper.get(projectile).position.x;
                if (posX > Gdx.graphics.getWidth() || posX < 0) // Remove projectile if it is out of bounds
                    projectile.removeAll();

                // TODO: Maybe delete this system
            }
        }
    }
}
