package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.managers.GameStateManager;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.managers.ControlManager.CM;
import static com.mygdx.game.utils.GameConstants.MAX_SHOOTING_POWER;
import static com.mygdx.game.utils.GameConstants.ROUND_TIME;


/**
 * This system should control the aiming of a projectile
 * The system is paused & resumed in GamePlaySystem
 */
public class ShootingSystem extends EntitySystem {

    private ImmutableArray<Entity> players; // Array for all player entities that are aiming


    // Store all entities with respective components to entity arrays
    public void addedToEngine(Engine e) {
        this.players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float dt) {
        // Check first if there are any players aiming
        if (this.players.size() > 0) {
            Entity player = this.players.get(GSM.currentPlayer);
            ShootingComponent shootingComponent = EM.shootingMapper.get(player);

            shootingComponent.power += dt; // Increase power

            // Create & shoot projectile if button stops being pressed, max power is reached, or round time is reached
            if (!CM.powerPressed || shootingComponent.power >= MAX_SHOOTING_POWER || GSM.time > ROUND_TIME) {
                new SplitterProjectile(player).ShootProjectile(shootingComponent); // Create new projectile and shoot it
                GSM.setGameState(GameStateManager.STATE.PROJECTILE_AIRBORNE); // GSM.time paused on start() and resumed on end()
            }
        }
    }
}
