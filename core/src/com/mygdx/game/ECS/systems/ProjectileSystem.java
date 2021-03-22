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

public class ProjectileSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    //Using a component mapper is the fastest way to load entities
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<ProjectileDamageComponent> em = ComponentMapper.getFor(ProjectileDamageComponent.class);

    public ProjectileSystem() {
    }

    public void addedToEngine(Engine e) {//will be called automatically by the engine
        entities = e.getEntitiesFor(Family.all(ProjectileDamageComponent.class,PositionComponent.class, VelocityComponent.class).get());
    }

    public void update(float deltaTime) {//will be called by the engine automatically
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            ProjectileDamageComponent damage = em.get(entity);
            PositionComponent position=pm.get(entity);
            VelocityComponent vel=vm.get(entity);

            //DO SOMETHING HERE
            //This is just a simple demo code
            position.position.x+=vel.velocity.x;
            position.position.y+=vel.velocity.y;

        }
    }
}
