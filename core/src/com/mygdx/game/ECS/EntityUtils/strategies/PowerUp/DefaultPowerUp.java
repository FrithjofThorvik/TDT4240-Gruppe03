package com.mygdx.game.ECS.EntityUtils.strategies.PowerUp;

import com.badlogic.ashley.core.Entity;
import static com.mygdx.game.ECS.managers.ECSManager.ECSManager;


public class DefaultPowerUp implements PowerUpEffect{

    @Override
    public void applyEffect(Entity player) {
        ECSManager.shootingMapper.get(player).damageMult *=1.5;
    }
}
