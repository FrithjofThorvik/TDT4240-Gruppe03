package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.ECS.components.EffectComponent;

/**
 * This system should control the PowerUps
 * TODO: Has not been implemented yet
 **/
public class PowerUpSystem extends EntitySystem {
    // Prepare entity arrays
    private ImmutableArray<Entity> effects;

    // Prepare component mappers
    private final ComponentMapper<EffectComponent> em = ComponentMapper.getFor(EffectComponent.class);

    // PowerUpSystem constructor
    public PowerUpSystem() {}

    // Store entities into arrays
    public void addedToEngine(Engine e) {
        effects = e.getEntitiesFor(Family.all(EffectComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float deltaTime) {
        // Loop through all effect entities
        for (int i = 0; i < effects.size(); ++i) {
            // Get entity
            Entity entity = effects.get(i);

            // Get entity components
            EffectComponent effect = em.get(entity);
            // TODO: Do something here
        }
    }
}

