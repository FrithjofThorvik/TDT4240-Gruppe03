package com.mygdx.game.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
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

public class LocalMultiplayer implements Mode {
    private final DecimalFormat df = new DecimalFormat("0.0"); // Format timer that displays on the time on the screen
    int currentPlayer=0;
    boolean stopTimer = false;
    float time = 0;
    float switchTime = 500;
    float airBorneTime = 0;
    float timeoutTime = 10;
    public ImmutableArray<Entity> players; // List of players
    public ImmutableArray<Entity> healthDisplayers; // List of players
    private ImmutableArray<Entity> projectiles;
    private EntityListener playerComponentListener;

    public LocalMultiplayer() {
        this.players = EM.engine.getEntitiesFor(Family.one(PlayerComponent.class).get());
        this.projectiles = EM.engine.getEntitiesFor(Family.one(ProjectileComponent.class).get());
        this.healthDisplayers = EM.engine.getEntitiesFor(Family.one(HealthDisplayerComponent.class).get());
        // This should activate when a player component is added or removed from an entity
        this.playerComponentListener = new EntityListener() {
            @Override
            public void entityRemoved(Entity entity) {
                // Reset player counter if max limit reached
                if (currentPlayer >= players.size())
                    currentPlayer = 0; // Decrease number of players variable
            }

            @Override
            public void entityAdded(Entity entity) {
                //
            }
        };

        // The family decides which components the entity listener should listen for
        Family players = Family.all(PlayerComponent.class).get();
        EM.engine.addEntityListener(players, playerComponentListener);
    }

    @Override
    public void update(float dt) {
        this.players = EM.engine.getEntitiesFor(Family.one(PlayerComponent.class).get());
        this.healthDisplayers = EM.engine.getEntitiesFor(Family.one(HealthDisplayerComponent.class).get());
        this.projectiles = EM.engine.getEntitiesFor(Family.one(ProjectileComponent.class).get());
        if (this.players.size() > 0) {
            if (players.size() == 1)
                GSM.setGameState(GameStateManager.STATE.END_GAME);
            this.checkHealth(); // Check if any health components have reached 0
        }
        updateUI();
        checkProjectiles(dt);
        if (!stopTimer)
            time += dt;
        if (time > switchTime)
            GSM.setGameState(GameStateManager.STATE.START_ROUND);
    }

    @Override
    public void startGame() {
        setPlayerSpawn();
        switchTime = START_GAME_TIME;
        // Start or stop systems (if they should be processed or not)
        EM.engine.getSystem(ShootingSystem.class).setProcessing(false);
        EM.engine.getSystem(MovementSystem.class).setProcessing(false);
        EM.engine.getSystem(AimingSystem.class).setProcessing(false);
    }

    @Override
    public void endGame() {
        SM.setScreen(ScreenManager.STATE.END_SCREEN); // Display the end screen
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

    @Override
    public void startRound() {
        CM.startMoving(); // Enable moving buttons
        time = 0;
        switchTime = ROUND_TIME;
        // Remove or add components to entities
        players.get(currentPlayer).add(new MovementControlComponent());

        // Start or stop systems (if they should be processed or not)
        EM.engine.getSystem(MovementSystem.class).setProcessing(true);
    }

    @Override
    public void playerAim() {
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
    public void playerShooting() {
        stopTimer = true;
        players.get(currentPlayer).remove(isAimingComponent.class);
        players.get(currentPlayer).add(new isShootingComponent());
        // Start or stop systems (if they should be processed or not)
        EM.engine.getSystem(ShootingSystem.class).setProcessing(true);
        EM.engine.getSystem(AimingSystem.class).setProcessing(false);
    }

    @Override
    public void projectileAirborne() {
        players.get(currentPlayer).remove(isShootingComponent.class);
        CM.idle(); // Make all controller buttons idle
        // Remove or add components to entities
        EM.aimArrow.remove(RenderComponent.class);
        EM.powerBar.remove(RenderComponent.class);
        EM.powerBarArrow.remove(RenderComponent.class);

        // Start or stop systems (if they should be processed or not)
        EM.engine.getSystem(ShootingSystem.class).setProcessing(false);
    }

    @Override
    public void switchRound() {
        time = 0;
        switchTime = TIME_BETWEEN_ROUNDS;
        stopTimer = false;
        currentPlayer++; // Update current player to next player

        // Reset player counter if max limit reached
        if (currentPlayer >= players.size())
            currentPlayer = 0;
    }

    @Override
    public void updateUI() {
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

    // Print information about how much time is left in a round, etc...
    private void printTimer() {
        // SWITCH_ROUND
        if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.SWITCH_ROUND)) {
            FontComponent timerFont = EM.fontMapper.get(EM.timer);
            timerFont.text = "Switching players in: " + this.df.format(TIME_BETWEEN_ROUNDS - time) + "s";
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        }

        // START_GAME
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.START_GAME)) {
            FontComponent timerFont = EM.fontMapper.get(EM.timer);
            timerFont.text = "\n\n\n\n" + ((int) START_GAME_TIME - (int) time);
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        }

        // OTHER
        else {
            FontComponent timerFont = EM.fontMapper.get(EM.timer);
            timerFont.text = "Timer: " + this.df.format(ROUND_TIME - time) + "s";
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

    private void checkProjectiles(float dt) {
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
