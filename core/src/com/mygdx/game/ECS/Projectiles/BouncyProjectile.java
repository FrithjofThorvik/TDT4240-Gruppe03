package com.mygdx.game.ECS.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.BouncyComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileDamageComponent;
import com.mygdx.game.ECS.components.SpriteComponent;

public class BouncyProjectile extends ProjectileCreator {
    @Override
    void addVariableComponents() {
        projectile.add(new ProjectileDamageComponent(5, 5))
                .add(new BouncyComponent(4));
        projectile.getComponent(Box2DComponent.class).fixture.setRestitution(0.9f);
        projectile.getComponent(Box2DComponent.class).fixture.setFriction(0.0001f);
    }
}
