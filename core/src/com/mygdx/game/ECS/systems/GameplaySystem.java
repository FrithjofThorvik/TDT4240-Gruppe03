package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.ECS.components.AimComponent;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.PlayerComponent;

/**
 * This system controls which player has the turn
 * This system is also responsible how long the rounds are, and how much time between each round
 * TODO: Improve state functionality
 **/
public class GameplaySystem extends EntitySystem {
    // Game state manager for managing rounds, switching, etc
    public GameStateManager gameStateManager;

    // Store all entities with respective components to entity arrays
    public void addedToEngine(Engine e) {
        // Arrays of entities that this system act on
        ImmutableArray<Entity> players = e.getEntitiesFor(Family.all(PlayerComponent.class).get()); // Get all entities that are players
        ImmutableArray<Entity> timers = e.getEntitiesFor(Family.all(FontComponent.class).get()); // Get all entities that are fonts
        ImmutableArray<Entity> aims = e.getEntitiesFor(Family.all(AimComponent.class).get()); // Get all entities that are fonts
        this.gameStateManager = new GameStateManager(players, timers, aims); // Create game state manager, and pass in entities
    }

    // Update function for GameplaySystem
    public void update(float dt) {
        gameStateManager.update(dt);
    }
}
