package com.mygdx.game.ECS.components.ProjectileAlgorithms;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.ECS.components.ProjectileComponents.BouncyComponent;


public class BouncyType implements ProjectileType {
    @Override
    public void collision(Entity projectile) {
        // When this projectile collides with something -> count down numberOfBounces
        projectile.getComponent(BouncyComponent.class).numberOfBounces--;

        // When the projectile is done bouncing destroy it
        if (projectile.getComponent(BouncyComponent.class).numberOfBounces <= 0) {
            projectile.removeAll();
        }
    }

    @Override
    public void midAir(Entity projectile) {

    }
}
