package com.mygdx.game.ECS.EntityUtils.strategies.PowerUp;


import com.badlogic.ashley.core.Entity;
import static com.mygdx.game.ECS.managers.ECSManager.ECSManager;


public class HealthUP implements PowerUpEffect {
    @Override
    public void applyEffect(Entity player) {
        ECSManager.healthMapper.get(player).hp += 25;
    }
}
