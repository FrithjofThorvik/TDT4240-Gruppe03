package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.ECS.Projectiles.SplitterProjectile;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.states.screens.GameScreen;

import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.utils.GameConstants.MAX_SHOOTING_POWER;
import static com.mygdx.game.utils.GameConstants.ROUND_TIME;


/**
 * This system should control the aiming of a projectile
 * The system is paused & resumed in GamePlaySystem
 */
public class ShootingSystem extends EntitySystem {

    private ImmutableArray<Entity> players; // Array for all player entities that are aiming

    // Using a component mapper is the fastest way to load entities
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<ShootingComponent> sm = ComponentMapper.getFor(ShootingComponent.class);

    // Add entities to arrays
    public void addedToEngine(Engine e) {
        this.players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float dt) {
        // Check first if there are any players aiming
        if (this.players.size() > 0) {
            Entity player = this.players.get(GSM.currentPlayer);
            ShootingComponent shootingComponent = this.sm.get(player);

            // Aim if power button hasn't been pressed
            if (shootingComponent.power ==  0) {
                if (Gdx.input.isTouched()) {
                    PositionComponent playerPosition = this.pm.get(player); // Get the position component of that player
                    shootingComponent.angle = calculateAimAngle(playerPosition); // Update angle in the players ShootingComponent
                }
            }

            // Create & shoot projectile if button stops being pressed, max power is reached, or round time is reached
            else if (!shootingComponent.shooting || shootingComponent.power >= MAX_SHOOTING_POWER || GSM.time > ROUND_TIME) {
                new SplitterProjectile(player).ShootProjectile(shootingComponent); // Create new projectile and shoot it
                GSM.setGameState(GameStateManager.STATE.PROJECTILE_AIRBORNE); // GSM.time paused on start() and resumed on end()
            }
        }
    }

    // Calculate angle of click relative to player position
    private double calculateAimAngle(PositionComponent position) {
        // Get the screen position of touch/click
        float xTouchPixels = Gdx.input.getX();
        float yTouchPixels = Gdx.input.getY();

        // Convert touch/click to world position
        Vector3 touchPoint = new Vector3(xTouchPixels, yTouchPixels, 0);
        touchPoint = GameScreen.camera.unproject(touchPoint);

        // Find the aim angle in radians
        return Math.atan2(touchPoint.x - position.position.x, touchPoint.y - position.position.y);
    }
}
