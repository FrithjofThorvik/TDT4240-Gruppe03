package com.mygdx.game.ECS.Projectiles;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileDamageComponent;
import com.mygdx.game.ECS.components.RenderComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.ECS.components.SpriteComponent;

/**
 * This abstract class should add the basic components that are equal for each projectile
 * The abstract function addExtraComponents will be defined in in other classes and should add the components that will vary for each projectile
 **/
public abstract class ProjectileCreator {
    Entity projectile = new Entity(); // This projectile entity is modified through this class and then returned

    ProjectileCreator() {
    }

    // Adds basic universal components to the projectile
    public Entity createProjectile(Entity player, ShootingComponent shootingComponent) {
        {
            projectile
                    .add(new SpriteComponent(
                            new Texture("cannonball.png"),
                            15f, 15f)
                    )
                    .add(new ShootingComponent(shootingComponent.angle, shootingComponent.power))
                    .add(new PositionComponent(
                            player.getComponent(PositionComponent.class).position.x,
                            player.getComponent(PositionComponent.class).position.y + player.getComponent(SpriteComponent.class).size.y)
                    )
                    .add(new Box2DComponent(
                            projectile.getComponent(PositionComponent.class).position,
                            projectile.getComponent(SpriteComponent.class).size,
                            false, 1f)
                    )
                    .add(new RenderComponent());
            addVariableComponents();
            return projectile;
        }
    }

    // Adds basic universal components to the projectile
    public Entity createChildProjectile(Entity parentProjectile) {
        {
            Entity childProjectile = new Entity();
            childProjectile.add(new ProjectileDamageComponent(5, 5))
                    .add(new SpriteComponent(
                            new Texture("cannonball.png"),
                            15f, 15f)
                    )
                    .add(new PositionComponent(
                            parentProjectile.getComponent(PositionComponent.class).position.x,
                            parentProjectile.getComponent(PositionComponent.class).position.y)
                    )
                    .add(new Box2DComponent(
                            parentProjectile.getComponent(PositionComponent.class).position,
                            parentProjectile.getComponent(SpriteComponent.class).size,
                            false, 1f)
                    )
                    .add(new RenderComponent());
            return childProjectile;
        }

    }

    // Abstract function for adding variable components
    abstract void addVariableComponents();

}
