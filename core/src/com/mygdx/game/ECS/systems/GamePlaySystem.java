package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.HealthComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.RenderComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.managers.ScreenManager;

import javax.swing.text.Position;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.managers.ScreenManager.SM;


/**
 * This system is responsible for removing and adding components between rounds
 * This system is also responsible for stopping and starting systems between rounds
 **/
public class GamePlaySystem extends EntitySystem {
    // Entity arrays
    public ImmutableArray<Entity> players; // List of players

    // Prepare component mappers
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);
    private final ComponentMapper<FontComponent> fm = ComponentMapper.getFor(FontComponent.class);
    private final ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);
    private final ComponentMapper<ShootingComponent> shm = ComponentMapper.getFor(ShootingComponent.class);
    private final ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);

    // Get entities
    public void addedToEngine(Engine e) {
        this.players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    // Update function for GamePlaySystem (calls automatically by engine)
    public void update(float dt) {
        if (this.players.size() > 0) {
            if (GSM.numberOfPlayers == 0) GSM.numberOfPlayers = this.players.size(); // Tell the state manager how many players are in the game

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

        // END_GAME -> Displays necessary game data when a player has won the game
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

            // Activate processing of systems
            getEngine().getSystem(UISystem.class).setProcessing(true);
            getEngine().getSystem(PhysicsSystem.class).setProcessing(true);
            getEngine().getSystem(GamePlaySystem.class).setProcessing(true);

            // Deactivate processing of systems
            getEngine().getSystem(AimingSystem.class).setProcessing(false);
            getEngine().getSystem(ControllerSystem.class).setProcessing(false);
            getEngine().getSystem(ShootingSystem.class).setProcessing(false);

            this.resetPlayers(); // Reset all players

            // Handle input
            if (Gdx.input.justTouched()) {
                int x = Gdx.input.getX();
                int y = Gdx.graphics.getHeight() - Gdx.input.getY(); // Input y is inverted compared to position y

                PositionComponent exitButtonPosition = pm.get(EntityManager.exitButton);
                FontComponent exitButtonFont = fm.get(EntityManager.exitButton);

                PositionComponent restartButtonPosition = pm.get(EntityManager.restartButton);
                FontComponent restartButtonFont = fm.get(EntityManager.restartButton);

                // Check if exit button is pressed
                if (
                        (int) exitButtonPosition.position.x - (int) exitButtonFont.layout.width / 1f <= x &&
                        (int) exitButtonPosition.position.x + (int) exitButtonFont.layout.width / 1f >= x &&
                        (int) exitButtonPosition.position.y - (int) exitButtonFont.layout.height / 1f <= y &&
                        (int) exitButtonPosition.position.y + (int) exitButtonFont.layout.height / 1f >= y
                ) {
                    System.out.println("Exiting to main menu...");
                    GSM.setGameState(GameStateManager.STATE.EXIT_GAME);
                }

                // Check if restart button is pressed
                else if (
                        (int) restartButtonPosition.position.x - (int) restartButtonFont.layout.width / 1f <= x &&
                        (int) restartButtonPosition.position.x + (int) restartButtonFont.layout.width / 1f >= x &&
                        (int) restartButtonPosition.position.y - (int) restartButtonFont.layout.height / 1f <= y &&
                        (int) restartButtonPosition.position.y + (int) restartButtonFont.layout.height / 1f >= y
                ) {
                    System.out.println("Restarting game...");
                    GSM.setGameState(GameStateManager.STATE.RESTART_GAME);
                }

            }
        }

        // RESTART_GAME -> Resets necessary game data, e.g. player health, position etc
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.RESTART_GAME)) {
            // Render of entities
            EntityManager.player1.add(new RenderComponent());
            EntityManager.player2.add(new RenderComponent());
            EntityManager.health1.add(new RenderComponent());
            EntityManager.health2.add(new RenderComponent());
            EntityManager.ground.add(new RenderComponent());
            EntityManager.timer.add(new RenderComponent());

            // Remove render of entities
            EntityManager.statistics.remove(RenderComponent.class);
            EntityManager.restartButton.remove(RenderComponent.class);
            EntityManager.exitButton.remove(RenderComponent.class);

            // Enable entity systems
            getEngine().getSystem(ProjectileSystem.class).setProcessing(true);
            getEngine().getSystem(PowerUpSystem.class).setProcessing(true);

            GSM.setGameState(GameStateManager.STATE.START_GAME); // Start game
        }

        // EXIT_GAME -> Removes everything
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.EXIT_GAME)) {
            EM.removeAll(); // Remove all entities and systems
            SM.setScreen(ScreenManager.STATE.MAIN_MENU); // Change screen to main_menu
        }

        // SWITCH_ROUND -> Switches player & countdown
        /*else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.SWITCH_ROUND)) {
            // Remove or add components to entities
            // Start or stop systems (if they should be processed or not)
        }*/

        // START_ROUND -> Player can move & countdown
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.START_ROUND)) {
            // Remove or add components to entities
            players.get(GSM.currentPlayer).add(new MovementControlComponent());

            // Start or stop systems (if they should be processed or not)
            getEngine().getSystem(ControllerSystem.class).setProcessing(true);
        }

        // PLAYER_AIM -> Player can aim projectile
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PLAYER_AIM)) {
            // Remove or add components to entities
            players.get(GSM.currentPlayer).remove(MovementControlComponent.class); // The player should loose ability to move whilst aiming
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
            shm.get(players.get(GSM.currentPlayer)).power = 0; // Reset current players shooting power

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

    // Reset all players (health, etc)
    private void resetPlayers() {
        for (int i = 0; i < this.players.size(); i++) {
            Entity player = players.get(i);
            HealthComponent playerHealth = hm.get(player);
            PositionComponent playerPosition = pm.get(player);
            Box2DComponent playerBox2D = b2dm.get(player);

            // Reset player health for each player TODO: Make dynamic
            playerHealth.hp = 100;

            // Reset player position component for each player TODO: Make dynamic
            if (i == 0) playerPosition.position.x = sm.get(player).size.x;
            else playerPosition.position.x = Gdx.graphics.getWidth() - sm.get(player).size.x;
            playerPosition.position.y = Gdx.graphics.getHeight() / 1.2f;

            // Reset Box2D component position
            playerBox2D.body.setTransform(playerPosition.position, 0); // Update Box2D body of new position
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