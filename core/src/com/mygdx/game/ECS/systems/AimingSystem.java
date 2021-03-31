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
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.states.screens.GameScreen;

import static com.mygdx.game.managers.GameStateManager.GSM;

/**
 * This system should control the aiming of a projectile
 * A player gets the TakeAimComponent when it is ready to aim
 * A player with the TakeAimComponent is controlled by the AimingSystem
 * TODO: Implement better shooting mechanics for projectiles
 */
public class AimingSystem extends EntitySystem {

    private ImmutableArray<Entity> players; // Array for all player entities that are aiming

    //Using a component mapper is the fastest way to load entities
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

    // AimingSystem constructor
    public AimingSystem() {
    }

    // Add entities to arrays
    public void addedToEngine(Engine e) {
        players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float deltaTime) {
        // Check first if there are any players aiming
        if (players.size() > 0) {
            Entity player = players.get(GSM.currentPlayer); // Get current player entity
            PositionComponent position = pm.get(player); // Get the position component of that player

            // Calculate the aim angle when the screen is touched
            if (Gdx.input.isTouched()) {
                double aimAngleInRad = calculateAimAngle(position);
                player.getComponent(ShootingComponent.class).angle = aimAngleInRad;
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
