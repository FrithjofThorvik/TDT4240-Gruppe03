package com.mygdx.game.ECS.components.PowerUpAlgorithm;


import com.badlogic.ashley.core.Entity;

import static com.mygdx.game.managers.EntityManager.EM;

public class HealthUP implements PowerUpEffect {
    @Override
    public void applyEffect(Entity player) {
        EM.healthMapper.get(player).hp += 25;
    }
}
