package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.ECS.components.EffectComponent;
import com.mygdx.game.ECS.components.ProjectileDamageComponent;

public class DamageSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<ProjectileDamageComponent> em = ComponentMapper.getFor(ProjectileDamageComponent.class);

    public DamageSystem() {
    }

    public void addedToEngine(Engine e) {//will be called automatically by the engine
        entities = e.getEntitiesFor(Family.all(ProjectileDamageComponent.class).get());
    }

    public void update(float deltaTime) {//will be called by the engine automatically
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            ProjectileDamageComponent damage = em.get(entity);

            //DO SOMETHING HERE
        }
    }
}
