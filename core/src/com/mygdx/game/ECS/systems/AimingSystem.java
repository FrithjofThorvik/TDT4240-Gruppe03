package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.ECS.components.isAimingComponent;
import com.mygdx.game.managers.GameStateManager;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.managers.ControlManager.CM;


/**
 * This system is responsible for handling when player is aiming projectile
 **/
public class AimingSystem extends EntitySystem {
    // Prepare arrays for entities
    private ImmutableArray<Entity> playersAiming;

    // Add entities to arrays
    public void addedToEngine(Engine e) {
        this.playersAiming = e.getEntitiesFor(Family.all(PlayerComponent.class, isAimingComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float dt) {
        // Check first if there are any players aiming
        for (int i = 0; i < this.playersAiming.size(); i++) {
            // Calculate the aim angle when the screen is touched
            if (Gdx.input.isTouched()) {
                Entity player = this.playersAiming.get(i); // Get current player entity
                PositionComponent playerPosition = EM.positionMapper.get(player); // Get the position component of that player
                ShootingComponent shootingComponent = EM.shootingMapper.get(player);

                // Aim if power button hasn't been pressed
                if (Gdx.input.isTouched()) {
                    // Check if power button is pushed
                    if (CM.powerPressed)
                        GSM.setGameState(GameStateManager.STATE.PLAYER_SHOOTING);
                    else {
                        shootingComponent.angle = this.calculateAimAngle(playerPosition); // Update angle in the players ShootingComponent
                    }
                }
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
        touchPoint = Application.camera.unproject(touchPoint);

        // Find the aim angle in radians
        return Math.atan2(touchPoint.x - position.position.x, touchPoint.y - position.position.y);
    }
}

