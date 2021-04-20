package com.mygdx.game.ECS.EntityUtils.strategies.Projectile;

import com.badlogic.ashley.core.Entity;

public class StandardType implements ProjectileType{
    @Override
    public void collision(Entity projectile) {
        // When this projectile collides with something -> destroy it
        projectile.removeAll();
    }

    @Override
    public void midAir(Entity projectile) {
        // Do nothing
    }
}
