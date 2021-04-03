package com.mygdx.game.ECS.Projectiles;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileComponent;

/**
 * Inherits from Projectile and implements features for the speedy projectile type
 **/
public class SpeedyProjectile extends Projectile {
    public SpeedyProjectile(Entity parent) {
        super(parent);
    }

    @Override
    void addVariableComponents() {
        projectile.add(new ProjectileComponent(5, 500, this));
    }

    @Override
    public void collision() {
        // Destroy the projectile when it collides with something
        projectile.removeAll();
    }

    @Override
    public void midAir() {
        // Do nothing
    }
}
