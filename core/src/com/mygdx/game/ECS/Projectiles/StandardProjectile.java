package com.mygdx.game.ECS.Projectiles;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileComponent;

/**
 * Inherits from Projectile and implements features for the standard projectile type
 **/
public class StandardProjectile extends Projectile {
    public StandardProjectile(Entity parent) {
        super(parent);
    }

    @Override
    void addVariableComponents() {
        projectile.add(new ProjectileComponent(20, 20, this));
    }

    @Override
    public void collision() {
        // When this projectile collides with something -> destroy the projectile
        projectile.removeAll();
    }

    @Override
    public void midAir() {
        // Do nothing
    }
}
