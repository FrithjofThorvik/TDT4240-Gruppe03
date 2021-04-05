package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.states.screens.GameScreen;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.GameStateManager.GSM;


/**
 * This system should control the aiming of a projectile
 * The system is paused & resumed in GamePlaySystem
 */
public class AimingSystem extends EntitySystem {

    private ImmutableArray<Entity> players; // Array for all player entities that are aiming

    // Add entities to arrays
    public void addedToEngine(Engine e) {
        this.players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float dt) {
        // Check first if there are any players aiming
        if (this.players.size() > 0) {
            // Calculate the aim angle when the screen is touched
            if (Gdx.input.isTouched()) {
                Entity player = this.players.get(GSM.currentPlayer); // Get current player entity
                PositionComponent playerPosition = EM.positionMapper.get(player); // Get the position component of that player
                ShootingComponent playerShot = EM.shootingMapper.get(player);

                // Update angle in the players ShootingComponent
                playerShot.angle = calculateAimAngle(playerPosition);
            }

            // When the player presses "S" change state to shooting
            if (Gdx.input.isKeyPressed(Input.Keys.S))
                GSM.setGameState(GameStateManager.STATE.PLAYER_SHOOT);
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
