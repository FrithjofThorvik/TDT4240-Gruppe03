package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileDamageComponent;
import com.mygdx.game.ECS.components.VelocityComponent;


/**
 * This system should control a projectile after it has been created/fired
 * TODO: Implement projectile functionality
 **/
public class ProjectileSystem extends EntitySystem {
    // Prepare arrays for entities
    private ImmutableArray<Entity> projectiles;

    // Prepare component mappers
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private final ComponentMapper<ProjectileDamageComponent> em = ComponentMapper.getFor(ProjectileDamageComponent.class);

    // Will be called automatically by the engine
    public void addedToEngine(Engine e) {
        this.projectiles = e.getEntitiesFor(Family.all(ProjectileDamageComponent.class, PositionComponent.class, VelocityComponent.class).get());
    }

    // Update function for ProjectileSystem
    public void update(float deltaTime) {
        if (this.projectiles.size() > 0) {
            // Loop through projectile entities
            for (int i = 0; i < this.projectiles.size(); ++i) {
                // Get projectile
                Entity entity = this.projectiles.get(i);

                // Get projectile components
                ProjectileDamageComponent projectileDamage = this.em.get(entity);
                PositionComponent projectilePosition = this.pm.get(entity);
                VelocityComponent projectileVelocity = this.vm.get(entity);

                // TODO: Implement projectile damage functionality
            }
        }
    }
}
