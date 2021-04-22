package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.components.misc.Box2DComponent;
import com.mygdx.game.ECS.components.flags.MovementControlComponent;
import com.mygdx.game.ECS.components.flags.PlayerComponent;
import com.mygdx.game.ECS.components.misc.SpriteComponent;
import com.mygdx.game.ECS.components.misc.VelocityComponent;

import static com.mygdx.game.ECS.managers.ECSManager.ECSManager;
import static com.mygdx.game.utils.GameController.CM;


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
                // Loop through all moving player entities
                for (int i = 0; i < this.movingPlayers.size(); ++i) {
                    // Get player entity with controller component and handle movement
                    Entity movingPlayer = this.movingPlayers.get(i);
                    this.handleMovement(movingPlayer); // Move player
                }
            }
        }
    }

    // Handles user input, and will handle player movement and changing if GameState
    private void handleMovement(Entity player) {

        // Get entity components
        VelocityComponent playerVelocity = ECSManager.velocityMapper.get(player);
        Box2DComponent playerBox2D = ECSManager.b2dMapper.get(player);
        SpriteComponent playerSprite = ECSManager.spriteMapper.get(player);

        if (CM.rightPressed) {
            playerBox2D.body.applyLinearImpulse(new Vector2(10f, 0f), playerBox2D.body.getWorldCenter(), false);

            // Flip sprite if it is already flipped from it's original state
            if (playerSprite.sprite.isFlipX())
                playerSprite.sprite.flip(true, false);

        } else if (CM.leftPressed) {
            Vector2 negativeImpulse = new Vector2(-10f, 0f);
            playerBox2D.body.applyLinearImpulse(negativeImpulse, playerBox2D.body.getWorldCenter(), false);

            // Flip sprite if it is not flipped from it's initial state
            if (!playerSprite.sprite.isFlipX())
                playerSprite.sprite.flip(true, false);
        }

        if (playerBox2D.body.getLinearVelocity().x > playerVelocity.velocity.x) {
            playerBox2D.body.setLinearVelocity(playerVelocity.velocity.x, playerBox2D.body.getLinearVelocity().y);
        } else if (playerBox2D.body.getLinearVelocity().x < -playerVelocity.velocity.x)
            playerBox2D.body.setLinearVelocity(-playerVelocity.velocity.x, playerBox2D.body.getLinearVelocity().y);
    }
}
