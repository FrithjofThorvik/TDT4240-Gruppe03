package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.flags.PlayerComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.ECS.components.flags.isShootingComponent;
import com.mygdx.game.managers.EntityCreator;
import com.mygdx.game.managers.GameStateManager;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.managers.ControlManager.CM;
import static com.mygdx.game.utils.B2DConstants.PPM;
import static com.mygdx.game.utils.GameConstants.MAX_SHOOTING_POWER;


/**
 * This system should control the aiming of a projectile
 */
public class ShootingSystem extends EntitySystem {

    private ImmutableArray<Entity> playersShooting; // Array for all player entities that are aiming


    // Store all entities with respective components to entity arrays
    public void addedToEngine(Engine e) {
        this.playersShooting = e.getEntitiesFor(Family.all(PlayerComponent.class, isShootingComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float dt) {
        // Check first if there are any players aiming
        for (int i = 0; i < this.playersShooting.size(); i++) {
            Entity player = this.playersShooting.get(i);
            ShootingComponent shootingComponent = EM.shootingMapper.get(player);

            shootingComponent.power += dt; // Increase power

            // Create & shoot projectile if button stops being pressed, max power is reached, or round time is reached
            if (!CM.powerPressed || shootingComponent.power >= MAX_SHOOTING_POWER) {
                Entity projectile = EM.entityCreator.getProjectileClass(EntityCreator.PROJECTILES.values()[CM.currentProjectile]).createEntity(); // Get the projectile chosen from the Controller
                ShootProjectile(projectile, shootingComponent, player); // Shoot the projectile
                GSM.setGameState(GameStateManager.STATE.PROJECTILE_AIRBORNE); // Change the game state
            }
        }
    }

    public void ShootProjectile(Entity projectile, ShootingComponent shootingComponent, Entity player) {
        // Shoot projectile with Box2D impulse
        EM.b2dMapper.get(projectile).body.setTransform((EM.positionMapper.get(player).position.x) / PPM, (EM.positionMapper.get(player).position.y + EM.spriteMapper.get(player).size.y) / PPM, 0);
        Box2DComponent b2d = EM.b2dMapper.get(projectile); // Get Box2D component
        float impulse = (float) (EM.projectileMapper.get(projectile).speed * shootingComponent.power);
        Vector2 impulseVector = new Vector2(
                impulse * (float) Math.sin(shootingComponent.angle),
                impulse * (float) Math.cos(shootingComponent.angle)); // Calculate velocity
        b2d.body.applyLinearImpulse(impulseVector, b2d.body.getWorldCenter(), false); // Apply impulse to body
        shootingComponent.power = 0; // Reset the power when the shot has been taken
    }
}
