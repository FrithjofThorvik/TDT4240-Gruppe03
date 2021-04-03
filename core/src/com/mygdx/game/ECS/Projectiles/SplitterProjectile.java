package com.mygdx.game.ECS.Projectiles;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.SplitterComponent;

/**
 * Inherits from Projectile and implements features for the speedy projectile type
 **/
public class SplitterProjectile extends Projectile {
    public SplitterProjectile(Entity parent) {
        super(parent);
    }

    @Override
    void addVariableComponents() {
        this.projectile.add(new ProjectileComponent(5, 20, this))
                .add(new SplitterComponent(4));
    }

    @Override
    public void collision() {
        // When this projectile collides with something -> count down numberOfBounces
        projectile.removeAll();
    }

    @Override
    public void midAir() {
        // Get the number of projectiles this projectile should split into
        int numberOfSplits = this.projectile.getComponent(SplitterComponent.class).numberOfSplits;

        // Instantiate the new projectiles
        for (int i = 0; i < numberOfSplits; i++) {
            Projectile split = new BouncyProjectile(this.projectile); // Create the new projectile and as it parent use current projectile
            split.setPositionToParent(); // Make it so that the newly create projectile spawns at same position as current projectile

            // Get the new projectiles body and give it a variable impulse -> so that the projectile's movements vary
            Body body = split.projectile.getComponent(Box2DComponent.class).body;
            body.applyLinearImpulse(new Vector2(body.getLinearVelocity().x * i, 0), body.getWorldCenter(), false);
        }
    }
}
