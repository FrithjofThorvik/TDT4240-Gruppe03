package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.states.screens.GameScreen;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.GameStateManager.GSM;


/**
 * This system is responsible for moving the player when it is that players turn
 **/
public class ControllerSystem extends EntitySystem {
    // Prepare arrays for entities
    private ImmutableArray<Entity> players;

    // Store all entities with respective components to entity arrays
    public void addedToEngine(Engine e) {
        this.players = e.getEntitiesFor(Family.all(PlayerComponent.class, MovementControlComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float dt) {
        if (this.players.size() > 0) {
            // Loop through all player entities
            for (int i = 0; i < this.players.size(); ++i) {
                // Get player entity with controller component
                Entity player = this.players.get(i);

                // Move player when screen is touched
                if (Gdx.input.isTouched())
                    movePlayer(player);

                // When space is pressed -> change state to aiming
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
                    GSM.setGameState(GameStateManager.STATE.PLAYER_AIM);
            }
        }
    }

    // Move player with Box2D impulses
    public void movePlayer(Entity player) {
        // Get entity components
        PositionComponent playerPosition = EM.positionMapper.get(player);
        VelocityComponent playerVelocity = EM.velocityMapper.get(player);
        Box2DComponent playerBox2D = EM.b2dMapper.get(player);
        SpriteComponent playerSprite = EM.spriteMapper.get(player);

        // Get the screen position of the touch
        float xTouchPixels = Gdx.input.getX();
        float yTouchPixels = Gdx.input.getY();

        // Convert to world position
        Vector3 touchPoint = new Vector3(xTouchPixels, yTouchPixels, 0);
        touchPoint = GameScreen.camera.unproject(touchPoint);

        // Apply force to the players box2D body according to its velocity component
        if (playerPosition.position.x < touchPoint.x) {
            playerBox2D.body.applyLinearImpulse(playerVelocity.velocity, playerBox2D.body.getWorldCenter(), false);

            // Flip sprite if it is already flipped from it's original state
            if (playerSprite.sprite.isFlipX())
                playerSprite.sprite.flip(true, false);

        } else if (playerPosition.position.x > touchPoint.x) {
            Vector2 negativeImpulse = new Vector2(-playerVelocity.velocity.x, playerVelocity.velocity.y);
            playerBox2D.body.applyLinearImpulse(negativeImpulse, playerBox2D.body.getWorldCenter(), false);

            // Flip sprite if it is not flipped from it's initial state
            if (!playerSprite.sprite.isFlipX())
                playerSprite.sprite.flip(true, false);

        }
    }
}
