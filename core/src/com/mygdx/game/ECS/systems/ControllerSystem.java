package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.ECS.Controller;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.ControllerComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.states.screens.GameScreen;

import static com.mygdx.game.managers.GameStateManager.GSM;


/**
 * This system is responsible for moving the player when it is that players turn
 **/
public class ControllerSystem extends EntitySystem {
    // Controller creating buttons
    private Controller controller;

    // Prepare arrays for entities
    private ImmutableArray<Entity> movingPlayers;
    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> controllers;

    // Prepare component mappers
    private final ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private final ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);
    private final ComponentMapper<ShootingComponent> shm = ComponentMapper.getFor(ShootingComponent.class);
    private final ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);

    // Store all entities with respective components to entity arrays
    public void addedToEngine(Engine e) {
        this.movingPlayers = e.getEntitiesFor(Family.all(PlayerComponent.class, MovementControlComponent.class).get());
        this.players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
        this.controllers = e.getEntitiesFor(Family.all(ControllerComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float dt) {
        if (this.players.size() > 0 && this.controllers.size() > 0) {
            // Check if controller has been initialized
            if (this.controller != null) {
                Entity currentPlayer = this.players.get(GSM.currentPlayer);

                // Check if current is in a state where movement is enabled
                if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.START_ROUND)) {
                    this.controller.startMoving(); // Make everything, but the movement buttons, transparent
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

                // Check if player is shooting
                else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PLAYER_SHOOTING)) {
                    this.handleShooting(currentPlayer);
                }

                // Make controller buttons idle, if a player is not moving or shooting
                else {
                    this.controller.idle();
                }

            } else {
                this.controller = new Controller(this.controllers.first()); // Initialize controller
            }
        }
    }

    // Handles user input, and will handle player movement and changing if GameState
    private void handleMovement(Entity player) {
        // Get entity components
        VelocityComponent playerVelocity = this.vm.get(player);
        Box2DComponent playerBox2D = this.b2dm.get(player);
        SpriteComponent playerSprite = this.sm.get(player);

        if (this.controller.buttonPresses.rightPressed) {
            playerBox2D.body.applyLinearImpulse(playerVelocity.velocity, playerBox2D.body.getWorldCenter(), false);

            // Flip sprite if it is already flipped from it's original state
            if (playerSprite.sprite.isFlipX())
                playerSprite.sprite.flip(true, false);

        } else if (controller.buttonPresses.leftPressed) {
            Vector2 negativeImpulse = new Vector2(-playerVelocity.velocity.x, playerVelocity.velocity.y);
            playerBox2D.body.applyLinearImpulse(negativeImpulse, playerBox2D.body.getWorldCenter(), false);

            // Flip sprite if it is not flipped from it's initial state
            if (!playerSprite.sprite.isFlipX())
                playerSprite.sprite.flip(true, false);

        } else if (controller.buttonPresses.aimPressed) {
            this.controller.startShooting(); // Make everything, but the power button, transparent
            GSM.setGameState(GameStateManager.STATE.PLAYER_SHOOTING);
        }
    }

    // Handles the increase of pressing shooting power button
    private void handleShooting(Entity player) {
        // Get entity components
        ShootingComponent shootingComponent = this.shm.get(player);

        // Check if power button is being pressed and increase shot power
        if (this.controller.buttonPresses.powerPressed) {
            shootingComponent.power += Gdx.graphics.getDeltaTime(); // Increase power of shot
            shootingComponent.shooting = true; // Used in ShootingSystem
        }

        // Check if power button has stopped being pressed after being pressed
        else if (shootingComponent.power > 0) {
            this.controller.idle(); // Make all controller button transparent
            shootingComponent.shooting = false; // Used in ShootingSystem
        }
    }
}
