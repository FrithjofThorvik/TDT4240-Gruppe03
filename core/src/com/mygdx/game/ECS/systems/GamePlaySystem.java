package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.components.HealthComponent;
import com.mygdx.game.ECS.components.HealthDisplayerComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.ParentComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.RenderComponent;
import com.mygdx.game.ECS.entities.EntityCreator;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.ECS.components.PlayerComponent;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.utils.B2DConstants.PPM;


/**
 * This system is responsible for removing and adding components between rounds
 * This system is also responsible for stopping and starting systems between rounds
 **/
public class GamePlaySystem extends EntitySystem {
    // Entity arrays
    public ImmutableArray<Entity> players; // List of players
    public ImmutableArray<Entity> healthDisplayers; // List of players

    // Get entities
    public void addedToEngine(Engine e) {
        this.players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
        this.healthDisplayers = e.getEntitiesFor(Family.all(HealthDisplayerComponent.class).get());
    }

    // Update function for GamePlaySystem (calls automatically by engine)
    public void update(float dt) {
        if (this.players.size() > 0) {
            if(players.size()==1)
                GSM.setGameState(GameStateManager.STATE.END_GAME);
            this.checkHealth(); // Check if any health components have reached 0
            if(GSM.updateGamePlaySystem)
                this.updateStates(); // Check which gameState the system is in -> Remove or add component, start or stop systems
        }
    }

    // Update game states by removing/adding components, starting/stopping systems, etc
    private void updateStates() {

        // START_GAME -> Displays a countdown until round starts
        if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.START_GAME)) {
            setPlayerSpawn();
            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(ShootingSystem.class).setProcessing(false);
            getEngine().getSystem(MovementSystem.class).setProcessing(false);
            getEngine().getSystem(AimingSystem.class).setProcessing(false);
        }

        //END_GAME -> Displays necessary game data when a player has won the game
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.END_GAME)) {
            // Remove rendering of entities
            // Remove rendering of entities
            for (int i = 0; i < players.size(); i++) {
                players.get(i).remove(RenderComponent.class);
            }
            for (int i = 0; i < healthDisplayers.size(); i++) {
                healthDisplayers.get(i).remove(RenderComponent.class);
            }

            EM.timer.remove(RenderComponent.class);
            EM.powerBar.remove(RenderComponent.class);
            EM.powerBarArrow.remove(RenderComponent.class);
            EM.aimArrow.remove(RenderComponent.class);
            EM.powerBar.remove(RenderComponent.class);
            EM.powerBarArrow.remove(RenderComponent.class);
        }

        // START_ROUND -> Player can move & countdown
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.START_ROUND)) {
            // Remove or add components to entities
            players.get(GSM.currentPlayer).add(new MovementControlComponent());

            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(MovementSystem.class).setProcessing(true);
        }

        // PLAYER_AIMING -> Player can aim projectile
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PLAYER_AIMING)) {
            // Remove or add components to entities
            players.get(GSM.currentPlayer).remove(MovementControlComponent.class); // The player should loose ability to move whilst aiming
            EM.powerBar.add(new RenderComponent()); // Render power bar
            EM.powerBarArrow.add(new RenderComponent()); // Render power bar arrow
            EM.aimArrow.add(new RenderComponent()); // Render the aim arrow

            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(AimingSystem.class).setProcessing(true);
            getEngine().getSystem(MovementSystem.class).setProcessing(false);
        }

        // PLAYER_SHOOTING -> Player can shoot projectile
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PLAYER_SHOOTING)) {
            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(ShootingSystem.class).setProcessing(true);
            getEngine().getSystem(AimingSystem.class).setProcessing(false);
        }

        // PROJECTILE_AIRBORNE -> Player can choose shot power & shoot projectile
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PROJECTILE_AIRBORNE)) {
            // Remove or add components to entities
            EM.aimArrow.remove(RenderComponent.class);
            EM.powerBar.remove(RenderComponent.class);
            EM.powerBarArrow.remove(RenderComponent.class);

            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(ShootingSystem.class).setProcessing(false);
        }

        // SWITCH_ROUND -> Switching players
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.SWITCH_ROUND)) {
            // Remove or add components to entities
            players.get(GSM.currentPlayer).remove(MovementControlComponent.class); // The player should loose ability to move
        }

        GSM.updateGamePlaySystem = false; // So it only runs the update once
    }

    // Check if any player healths are under 0
    private void checkHealth() {
        for (int i = 0; i < players.size(); i++) {
            Entity player = players.get(i);
            HealthComponent playerHealth = EM.healthMapper.get(player);

            // If player health < 0 -> delete the player and its associate health displayer
            if (playerHealth.hp <= 0) {
                for (int j = 0; j < healthDisplayers.size(); j++) {
                    Entity healthDisplayer = healthDisplayers.get(j);
                    if (EM.parentMapper.get(healthDisplayer).parent == player)
                        healthDisplayer.removeAll();
                }
                player.removeAll();
            }
        }
    }

    // Set the spawnpoint of players
    private void setPlayerSpawn(){
        for (int i = 0; i < players.size(); i++) {
            Entity player = players.get(i);
            PositionComponent pos = EM.positionMapper.get(player);

            // Spawn players with equal distance between them
            EM.b2dMapper.get(player).body.setTransform((50f + (Application.camera.viewportWidth / players.size()) * i)/PPM,pos.position.y/PPM,0);
        }
    }
}