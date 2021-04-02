package com.mygdx.game.ECS.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.ECS.components.ProjectileComponents.BouncyComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileDamageComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.SplitterComponent;
import com.mygdx.game.ECS.components.SpriteComponent;

public class SplitterProjectile extends ProjectileCreator{
    @Override
    void addVariableComponents() {
        projectile.add(new ProjectileDamageComponent(5, 5))
                .add(new SpriteComponent(
                        new Texture("cannonball.png"),
                        15f, 15f)
                )
                .add(new SplitterComponent(10));
    }
}
