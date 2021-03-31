package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.ECS.components.PlayerComponent;

import static com.mygdx.game.managers.GameStateManager.GSM;

/**
 * This system is responsible for removing and adding components between rounds
 * This system is also responsible for stopping and starting systems between rounds
 * TODO: Improve state functionality
 **/
public class GameplaySystem extends EntitySystem {
    // Entity arrays
    public ImmutableArray<Entity> players; // List of players
    public ImmutableArray<Entity> powerBars; // PowerBar entities

    // Single entities
    public Entity player; // Current player entity

    // Get entities
    public void addedToEngine(Engine e) {
        // Get player entities
        this.players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    // Update function for GameplaySystem (calls automatically by engine)
    public void update(float deltaTime) {
        // Get current player (the player who's turn it is)
        this.player = players.get(GSM.currentPlayer);

        // Tell the state manager how many players are in the game
        GSM.numberOfPlayers = players.size();

        // Check which gameState the system is in -> Remove or add component, start or stop systems
        if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.SWITCH_ROUND)) {
            // Remove or add components to entities
            EntityManager.powerBar.remove(RenderableComponent.class); // Remove render of power bar
            EntityManager.powerBarArrow.remove(RenderableComponent.class); // Remove render of power bar arrow
            EntityManager.aimArrow.remove(RenderableComponent.class); // Remove render of aim arrow

            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(ShootingSystem.class).setProcessing(false);
            getEngine().getSystem(ControllerSystem.class).setProcessing(false);
        } else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.START_ROUND)) {
            // Remove or add components to entities
            player.add(new MovementControlComponent());

            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(ShootingSystem.class).setProcessing(false);
            getEngine().getSystem(ControllerSystem.class).setProcessing(true);
        } else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PLAYER_AIM)) {
            // Remove or add components to entities
            player.remove(MovementControlComponent.class); // The player should loose ability to move whilst aiming
            EntityManager.aimArrow.add(new RenderableComponent()); // Render the aim arrow

            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(AimingSystem.class).setProcessing(true);
            getEngine().getSystem(ShootingSystem.class).setProcessing(false);
            getEngine().getSystem(ControllerSystem.class).setProcessing(false);
        } else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PLAYER_SHOOT)) {
            // Remove or add components to entities
            EntityManager.powerBar.add(new RenderableComponent()); // Render power bar
            EntityManager.powerBarArrow.add(new RenderableComponent()); // Render power bar arrow

            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(ShootingSystem.class).setProcessing(true);
            getEngine().getSystem(AimingSystem.class).setProcessing(false);
            getEngine().getSystem(ControllerSystem.class).setProcessing(false);
        }
    }
}