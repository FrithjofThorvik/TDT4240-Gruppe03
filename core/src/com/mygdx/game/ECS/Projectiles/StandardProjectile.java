package com.mygdx.game.ECS.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileDamageComponent;
import com.mygdx.game.ECS.components.SpriteComponent;

public class StandardProjectile extends ProjectileCreator {

    @Override
    void addVariableComponents() {
        projectile.add(new ProjectileDamageComponent(20, 20))
                .add(new SpriteComponent(
                        new Texture("cannonball.png"),
                        25f, 25)
                );
    }
}
