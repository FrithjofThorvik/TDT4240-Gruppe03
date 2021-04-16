package com.mygdx.game.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.HealthComponent;
import com.mygdx.game.ECS.components.HealthDisplayerComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileComponent;
import com.mygdx.game.ECS.components.RenderComponent;
import com.mygdx.game.ECS.components.isAimingComponent;
import com.mygdx.game.ECS.components.isShootingComponent;
import com.mygdx.game.ECS.systems.AimingSystem;
import com.mygdx.game.ECS.systems.MovementSystem;
import com.mygdx.game.ECS.systems.ShootingSystem;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.managers.ScreenManager;

import java.text.DecimalFormat;

import static com.mygdx.game.managers.ControlManager.CM;
import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.managers.ScreenManager.SM;
import static com.mygdx.game.utils.B2DConstants.PPM;
import static com.mygdx.game.utils.GameConstants.MAX_SHOOTING_POWER;
import static com.mygdx.game.utils.GameConstants.ROUND_TIME;
import static com.mygdx.game.utils.GameConstants.START_GAME_TIME;
import static com.mygdx.game.utils.GameConstants.TIME_BETWEEN_ROUNDS;

/**
 * This class controls the game logic of a local multiplayer game
 * This class also spawns the entities required for a local multiplayer game
 **/
public class LocalMultiplayer implements GameMode {
    private final DecimalFormat df = new DecimalFormat("0.0"); // Format timer that displays on the time on the screen
    int currentPlayer = 0; // The player whose turn it is
    boolean stopTimer = false; // If the timer should increment with time
    float timer = 0; // The timer
    float switchTime = 0; // The time used to switch between rounds -> is updated in some states
    float airBorneTime = 0; // How long has projectiles been airborne
    float timeoutTime = 10; // If projectiles are airborne longer than this -> switch round
    public ImmutableArray<Entity> players; // List of players
    public ImmutableArray<Entity> healthDisplayers; // List of players
    private ImmutableArray<Entity> projectiles; // List of projectiles

    public LocalMultiplayer() {
        this.players = EM.engine.getEntitiesFor(Family.one(PlayerComponent.class).get());
        this.projectiles = EM.engine.getEntitiesFor(Family.one(ProjectileComponent.class).get());
        this.healthDisplayers = EM.engine.getEntitiesFor(Family.one(HealthDisplayerComponent.class).get());
    }

