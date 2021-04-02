package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ECS.Projectiles.ProjectileCreator;
import com.mygdx.game.ECS.Projectiles.SplitterProjectile;
import com.mygdx.game.ECS.Projectiles.StandardProjectile;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.ParentComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.BouncyComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileDamageComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.SplitterComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.VelocityComponent;


/**
 * This system should control a projectile after it has been created/fired
 * TODO: Implement projectile functionality
 **/
public class ProjectileSystem extends EntitySystem {
    // Prepare arrays for entities
    private ImmutableArray<Entity> projectiles;
    private ImmutableArray<Entity> splitterProjectiles;
    private ImmutableArray<Entity> bouncyProjectiles;

    // Prepare component mappers
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private final ComponentMapper<SpriteComponent> spm = ComponentMapper.getFor(SpriteComponent.class);
    private final ComponentMapper<ProjectileDamageComponent> em = ComponentMapper.getFor(ProjectileDamageComponent.class);
    private final ComponentMapper<BouncyComponent> bm = ComponentMapper.getFor(BouncyComponent.class);
    private final ComponentMapper<SplitterComponent> sm = ComponentMapper.getFor(SplitterComponent.class);
    private final ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);

    // Will be called automatically by the engine
    public void addedToEngine(Engine e) {
        this.projectiles = e.getEntitiesFor(Family.all(ProjectileDamageComponent.class, PositionComponent.class).get());
        this.splitterProjectiles = e.getEntitiesFor(Family.all(ProjectileDamageComponent.class, PositionComponent.class, SplitterComponent.class).get());
        this.bouncyProjectiles = e.getEntitiesFor(Family.all(ProjectileDamageComponent.class, PositionComponent.class, BouncyComponent.class).get());
    }

    // Update function for ProjectileSystem
    public void update(float deltaTime) {
        if (this.projectiles.size() > 0) {
            // Loop through projectile entities
            for (int i = 0; i < this.projectiles.size(); ++i) {
                // Get projectile
                Entity entity = this.projectiles.get(i);

                // Get projectile components
                ProjectileDamageComponent projectileDamage = this.em.get(entity);
                PositionComponent projectilePosition = this.pm.get(entity);
                VelocityComponent projectileVelocity = this.vm.get(entity);

                // TODO: Implement projectile damage functionality
            }

            for (int i = 0; i < this.splitterProjectiles.size(); i++) {
                // Get projectile
                Entity parentProjectile = this.projectiles.get(i);
                SplitterComponent splitterComponent = sm.get(parentProjectile);
                Box2DComponent b2dComponent = b2dm.get(parentProjectile);

                if (splitterComponent.numberOfSplits > 0 && b2dComponent.body.getLinearVelocity().y <= 0) {
                    Entity childProjectile = new SplitterProjectile().createChildProjectile(parentProjectile);
                    childProjectile.add(new ParentComponent(parentProjectile));
                    pm.get(childProjectile).position.y += splitterComponent.numberOfSplits * (spm.get(childProjectile).size.y+5);
                    b2dm.get(childProjectile).body.setTransform(pm.get(childProjectile).position,0);
                    Vector2 impulseVector = new Vector2((20-splitterComponent.numberOfSplits) *20,0); // Calculate velocity
                    b2dm.get(childProjectile).body.applyLinearImpulse(impulseVector, b2dm.get(childProjectile).body.getWorldCenter(), false); // Apply impulse to body
                    splitterComponent.numberOfSplits -= 1;
                    getEngine().addEntity(childProjectile);
                }
            }
        }
    }
}
