package com.mygdx.game.ECS.EntityUtils.strategies.Projectile;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import com.mygdx.game.ECS.components.misc.Box2DComponent;
import com.mygdx.game.ECS.components.projectiles.SplitterComponent;
import com.mygdx.game.ECS.EntityUtils.EntityTemplateMapper;
import com.mygdx.game.ECS.managers.ECSManager;

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
            Entity split =  ECSManager.getInstance().getEntityTemplateMapper().getProjectileClass(EntityTemplateMapper.PROJECTILES.BOUNCER).createEntity(); // Create the new projectile and as it parent use current projectile
            ECSManager.getInstance().b2dMapper.get(split).body.setLinearVelocity( ECSManager.getInstance().b2dMapper.get(projectile).body.getLinearVelocity()); // Make it so that the newly create projectile spawns at same position as current projectile

            // Get the new projectiles body and give it a variable impulse -> so that the projectile's movements vary
            Body body = split.getComponent(Box2DComponent.class).body;
            body.setTransform(( ECSManager.getInstance().positionMapper.get(projectile).position.x + (25* ((float) Math.random() * (1 - (-1)) + (-1)))) / PPM, ( ECSManager.getInstance().positionMapper.get(projectile).position.y + (25*((float) Math.random() * (1 - (-1)) + (-1)))) / PPM, 0);
            body.applyLinearImpulse(new Vector2((float)  Math.random() * (0.5f - (-0.5f )) + (-0.5f ), (float) Math.random() * (0.5f  - (-0.5f )) + (-0.5f )), body.getWorldCenter(), false);
        }
    }
}