    @Override
    public void update(float dt) {
        updateUI(); // Update UI elements
        // Check if aim button is pressed
        if (CM.aimPressed)
            GSM.setGameState(GameStateManager.STATE.PLAYER_AIMING);

        //Update arrays
        this.players = EM.engine.getEntitiesFor(Family.one(PlayerComponent.class).get());
        this.healthDisplayers = EM.engine.getEntitiesFor(Family.one(HealthDisplayerComponent.class).get());
        this.projectiles = EM.engine.getEntitiesFor(Family.one(ProjectileComponent.class).get());

        if (this.players.size() > 0) {
            if (players.size() == 1) // End the game when there is only one player left
                GSM.setGameState(GameStateManager.STATE.END_GAME);
            this.checkHealth(); // Check if any health components have reached 0, and terminate those players
        }

        if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PROJECTILE_AIRBORNE))
            checkProjectileTimeOut(dt); // Check if the projectiles have been airborne

        if (!stopTimer)
            timer += dt;

        if (timer > switchTime) // Start a new round when the timer is greater than the switchTime variable
            GSM.setGameState(GameStateManager.STATE.START_ROUND);
    }

    @Override
    public void startGame() { // Is called when the game starts
        setPlayerSpawn(); // Choose location for where players spawn
        switchTime = START_GAME_TIME; // The timer now countsdown from START_GAME_TIME to 0

        // Start or stop systems (if they should be processed or not)
        EM.engine.getSystem(ShootingSystem.class).setProcessing(false);
        EM.engine.getSystem(MovementSystem.class).setProcessing(false);
        EM.engine.getSystem(AimingSystem.class).setProcessing(false);
    }

    @Override
    public void endGame() { // Is called when the game ends
        // Remove rendering of entities
        for (int i = 0; i < players.size(); i++) {
            players.get(i).remove(RenderComponent.class);
        }
        for (int i = 0; i < healthDisplayers.size(); i++) {
            healthDisplayers.get(i).remove(RenderComponent.class);
        }

        // Remove rendering of UIentities
        EM.timer.remove(RenderComponent.class);
        EM.powerBar.remove(RenderComponent.class);
        EM.powerBarArrow.remove(RenderComponent.class);
        EM.aimArrow.remove(RenderComponent.class);
        EM.powerBar.remove(RenderComponent.class);
        EM.powerBarArrow.remove(RenderComponent.class);

        SM.setScreen(ScreenManager.STATE.END_SCREEN); // Display the end screen
    }

    @Override
    public void startRound() { // Is called when a new round starts
        CM.startMoving(); // Enable moving buttons

        timer = 0; // Reset the timer
        switchTime = ROUND_TIME; // Set the timer to the length of a round

        // Remove or add components to entities
        players.get(currentPlayer).add(new MovementControlComponent());

        // Start or stop systems (if they should be processed or not)
        EM.engine.getSystem(MovementSystem.class).setProcessing(true);
    }

    @Override
    public void playerAim() { // Is called when the player should start aiming
        CM.startShooting(); // Enable shooting button

        // Remove or add components to entities
        players.get(currentPlayer).remove(MovementControlComponent.class); // The player should loose ability to move whilst aiming
        players.get(currentPlayer).add(new isAimingComponent());
        EM.powerBar.add(new RenderComponent()); // Render power bar
        EM.powerBarArrow.add(new RenderComponent()); // Render power bar arrow
        EM.aimArrow.add(new RenderComponent()); // Render the aim arrow

        // Start or stop systems (if they should be processed or not)
        EM.engine.getSystem(AimingSystem.class).setProcessing(true);
        EM.engine.getSystem(MovementSystem.class).setProcessing(false);
    }

    @Override
    public void playerShooting() { // Is called when the player should start shooting
        stopTimer = true; // Stop the timer from counting down

        // Remove or add components to entities
        players.get(currentPlayer).remove(isAimingComponent.class);
        players.get(currentPlayer).add(new isShootingComponent());

        // Start or stop systems (if they should be processed or not)
        EM.engine.getSystem(ShootingSystem.class).setProcessing(true);
        EM.engine.getSystem(AimingSystem.class).setProcessing(false);
    }

    @Override
    public void projectileAirborne() { // Is called when a projectile has been fired and is currently airborne
        CM.idle(); // Make all controller buttons idle

        // Remove or add components to entities
        players.get(currentPlayer).remove(isShootingComponent.class);
        EM.aimArrow.remove(RenderComponent.class);
        EM.powerBar.remove(RenderComponent.class);
        EM.powerBarArrow.remove(RenderComponent.class);

        // Start or stop systems (if they should be processed or not)
        EM.engine.getSystem(ShootingSystem.class).setProcessing(false);
    }

    @Override
    public void switchRound() { // Is called when the game is switching between round
        timer = 0; // Reset the timer
        stopTimer = false; // Start the timer again
        switchTime = TIME_BETWEEN_ROUNDS; // Set the switchTime to how long the round switch should take
        currentPlayer++; // Update current player to next player

        // Reset player counter if max limit reached
        if (currentPlayer >= players.size())
            currentPlayer = 0;
    }

    @Override
    public void updateUI() { // Is called every update
        this.printTimer(); // Print information about how much time is left in a round, etc...

        // Get the player who's turn it is and get its position component
        Entity player = this.players.get(currentPlayer);
        PositionComponent position = EM.positionMapper.get(player);

        // Calculate the startingPosition of an arrow (this is done here so that if the screen is resized the arrowPosition is updated)
        float startPositionArrow = EM.positionMapper.get(EM.powerBar).position.y - EM.spriteMapper.get(EM.powerBar).size.y / 2;

        // Get the angle (in degrees) and power of the currentPlayer's shootingComponent
        double aimAngleInDegrees = 90f - (float) EM.shootingMapper.get(player).angle / (float) Math.PI * 180f;
        float power = EM.shootingMapper.get(player).power;

        // Set rotation and position of AimArrow (displayed above the player -> rotated by where the player aims)
        EM.spriteMapper.get(EM.aimArrow).sprite.setRotation((float) aimAngleInDegrees);
        EM.positionMapper.get(EM.aimArrow).position.x = position.position.x;
        EM.positionMapper.get(EM.aimArrow).position.y = position.position.y + 25;

        //Set position of powerBarArrow -> given the power of the shootingComponent
        EM.positionMapper.get(EM.powerBarArrow).position.y = startPositionArrow + (EM.spriteMapper.get(EM.powerBar).size.y * (power / MAX_SHOOTING_POWER));

        // Update health displays
        // Update health displays
        for (int i = 0; i < healthDisplayers.size(); i++) {
            Entity entity = healthDisplayers.get(i);
            Entity parent = EM.parentMapper.get(entity).parent;
            EM.positionMapper.get(entity).position.x = EM.positionMapper.get(parent).position.x;
            EM.positionMapper.get(entity).position.y = EM.positionMapper.get(parent).position.y + EM.spriteMapper.get(parent).size.y;
            EM.fontMapper.get(entity).text = EM.healthMapper.get(parent).hp + " hp";
        }
    }

    @Override
    public void initEntities() {
        EM.spawnPlayers(5);
        EM.createHealthDisplayers();
        EM.createUIEntities();
    }

    // Print information about how much time is left in a round, etc...
    private void printTimer() {
        // SWITCH_ROUND
        if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.SWITCH_ROUND)) {
            FontComponent timerFont = EM.fontMapper.get(EM.timer);
            timerFont.text = "Switching players in: " + this.df.format(TIME_BETWEEN_ROUNDS - timer) + "s";
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        }

        // START_GAME
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.START_GAME)) {
            FontComponent timerFont = EM.fontMapper.get(EM.timer);
            timerFont.text = "\n\n\n\n" + ((int) START_GAME_TIME - (int) timer);
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        }

        // OTHER
        else {
            FontComponent timerFont = EM.fontMapper.get(EM.timer);
            timerFont.text = "Timer: " + this.df.format(ROUND_TIME - timer) + "s";
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        }
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
        this.players = EM.engine.getEntitiesFor(Family.one(PlayerComponent.class).get()); // Update the player array

        // Reset player counter if max limit reached
        if (currentPlayer >= players.size())
            currentPlayer = 0; // Decrease number of players variable
    }

    // Set the spawnpoint of players
    private void setPlayerSpawn() {
        for (int i = 0; i < players.size(); i++) {
            Entity player = players.get(i);
            PositionComponent pos = EM.positionMapper.get(player);

            // Spawn players with equal distance between them
            EM.b2dMapper.get(player).body.setTransform((50f + (Application.camera.viewportWidth / players.size()) * i) / PPM, pos.position.y / PPM, 0);
        }
    }

    // Checks if the game state should switch from ProjectileAirborne to a new round
    private void checkProjectileTimeOut(float dt) {
        // Check if there are no projectiles -> move on to SWITCH_ROUND state
        if (projectiles.size() <= 0) {
            if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PROJECTILE_AIRBORNE)) {
                GSM.setGameState(GameStateManager.STATE.SWITCH_ROUND);
            }
        }

        // Change gamestate if the projectile has been airborne too long
        if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PROJECTILE_AIRBORNE)) {
            airBorneTime += dt;
            if (airBorneTime > timeoutTime) {
                airBorneTime = 0;
                GSM.setGameState(GameStateManager.STATE.SWITCH_ROUND);
            }
        }
    }
}
