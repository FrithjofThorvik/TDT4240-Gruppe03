package com.mygdx.game.ECS.EntityUtils.strategies.PowerUp;

import com.badlogic.ashley.core.Entity;
;import static com.mygdx.game.ECS.managers.ECSManager.ECSManager;

public class SpeedUp implements PowerUpEffect{

    @Override
    public void applyEffect(Entity player) {
        ECSManager.velocityMapper.get(player).velocity.x *= 1.25f;
    }
}
