package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.ECS.components.EffectComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.VelocityComponent;

//This system should control the PowerUps (HAS NOT BEEN IMPLEMENTED YET)
public class PowerUpSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<EffectComponent> em = ComponentMapper.getFor(EffectComponent.class);

    public PowerUpSystem() {
    }

    //will be called automatically by the engine
    public void addedToEngine(Engine e) {
        entities = e.getEntitiesFor(Family.all(EffectComponent.class).get());
    }

    //will be called by the engine automatically
    public void update(float deltaTime) {
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            EffectComponent effect = em.get(entity);

            //DO SOMETHING HERE
        }
    }
}

