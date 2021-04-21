package com.mygdx.game.ECS.EntityUtils.strategies.PowerUp;


import com.badlogic.ashley.core.Entity;

public interface PowerUpEffect {
    void applyEffect(Entity player);
}
