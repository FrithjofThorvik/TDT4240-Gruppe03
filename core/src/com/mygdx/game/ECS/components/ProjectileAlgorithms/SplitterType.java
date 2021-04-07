package com.mygdx.game.ECS.components.ProjectileAlgorithms;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;

import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.ParentComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.SplitterComponent;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.utils.B2DConstants.PPM;

public class SplitterType implements ProjectileType {
    @Override
    public void collision(Entity projectile) {
        // When this projectile collides with something -> destroy it
        projectile.removeAll();
    }

    @Override
    public void midAir(Entity projectile) {
        // Get the number of projectiles this projectile should split into
        int numberOfSplits = projectile.getComponent(SplitterComponent.class).numberOfSplits;

        // Instantiate the new projectiles
        for (int i = 0; i < numberOfSplits; i++) {
//            Entity split = new BouncyProjectile(projectile).createEntity(); // Create the new projectile and as it parent use current projectile
//            Entity parent = projectile.getComponent(ParentComponent.class).parent; // Get this projectile's parent
//            EM.b2dMapper.get(split).body.setLinearVelocity(EM.b2dMapper.get(parent).body.getLinearVelocity()); // Make it so that the newly create projectile spawns at same position as current projectile
//
//            // Get the new projectiles body and give it a variable impulse -> so that the projectile's movements vary
//            Body body = split.getComponent(Box2DComponent.class).body;
//            body.setTransform((EM.positionMapper.get(projectile).position.x + (25* ((float) Math.random() * (1 - (-1)) + (-1)))) / PPM, (EM.positionMapper.get(projectile).position.y + (25*((float) Math.random() * (1 - (-1)) + (-1)))) / PPM, 0);
//            //body.applyLinearImpulse(new Vector2((float)  Math.random() * (1 - (-1)) + (-1), (float) Math.random() * (1 - (-1)) + (-1)), body.getWorldCenter(), false);
        }
    }
}
