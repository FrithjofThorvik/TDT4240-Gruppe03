package com.mygdx.game.ECS;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.ProjectileDamageComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.ECS.components.SpriteComponent;

public class ProjectileCreator {
    // Creates a projectile
    public Entity createProjectile(Entity player, ShootingComponent shootingComponent) {
        Entity projectile = new Entity();

        projectile
                .add(new ProjectileDamageComponent(20, 20, 100))
                .add(new ShootingComponent(shootingComponent.angle, shootingComponent.power))
                .add(new PositionComponent(
                        player.getComponent(PositionComponent.class).position.x,
                        player.getComponent(PositionComponent.class).position.y + player.getComponent(SpriteComponent.class).size.y)
                )
                .add(new SpriteComponent(
                        new Texture("cannonball.png"),
                        25f, 25)
                )
                .add(new Box2DComponent(
                        projectile.getComponent(PositionComponent.class).position,
                        projectile.getComponent(SpriteComponent.class).size,
                        false, 1f)
                )
                .add(new RenderableComponent());

        return projectile;
    }
}
