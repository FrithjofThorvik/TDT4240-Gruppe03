package com.mygdx.game.ECS.components.PowerUpAlgorithm;

import com.badlogic.ashley.core.Entity;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.GameStateManager.GSM;

public class DefaultPowerUp implements PowerUpEffect{

    @Override
    public void applyEffect(Entity player) {
        EM.shootingMapper.get(player).damageMult *=1.5;
    }
}
