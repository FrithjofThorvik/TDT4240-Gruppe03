package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.components.flags.PlayerComponent;
import com.mygdx.game.ECS.components.flags.RenderComponent;
import com.mygdx.game.ECS.components.misc.PositionComponent;
import com.mygdx.game.ECS.components.misc.ShootingComponent;
import com.mygdx.game.ECS.components.misc.SpriteComponent;
import com.mygdx.game.ECS.components.flags.isAimingComponent;
import com.mygdx.game.gamelogic.states.GameStateManager;

import static com.mygdx.game.ECS.managers.ECSManager.ECSManager;
import static com.mygdx.game.gamelogic.states.GameStateManager.GSM;
import static com.mygdx.game.utils.GameController.CM;


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
                PositionComponent playerPosition = ECSManager.positionMapper.get(player); // Get the position component of that player
                ShootingComponent shootingComponent = ECSManager.shootingMapper.get(player);

                ECSManager.UIManager.getAimArrow().add(new RenderComponent());

                repositionAimArrow(player); // Reposition the aim arrow
                if (!CM.checkControllerIsTouched()) // We don't want to calculate aim if we are touching controllers
                    shootingComponent.angle = this.calculateAimAngle(playerPosition); // Update angle in the players ShootingComponent

                // Check if power button is pushed
                if (CM.powerPressed)
                    GSM.setGameState(GameStateManager.STATE.PLAYER_SHOOTING);
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

    // Call to make the ai marrow update according to player aim angle
    private void repositionAimArrow(Entity player) {
        PositionComponent playerPos = ECSManager.positionMapper.get(player);
        SpriteComponent playerSprite = ECSManager.spriteMapper.get(player);

        // Get the angle (in degrees) and power of the currentPlayer's shootingComponent
        double aimAngleInDegrees = 90f - (float) ECSManager.shootingMapper.get(player).angle / (float) Math.PI * 180f;

        // Set rotation and position of AimArrow (displayed above the player -> rotated by where the player aims)
        ECSManager.spriteMapper.get(ECSManager.UIManager.getAimArrow()).sprite.setRotation((float) aimAngleInDegrees);
        ECSManager.positionMapper.get(ECSManager.UIManager.getAimArrow()).position.x = playerPos.position.x;
        ECSManager.positionMapper.get(ECSManager.UIManager.getAimArrow()).position.y = playerPos.position.y + playerSprite.size.y;
    }
}

