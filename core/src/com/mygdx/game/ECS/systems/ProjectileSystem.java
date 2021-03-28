package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.ECS.components.EffectComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.ProjectileDamageComponent;
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

    // ProjectileSystem constructor
    public ProjectileSystem() {}

    // Will be called automatically by the engine
    public void addedToEngine(Engine e) {
        projectiles = e.getEntitiesFor(Family.all(ProjectileDamageComponent.class, PositionComponent.class, VelocityComponent.class).get());
    }

    // Update function for ProjectileSystem
    public void update(float deltaTime) {
        // Loop through projectile entities
        for (int i = 0; i < projectiles.size(); ++i) {
            // Get projectile
            Entity entity = projectiles.get(i);

            // Get projectile components
            ProjectileDamageComponent damage = em.get(entity);
            PositionComponent pos = pm.get(entity);
            VelocityComponent vel = vm.get(entity);

            //This is just a simple demo code that moves the projectile
            pos.position.x += vel.velocity.x;
            pos.position.y += vel.velocity.y;
            vel.velocity.y -= 9.8f * deltaTime;

            // TODO: Implement projectile damage functionality
        }
    }
}
