package com.mygdx.game.ECS.components.PowerUpAlgorithm;

import com.badlogic.ashley.core.Entity;

import static com.mygdx.game.managers.EntityManager.EM;

public class SpeedUp implements PowerUpEffect{

    @Override
    public void applyEffect(Entity player) {
        EM.velocityMapper.get(player).velocity.x *= 1.25f;
    }
}
