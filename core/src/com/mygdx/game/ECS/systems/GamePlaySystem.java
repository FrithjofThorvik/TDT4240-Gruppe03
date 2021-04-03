package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.ECS.components.ControllerComponent;
import com.mygdx.game.ECS.components.HealthComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.RenderComponent;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.ECS.components.PlayerComponent;

import static com.mygdx.game.managers.GameStateManager.GSM;


/**
 * This system is responsible for removing and adding components between rounds
 * This system is also responsible for stopping and starting systems between rounds
 **/
public class GamePlaySystem extends EntitySystem {
    // Entity arrays
    public ImmutableArray<Entity> players; // List of players
    public ImmutableArray<Entity> controllers; // List of controllers

    // Prepare component mappers
    private final ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);

    // Get entities
    public void addedToEngine(Engine e) {
        this.players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
        this.controllers = e.getEntitiesFor(Family.all(ControllerComponent.class).get());
    }

    // Update function for GamePlaySystem (calls automatically by engine)
    public void update(float dt) {
        if (this.players.size() > 0) {
            if (GSM.numberOfPlayers == 0)
                GSM.numberOfPlayers = this.players.size(); // Tell the state manager how many players are in the game

            this.checkHealth(); // Check if any health components have reached 0
            this.updateStates(); // Check which gameState the system is in -> Remove or add component, start or stop systems
        }
    }

    // Update game states by removing/adding components, starting/stopping systems, etc
    private void updateStates() {

        // START_GAME -> Displays a countdown until round starts
        if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.START_GAME)) {
            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(AimingSystem.class).setProcessing(false);
            getEngine().getSystem(ShootingSystem.class).setProcessing(false);
            getEngine().getSystem(ControllerSystem.class).setProcessing(false);
        }

        //END_GAME -> Displays necessary game data when a player has won the game
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.END_GAME)) {
            // Rendering of END_GAME entities
            EntityManager.statistics.add(new RenderComponent());
            EntityManager.restartButton.add(new RenderComponent());
            EntityManager.exitButton.add(new RenderComponent());

            // Remove rendering of entities
            EntityManager.player1.remove(RenderComponent.class);
            EntityManager.player2.remove(RenderComponent.class);
            EntityManager.health1.remove(RenderComponent.class);
            EntityManager.health2.remove(RenderComponent.class);
            EntityManager.ground.remove(RenderComponent.class);
            EntityManager.timer.remove(RenderComponent.class);
            EntityManager.powerBar.remove(RenderComponent.class);
            EntityManager.powerBarArrow.remove(RenderComponent.class);
            EntityManager.aimArrow.remove(RenderComponent.class);
            EntityManager.powerBar.remove(RenderComponent.class);
            EntityManager.powerBarArrow.remove(RenderComponent.class);
        }

        // START_ROUND -> Player can move & countdown
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.START_ROUND)) {
            // Remove or add components to entities
            players.get(GSM.currentPlayer).add(new MovementControlComponent());
            controllers.first().add(new RenderComponent()); // TODO: Create EntityListener

            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(ControllerSystem.class).setProcessing(true);
        }

        // PLAYER_AIM -> Player can aim projectile
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PLAYER_AIM)) {
            // Remove or add components to entities
            players.get(GSM.currentPlayer).remove(MovementControlComponent.class); // The player should loose ability to move whilst aiming
            controllers.first().remove(RenderComponent.class); // TODO: Create EntityListener
            EntityManager.aimArrow.add(new RenderComponent()); // Render the aim arrow

            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(AimingSystem.class).setProcessing(true);
            getEngine().getSystem(ControllerSystem.class).setProcessing(false);
        }

        // PLAYER_SHOOT -> Player can choose shot power & shoot projectile
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PLAYER_SHOOT)) {
            // Remove or add components to entities
            EntityManager.powerBar.add(new RenderComponent()); // Render power bar
            EntityManager.powerBarArrow.add(new RenderComponent()); // Render power bar arrow

            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(ShootingSystem.class).setProcessing(true);
            getEngine().getSystem(AimingSystem.class).setProcessing(false);
        }

        // PLAYER_SHOOT -> Player can choose shot power & shoot projectile
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PROJECTILE_AIRBORNE)) {
            // Remove or add components to entities
            EntityManager.aimArrow.remove(RenderComponent.class);
            EntityManager.powerBar.remove(RenderComponent.class);
            EntityManager.powerBarArrow.remove(RenderComponent.class);

            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(ShootingSystem.class).setProcessing(false);
        }
    }

    // Check if any player healths are under 0
    private void checkHealth() {
        for (int i = 0; i < players.size(); i++) {
            Entity player = players.get(i);
            HealthComponent playerHealth = hm.get(player);

            if (playerHealth.hp <= 0) {
                GSM.setGameState(GameStateManager.STATE.END_GAME);
            }
        }
    }
}