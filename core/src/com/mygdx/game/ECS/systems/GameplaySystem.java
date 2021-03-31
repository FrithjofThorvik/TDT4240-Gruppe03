package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.HealthComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.RenderComponent;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.managers.ScreenManager;

import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.managers.ScreenManager.SM;


/**
 * This system is responsible for removing and adding components between rounds
 * This system is also responsible for stopping and starting systems between rounds
 **/
public class GameplaySystem extends EntitySystem {
    // Entity arrays
    public ImmutableArray<Entity> players; // List of players

    // Prepare component mappers
    private final ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);
    private final ComponentMapper<FontComponent> fm = ComponentMapper.getFor(FontComponent.class);

    // Get entities
    public void addedToEngine(Engine e) {
        this.players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    // Update function for GameplaySystem (calls automatically by engine)
    public void update(float dt) {
        if (this.players.size() > 0) {
            GSM.numberOfPlayers = this.players.size(); // Tell the state manager how many players are in the game

            this.checkHealth(); // Check if any health components have reached 0
            this.updateStates(); // Check which gameState the system is in -> Remove or add component, start or stop systems
        }
    }

    // Update game states by removing/adding components, starting/stopping systems, etc
    private void updateStates() {
        Entity player = this.players.get(GSM.currentPlayer); // Get current player (the player who's turn it is)

        // SWITCH_ROUND
        if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.SWITCH_ROUND)) {
            // Remove or add components to entities
            EntityManager.powerBar.remove(RenderComponent.class); // Remove render of power bar
            EntityManager.powerBarArrow.remove(RenderComponent.class); // Remove render of power bar arrow
            EntityManager.aimArrow.remove(RenderComponent.class); // Remove render of aim arrow

            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(ShootingSystem.class).setProcessing(false);
            getEngine().getSystem(ControllerSystem.class).setProcessing(false);
        }

        // START_ROUND
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.START_ROUND)) {
            // Remove or add components to entities
            player.add(new MovementControlComponent());

            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(ShootingSystem.class).setProcessing(false);
            getEngine().getSystem(ControllerSystem.class).setProcessing(true);
        }

        // PLAYER_AIM
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PLAYER_AIM)) {
            // Remove or add components to entities
            player.remove(MovementControlComponent.class); // The player should loose ability to move whilst aiming
            EntityManager.aimArrow.add(new RenderComponent()); // Render the aim arrow

            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(AimingSystem.class).setProcessing(true);
            getEngine().getSystem(ShootingSystem.class).setProcessing(false);
            getEngine().getSystem(ControllerSystem.class).setProcessing(false);
        }

        // PLAYER_SHOOT
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PLAYER_SHOOT)) {
            // Remove or add components to entities
            EntityManager.powerBar.add(new RenderComponent()); // Render power bar
            EntityManager.powerBarArrow.add(new RenderComponent()); // Render power bar arrow

            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(ShootingSystem.class).setProcessing(true);
            getEngine().getSystem(AimingSystem.class).setProcessing(false);
            getEngine().getSystem(ControllerSystem.class).setProcessing(false);
        }

        // END_GAME
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.END_GAME)) {
            // TODO: Reset everything
            this.resetPlayers();
            SM.setScreen(ScreenManager.STATE.MAIN_MENU);
            GSM.setGameState(GameStateManager.STATE.SWITCH_ROUND);
        }

        // START_GAME
        // TODO: Implement START_GAME state
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

    // Reset all players (health, etc)
    private void resetPlayers() {
        for (int i = 0; i < this.players.size(); i++) {
            Entity player = players.get(i);
            HealthComponent playerHealth = hm.get(player);
            playerHealth.hp = 100; // TODO: Make dynamic
        }

        // Reset health fonts
        FontComponent healthFont1 = fm.get(EntityManager.health1);
        FontComponent healthFont2 = fm.get(EntityManager.health2);

        healthFont1.text = 100 + " hp";
        healthFont2.text = 100 + " hp";
        healthFont1.layout = new GlyphLayout(healthFont1.font, healthFont1.text);
        healthFont2.layout = new GlyphLayout(healthFont2.font, healthFont2.text);

    }
}