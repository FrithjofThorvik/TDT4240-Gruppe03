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

//This system should control a projectile after it has been created/fired
public class ProjectileSystem extends EntitySystem {
    private ImmutableArray<Entity> projectiles;

    //Using a component mapper is the fastest way to load entities
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private final ComponentMapper<ProjectileDamageComponent> em = ComponentMapper.getFor(ProjectileDamageComponent.class);

    public ProjectileSystem() {
    }

    //will be called automatically by the engine
    public void addedToEngine(Engine e) {
        projectiles = e.getEntitiesFor(Family.all(ProjectileDamageComponent.class, PositionComponent.class, VelocityComponent.class).get());
    }

    //will be called by the engine automatically
    public void update(float deltaTime) {
        for (int i = 0; i < projectiles.size(); ++i) {
            Entity entity = projectiles.get(i);
            ProjectileDamageComponent damage = em.get(entity);
            PositionComponent position = pm.get(entity);
            VelocityComponent vel = vm.get(entity);

            //This is just a simple demo code that moves the projectile
            position.position.x += vel.velocity.x;
            position.position.y += vel.velocity.y;
            vel.velocity.y-=9.8f*deltaTime;

        }
    }
}
