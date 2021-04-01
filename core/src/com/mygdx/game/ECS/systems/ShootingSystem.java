package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.ProjectileCreator;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.ProjectileDamageComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.managers.GameStateManager;

import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.utils.GameConstants.MAX_SHOOTING_POWER;
import static com.mygdx.game.utils.GameConstants.ROUND_TIME;


/**
 * For charging power to a shot and then shooting the projectile
 **/
public class ShootingSystem extends EntitySystem {
    private ImmutableArray<Entity> players; // Array for all player entities that are aiming

    // Using a component mapper is the fastest way to load entities
    private final ComponentMapper<ShootingComponent> sm = ComponentMapper.getFor(ShootingComponent.class);
    private final ComponentMapper<ProjectileDamageComponent> pdm = ComponentMapper.getFor(ProjectileDamageComponent.class);

    // Store all entities with respective components to entity arrays
    public void addedToEngine(Engine e) {
        players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float dt) {
        // Check first if there are any players shooting
        if (this.players.size() > 0) {
            if (!GSM.pauseTimer) {
                // Get the the player whose turn it is and get its shootingComponent
                Entity player = this.players.get(GSM.currentPlayer);
                ShootingComponent shootingComponent = this.sm.get(player);

                // Increase the power -> since we are now charging power for the shot
                shootingComponent.power += dt; // Reset in GamePlaySystem (SWITCH_ROUND)

                // Shoot if S key stops being pressed, power reaches max, or round time is reached
                if (!Gdx.input.isKeyPressed(Input.Keys.S) || shootingComponent.power >= MAX_SHOOTING_POWER || GSM.time > ROUND_TIME) {
                    shootProjectile(); // Create projectile and shoot it (deleted on collision)
                    GSM.setGameState(GameStateManager.STATE.PROJECTILE_AIRBORNE); // GSM.time paused on start() and resumed on end()
                }
            }
        }
    }

    // Function for creating and shooting projectile from current player
    private void shootProjectile() {
        Entity player = this.players.get(GSM.currentPlayer);
        ShootingComponent playerShot = this.sm.get(player);
        ProjectileCreator projectileCreator = new ProjectileCreator();

        // Create and add entities to engine
        Entity projectile = projectileCreator.createProjectile(player, playerShot); // Create projectile
        getEngine().addEntity(projectile); // Add the new projectile to the engine

        // Shoot projectile with Box2D impulse
        Box2DComponent b2d = projectile.getComponent(Box2DComponent.class); // Get Box2D component
        Vector2 impulseVector = calculateAngleVelocity(this.pdm.get(projectile).speed); // Calculate velocity
        b2d.body.applyLinearImpulse(impulseVector, b2d.body.getWorldCenter(), false); // Apply impulse to body
    }

    // Calculate velocity vector with angle
    private Vector2 calculateAngleVelocity(double speed) {
        // Get the the player whose turn it is and get its shootingComponent
        Entity player = this.players.get(GSM.currentPlayer);
        ShootingComponent shootingComponent = this.sm.get(player);

        // Calculate how much impulse should be given to the projectile
        // -> given by projectile speed and shootingComponent power
        float impulse = (float) (speed * Math.pow(shootingComponent.power, 2));

        // Return a vector that decomposes the impulse in x and y component depending on shootingComponent angle
        return new Vector2(
                impulse * (float) Math.sin(shootingComponent.angle),
                impulse * (float) Math.cos(shootingComponent.angle)
        );
    }
}
