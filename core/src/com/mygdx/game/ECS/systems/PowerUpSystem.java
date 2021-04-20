package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.ECS.components.flags.EffectComponent;

import static com.mygdx.game.managers.EntityManager.EM;


/**
 * This system should control the PowerUps
 * TODO: Has not been implemented yet
 **/
public class PowerUpSystem extends EntitySystem {
    // Prepare entity arrays
    private ImmutableArray<Entity> effects;

    // Store entities into arrays
    public void addedToEngine(Engine e) {
        effects = e.getEntitiesFor(Family.all(EffectComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float deltaTime) {
        if (this.effects.size() > 0) {
            // Loop through all effect entities
            for (int i = 0; i < this.effects.size(); ++i) {
                // Get entity
                Entity entity = this.effects.get(i);

                // Get entity components
                EffectComponent effect = EM.effectMapper.get(entity);
                // TODO: Do something here
            }
        }
    }
}

