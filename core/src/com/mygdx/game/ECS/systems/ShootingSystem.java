package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.components.flags.RenderComponent;
import com.mygdx.game.ECS.components.misc.Box2DComponent;
import com.mygdx.game.ECS.components.flags.PlayerComponent;
import com.mygdx.game.ECS.components.misc.ShootingComponent;
import com.mygdx.game.ECS.components.flags.isShootingComponent;
import com.mygdx.game.ECS.EntityUtils.EntityTemplateMapper;
import com.mygdx.game.ECS.managers.ECSManager;
import com.mygdx.game.gamelogic.states.GameStateManager;
import com.mygdx.game.utils.GameController;

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
            ShootingComponent shootingComponent = ECSManager.getInstance().shootingMapper.get(player);
            updatePowerBar(player);
            ECSManager.getInstance().UIManager.getPowerBar().add(new RenderComponent());
            ECSManager.getInstance().UIManager.getPowerBarArrow().add(new RenderComponent());

            shootingComponent.power += dt; // Increase power

            // Create & shoot projectile if button stops being pressed, max power is reached, or round time is reached
            if (!GameController.getInstance().powerPressed || shootingComponent.power >= MAX_SHOOTING_POWER) {
                Entity projectile = ECSManager.getInstance().getEntityTemplateMapper().getProjectileClass(EntityTemplateMapper.PROJECTILES.values()[GameController.getInstance().currentProjectile]).createEntity(); // Get the projectile chosen from the Controller
                ShootProjectile(projectile, shootingComponent, player); // Shoot the projectile

                ECSManager.getInstance().UIManager.getPowerBar().remove(RenderComponent.class);
                ECSManager.getInstance().UIManager.getPowerBarArrow().remove(RenderComponent.class);
                ECSManager.getInstance().UIManager.getAimArrow().remove(RenderComponent.class);
                GameStateManager.getInstance().setGameState(GameStateManager.STATE.PROJECTILE_AIRBORNE); // Change the game state
            }
        }
    }

    // Display the powerbar according to player power
    public void updatePowerBar(Entity player) {
        // Calculate the startingPosition of an powerBar arrow (this is done here so that if the screen is resized the arrowPosition is updated)
        float startPositionArrow = ECSManager.getInstance().positionMapper.get(ECSManager.getInstance().UIManager.getPowerBar()).position.y - ECSManager.getInstance().spriteMapper.get(ECSManager.getInstance().UIManager.getPowerBar()).size.y / 2;

        //Set position of powerBarArrow -> given the power of the shootingComponent
        float power = ECSManager.getInstance().shootingMapper.get(player).power;
        ECSManager.getInstance().positionMapper.get(ECSManager.getInstance().UIManager.getPowerBarArrow()).position.y = startPositionArrow + (ECSManager.getInstance().spriteMapper.get(ECSManager.getInstance().UIManager.getPowerBar()).size.y * (power / MAX_SHOOTING_POWER));
    }

    public void ShootProjectile(Entity projectile, ShootingComponent shootingComponent, Entity player) {
        // Increase projectile damage by the player's damageMultiplier;
        ECSManager.getInstance().projectileMapper.get(projectile).damage *= shootingComponent.damageMult;

        // Shoot projectile with Box2D impulse
        ECSManager.getInstance().b2dMapper.get(projectile).body.setTransform((ECSManager.getInstance().positionMapper.get(player).position.x) / PPM, (ECSManager.getInstance().positionMapper.get(player).position.y + ECSManager.getInstance().spriteMapper.get(player).size.y) / PPM, 0);
        Box2DComponent b2d = ECSManager.getInstance().b2dMapper.get(projectile); // Get Box2D component
        float impulse = (float) (ECSManager.getInstance().projectileMapper.get(projectile).speed * shootingComponent.power);
        Vector2 impulseVector = new Vector2(
                impulse * (float) Math.sin(shootingComponent.angle),
                impulse * (float) Math.cos(shootingComponent.angle)); // Calculate velocity
        b2d.body.applyLinearImpulse(impulseVector, b2d.body.getWorldCenter(), false); // Apply impulse to body
        shootingComponent.power = 0; // Reset the power when the shot has been taken
    }
}
