package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.managers.GameStateManager;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.managers.ControlManager.CM;


/**
 * This system is responsible for moving the player when it is that players turn
 **/
public class MovementSystem extends EntitySystem {
    // Prepare arrays for entities
    private ImmutableArray<Entity> movingPlayers;

    // Store all entities with respective components to entity arrays
    public void addedToEngine(Engine e) {
        this.movingPlayers = e.getEntitiesFor(Family.all(PlayerComponent.class, MovementControlComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float dt) {
        if (this.movingPlayers.size() > 0) {
            // Check if screen is pressed, and handle the input with the ControllerComponent
            if (Gdx.input.isTouched()) {
                // Check if aim button is pressed
                if (CM.aimPressed)
                    GSM.setGameState(GameStateManager.STATE.PLAYER_AIMING);
                else {
                    // Loop through all moving player entities
                    for (int i = 0; i < this.movingPlayers.size(); ++i) {
                        // Get player entity with controller component and handle movement
                        Entity movingPlayer = this.movingPlayers.get(i);
                        this.handleMovement(movingPlayer); // Move player
                    }
                }
            }
        }
    }

    // Handles user input, and will handle player movement and changing if GameState
    private void handleMovement(Entity player) {

        // Get entity components
        VelocityComponent playerVelocity = EM.velocityMapper.get(player);
        Box2DComponent playerBox2D = EM.b2dMapper.get(player);
        SpriteComponent playerSprite = EM.spriteMapper.get(player);

        if (CM.rightPressed) {
            playerBox2D.body.applyLinearImpulse(playerVelocity.velocity, playerBox2D.body.getWorldCenter(), false);

            // Flip sprite if it is already flipped from it's original state
            if (playerSprite.sprite.isFlipX())
                playerSprite.sprite.flip(true, false);

        } else if (CM.leftPressed) {
            Vector2 negativeImpulse = new Vector2(-playerVelocity.velocity.x, playerVelocity.velocity.y);
            playerBox2D.body.applyLinearImpulse(negativeImpulse, playerBox2D.body.getWorldCenter(), false);

            // Flip sprite if it is not flipped from it's initial state
            if (!playerSprite.sprite.isFlipX())
                playerSprite.sprite.flip(true, false);
        }
    }
}
