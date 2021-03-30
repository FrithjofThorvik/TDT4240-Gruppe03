package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.PowerBarComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.ECS.components.PlayerComponent;

import static com.mygdx.game.managers.GameStateManager.GSM;

/**
 * This system is responsible for removing and adding components between rounds
 * TODO: Improve state functionality
 **/
public class GameplaySystem extends EntitySystem {
    // Entity arrays
    public ImmutableArray<Entity> players; // List of players
    public ImmutableArray<Entity> powerBars; // PowerBar entities

    // Single entities
    public Entity player; // Current player entity
    public Entity powerBar; // Power bar sprite (green -> red)
    public Entity powerBarArrow; // Arrow displaying current shot power

    ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);


    // Get entities
    public void addedToEngine(Engine e) {
        // Get entities
        this.players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
        this.powerBars = e.getEntitiesFor(Family.all(PowerBarComponent.class).get());
    }

    // Update function for GameplaySystem (calls automatically by engine)
    public void update(float deltaTime) {
        // Update entities
        this.player = players.get(GSM.currentPlayer);
        this.powerBar = powerBars.get(0);
        this.powerBarArrow = powerBars.get(1);

        // Tell the state manager how many players are in the game
        GSM.numberOfPlayers = players.size();

        if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.SWITCH_ROUND)) {
            player.remove(TakeAimComponent.class); // Remove TakeAim component
            player.remove(MovementControlComponent.class); // Disable player movement

            powerBar.remove(RenderableComponent.class); // Remove render of power bar
            powerBarArrow.remove(RenderableComponent.class); // Remove render of power bar arrow
        } else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.START_ROUND)) {
            player.add(new MovementControlComponent()); // Enable new player to move
        } else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PLAYER_AIM)) {
            player.add(new TakeAimComponent()); // Add aim component to player
            player.remove(MovementControlComponent.class); // Disable player movement

            powerBar.add(new RenderableComponent()); // Render power bar
            powerBarArrow.add(new RenderableComponent()); // Render power bar arrow
        }
    }
}