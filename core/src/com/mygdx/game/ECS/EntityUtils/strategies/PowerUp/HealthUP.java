package com.mygdx.game.ECS.EntityUtils.strategies.PowerUp;


import com.badlogic.ashley.core.Entity;
import com.mygdx.game.ECS.managers.ECSManager;


public class HealthUP implements PowerUpEffect {
    @Override
    public void applyEffect(Entity player) {
        ECSManager.getInstance().healthMapper.get(player).hp += 25;
    }
}
