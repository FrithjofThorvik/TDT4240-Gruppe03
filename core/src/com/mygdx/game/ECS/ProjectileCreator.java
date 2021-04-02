package com.mygdx.game.ECS;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.ProjectileDamageComponent;
import com.mygdx.game.ECS.components.RenderComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.ECS.components.SpriteComponent;


/**
 * This class is for creating projectile entities
 * Used in ShootingSystem
 **/
public class ProjectileCreator {

    //Using a component mapper is the fastest way to load entities
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);

    // Creates a projectile
    public Entity createProjectile(Entity player, ShootingComponent shootingComponent) {
        Entity projectile = new Entity();

        projectile
                .add(new ProjectileDamageComponent(20, 20, 100))
                .add(new ShootingComponent(shootingComponent.angle, shootingComponent.power))
                .add(new PositionComponent(
                        pm.get(player).position.x,
                        pm.get(player).position.y + sm.get(player).size.y)
                )
                .add(new SpriteComponent(
                        new Texture("cannonball.png"),
                        25f, 25)
                )
                .add(new Box2DComponent(
                        pm.get(projectile).position,
                        sm.get(projectile).size,
                        false, 1f)
                )
                .add(new RenderComponent());

        return projectile;
    }
}
