package com.mygdx.game.ECS.Projectiles;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.ParentComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileComponent;
import com.mygdx.game.ECS.components.RenderComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.managers.EntityManager;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.utils.B2DConstants.BIT_GROUND;
import static com.mygdx.game.utils.B2DConstants.BIT_PLAYER;
import static com.mygdx.game.utils.B2DConstants.BIT_PROJECTILE;


/**
 * This abstract class is responsible for dictating the behavior of different Projectile types
 **/
public abstract class Projectile {
    Entity projectile = new Entity(); // This projectile entity is modified through this class and then returned

    // Using a component mapper is the fastest way to load entities
    private final ComponentMapper<ProjectileComponent> pdm = ComponentMapper.getFor(ProjectileComponent.class);
    private final ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);

    Projectile(Entity parent) {
        // Adds a parent to the projectile -> the parent should be entity that this projectile spawned from
        projectile.add(new ParentComponent(parent));
        createProjectile(); // Create the projectile
        EM.engine.addEntity(projectile); // Add the new projectile to the engine
    }

    // This function is responsible for giving the projectile an impulse when it is shot
    public void ShootProjectile(ShootingComponent shootingComponent) {
        // Shoot projectile with Box2D impulse
        Box2DComponent b2d = b2dm.get(projectile); // Get Box2D component
        float impulse = (float) (this.pdm.get(projectile).speed * shootingComponent.power);
        Vector2 impulseVector = new Vector2(
                impulse * (float) Math.sin(shootingComponent.angle),
                impulse * (float) Math.cos(shootingComponent.angle)); // Calculate velocity
        b2d.body.applyLinearImpulse(impulseVector, b2d.body.getWorldCenter(), false); // Apply impulse to body
        shootingComponent.power = 0; // Reset the power when the shot has been taken
    }

    // Adds basic universal components to the projectile
    public Entity createProjectile() {
        Entity parent = projectile.getComponent(ParentComponent.class).parent; // Get this projectile's parent
        projectile
                .add(new SpriteComponent(
                        new Texture("cannonball.png"),
                        15f, 15f)
                )
                .add(new PositionComponent(
                        parent.getComponent(PositionComponent.class).position.x,
                        parent.getComponent(PositionComponent.class).position.y + parent.getComponent(SpriteComponent.class).size.y)
                )
                .add(new Box2DComponent(
                        projectile.getComponent(PositionComponent.class).position,
                        projectile.getComponent(SpriteComponent.class).size,
                        false,
                        10f,
                        BIT_PROJECTILE,
                        (short) (BIT_PLAYER | BIT_GROUND))
                )
                .add(new RenderComponent());
        addVariableComponents(); // Add variable component -> is different for different subclasses

        return projectile;
    }

    // Set this projectile to the parent's position
    public void setPositionToParent() {
        Entity parent = projectile.getComponent(ParentComponent.class).parent; // Get this projectile's parent
        b2dm.get(projectile).body.setLinearVelocity(b2dm.get(parent).body.getLinearVelocity());
    }

    // Abstract function for adding variable components
    abstract void addVariableComponents();

    // What happens when the projectile collides with something
    public abstract void collision();

    // Implements functionality for when the projectile reaches peak height
    public abstract void midAir();
}
