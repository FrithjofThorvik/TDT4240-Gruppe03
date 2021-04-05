package com.mygdx.game.ECS.Projectiles;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.BouncyComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileComponent;

/**
 * Inherits from Projectile and implements features for the bouncy projectile type
 **/
public class BouncyProjectile extends Projectile {
    public BouncyProjectile(Entity parent) {
        super(parent);
    }

    @Override
    void addVariableComponents() {
        projectile.add(new ProjectileComponent(5, 5, this))
                .add(new BouncyComponent(4));

        // Give the box2d component bounciness
        projectile.getComponent(Box2DComponent.class).fixture.setRestitution(0.9f);
        projectile.getComponent(Box2DComponent.class).fixture.setFriction(0.01f);
    }

    @Override
    public void collision() {
        // When this projectile collides with something -> count down numberOfBounces
        projectile.getComponent(BouncyComponent.class).numberOfBounces--;

        // When the projectile is done bouncing destroy it
        if (projectile.getComponent(BouncyComponent.class).numberOfBounces <= 0) {
            projectile.removeAll();
        }
    }

    @Override
    public void midAir() {
        // Do nothing
    }
}
