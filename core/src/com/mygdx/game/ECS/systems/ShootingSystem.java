package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.ECS.Projectiles.SplitterProjectile;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.managers.GameStateManager;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.utils.GameConstants.MAX_SHOOTING_POWER;
import static com.mygdx.game.utils.GameConstants.ROUND_TIME;


/**
 * For charging power to a shot and then shooting the projectile
 **/
public class ShootingSystem extends EntitySystem {
    private ImmutableArray<Entity> players; // Array for all player entities that are aiming

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
                ShootingComponent shootingComponent = EM.shootingMapper.get(player);

                // Increase the power -> since we are now charging power for the shot
                shootingComponent.power += dt; // Reset in GamePlaySystem (SWITCH_ROUND)

                // Shoot if S key stops being pressed, power reaches max, or round time is reached
                if (!Gdx.input.isKeyPressed(Input.Keys.S) || shootingComponent.power >= MAX_SHOOTING_POWER || GSM.time > ROUND_TIME) {
                    new SplitterProjectile(player).ShootProjectile(shootingComponent); // Create new projectile and shoot it
                    GSM.setGameState(GameStateManager.STATE.PROJECTILE_AIRBORNE); // GSM.time paused on start() and resumed on end()
                }
            }
        }
    }
}
