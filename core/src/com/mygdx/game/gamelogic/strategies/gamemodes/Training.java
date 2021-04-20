package com.mygdx.game.gamelogic.strategies.gamemodes;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.flags.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.flags.isAimingComponent;
import com.mygdx.game.ECS.components.flags.isShootingComponent;
import com.mygdx.game.ECS.systems.AimingSystem;
import com.mygdx.game.ECS.systems.MovementSystem;
import com.mygdx.game.ECS.systems.ShootingSystem;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.managers.ScreenManager;

import java.text.DecimalFormat;

import static com.mygdx.game.Application._FBIC;
import static com.mygdx.game.managers.ControlManager.CM;
import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.managers.ScreenManager.SM;
import static com.mygdx.game.utils.B2DConstants.PPM;

/**
 * This class controls the game logic of a training game
 * This class also spawns the entities required for a training game
 **/
public class Training implements GameMode {
    private final DecimalFormat df = new DecimalFormat("0"); // Format timer that displays on the time on the screen
    float timer = 0; // The timer
    int score = 0; // This will increase when you hit the target
    float trainingLength = 60; // The length of training (in seconds)
    float moveTargetTime = 15; // Move the target every 10 seconds
    float moveTargetCountDown = moveTargetTime; // Count down and move target when reaching zero

    String mapFile = "basic.tmx";
    String mapTexture = "basic.png";

    Entity player;
    Entity scoreFont;
    Entity target;

    public Training() {
    }

    @Override
    public void update(float dt) {
        updateUI(); // Update UI elements
        updateScore(); // Check if player hits the target

        // Check if aim button is pressed
        if (CM.aimPressed)
            GSM.setGameState(GameStateManager.STATE.PLAYER_AIMING);

        moveTargetCountDown -= dt;
        if (moveTargetCountDown <= 0) {
            moveTargetCountDown = moveTargetTime;
            moveTarget();
        }

        timer += dt; // Increase the timer
        if (timer >= trainingLength) // End game when timer runs out
            GSM.setGameState(GameStateManager.STATE.END_GAME);

    }

    @Override
    public void startGame() { // Is called when the game starts
        CM.startTraining(); // Enable moving buttons (includes aiming button)
        CM.setTouchable(true);
        setPlayerSpawn(); // Choose location for where players spawn

        // Start or stop systems (if they should be processed or not)
        EM.engine.getSystem(ShootingSystem.class).setProcessing(false);
        EM.engine.getSystem(MovementSystem.class).setProcessing(false);
        EM.engine.getSystem(AimingSystem.class).setProcessing(false);
    }

    @Override
    public void endGame() { // Is called when the game ends
        // Remove entities
        EM.removeAllEntities();

        _FBIC.SetHighScore(score);
        timer = 0;
        score = 0;

        SM.setScreen(ScreenManager.STATE.LEADERBOARD); // Display the end screen
    }

    @Override
    public void startRound() { // Is called when a new round starts
        CM.startTraining(); // Enable moving buttons (includes aiming button)
    }

    @Override
    public void playerAim() { // Is called when the player should start aiming
        CM.startShooting(); // Enable shooting button

        // Remove or add components to entities
        player.add(new isAimingComponent());
        EM.addShootingRender();

        // Start or stop systems (if they should be processed or not)
        EM.engine.getSystem(AimingSystem.class).setProcessing(true);
    }

    @Override
    public void playerShooting() { // Is called when the player should start shooting
        // Remove or add components to entities
        player.remove(isAimingComponent.class);
        player.add(new isShootingComponent());

        // Start or stop systems (if they should be processed or not)
        EM.engine.getSystem(ShootingSystem.class).setProcessing(true);
        EM.engine.getSystem(AimingSystem.class).setProcessing(false);
    }

    @Override
    public void projectileAirborne() { // Is called when a projectile has been fired and is currently airborne
        // Remove or add components to entities
        player.remove(isShootingComponent.class);
        EM.removeShootingRender();

        // Start or stop systems (if they should be processed or not)
        EM.engine.getSystem(ShootingSystem.class).setProcessing(false);

        GSM.setGameState(GameStateManager.STATE.START_ROUND); // Change state
    }

    @Override
    public void switchRound() { // Is called when the game is switching between round
    }

    @Override
    public void updateUI() { // Is called every update
        this.printTimer(); // Print information about how much time is left
        this.printScore(); // Prints the score

        EM.updatePowerBar(player); // Makes the powerbar display correctly
    }

    @Override
    public void initEntities() {
        EM.createMap(mapFile, mapTexture);
        EM.spawnPlayers(1); // Spawn players

        player = EM.engine.getEntitiesFor(Family.one(PlayerComponent.class).get()).first(); // Init the player variable

        scoreFont = EM.entityCreator.getTextFont().createEntity(); // Init the score font variable
        EM.positionMapper.get(scoreFont).position = new Vector2(Application.camera.viewportWidth / 2f,
                Application.camera.viewportHeight * 0.93f); // Set position of score font

        target = EM.spawnTarget(); // Init the target entity
    }

    // Print information about how much time is left in a round, etc...
    private void printTimer() {
        FontComponent timerFont = EM.fontMapper.get(EM.timer);
        timerFont.text = "Timer: " + this.df.format(trainingLength - timer) + "s";
        timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
    }

    // Print information about how much time is left in a round, etc...
    private void printScore() {
        FontComponent scoreFont = EM.fontMapper.get(this.scoreFont);
        scoreFont.text = "Score: " + this.df.format(this.score);
        scoreFont.layout = new GlyphLayout(scoreFont.font, scoreFont.text);
    }

    // Set the spawnpoint of players
    private void setPlayerSpawn() {
        PositionComponent pos = EM.positionMapper.get(player);
        SpriteComponent sprite = EM.spriteMapper.get(player);

        // Spawn players with equal distance between them
        EM.b2dMapper.get(player).body.setTransform((sprite.size.x) / PPM, pos.position.y / PPM, 0);
    }

    // Update the score
    private void updateScore() {
        // If a projectile collides with the target -> update score equal to projectile damage
        if (EM.collisionMapper.has(target)) {
            Entity collisionEntity = EM.collisionMapper.get(target).collisionEntity; // Get the colliding entity
            if (EM.projectileMapper.has(collisionEntity)) {
                score += EM.projectileMapper.get(collisionEntity).damage;
            }
        }
    }

    // Move the target
    private void moveTarget() {
        PositionComponent pos = EM.positionMapper.get(target);
        // Move the target to a random position within posFromEdge boundary
        float posFromEdge = (float) Math.floor(Math.random() * Application.VIRTUAL_WORLD_WIDTH / 2);
        EM.b2dMapper.get(target).body.setTransform((Application.VIRTUAL_WORLD_WIDTH - posFromEdge) / PPM, pos.position.y / PPM, 0);
    }
}
