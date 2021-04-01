package com.mygdx.game.ECS.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileDamageComponent;
import com.mygdx.game.ECS.components.SpriteComponent;

public class SpeedyProjectile extends ProjectileCreator {
    @Override
    void addVariableComponents() {
        projectile.add(new ProjectileDamageComponent(5, 5, 100))
                .add(new SpriteComponent(
                        new Texture("cannonball.png"),
                        15f, 15f)
                );
    }
}
