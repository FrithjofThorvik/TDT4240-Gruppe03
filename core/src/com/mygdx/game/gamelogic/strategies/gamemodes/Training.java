package com.mygdx.game.gamelogic.strategies.gamemodes;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.components.misc.FontComponent;
import com.mygdx.game.ECS.components.flags.PlayerComponent;
import com.mygdx.game.ECS.components.misc.PositionComponent;
import com.mygdx.game.ECS.components.misc.SpriteComponent;
import com.mygdx.game.ECS.components.flags.isAimingComponent;
import com.mygdx.game.ECS.components.flags.isShootingComponent;
import com.mygdx.game.ECS.managers.ECSManager;
import com.mygdx.game.ECS.systems.AimingSystem;
import com.mygdx.game.ECS.systems.MovementSystem;
import com.mygdx.game.ECS.systems.PowerUpSystem;
import com.mygdx.game.ECS.systems.ShootingSystem;
import com.mygdx.game.gamelogic.states.GameStateManager;
import com.mygdx.game.gamelogic.states.ScreenManager;
import com.mygdx.game.utils.GameConstants;
import com.mygdx.game.utils.GameController;

import java.text.DecimalFormat;

import static com.mygdx.game.Application._FBIC;

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
        if (GameController.getInstance().aimPressed)
            GameStateManager.getInstance().setGameState(GameStateManager.STATE.PLAYER_AIMING);

        moveTargetCountDown -= dt;
        if (moveTargetCountDown <= 0) {
            moveTargetCountDown = moveTargetTime;
            moveTarget();
        }

        timer += dt; // Increase the timer
        if (timer >= trainingLength) // End game when timer runs out
            GameStateManager.getInstance().setGameState(GameStateManager.STATE.END_GAME);

    }

    @Override
    public void startGame() { // Is called when the game starts
        ECSManager.getInstance().UIManager.createUIEntities();

        GameController.getInstance().startTraining(); // Enable moving buttons (includes aiming button)
        GameController.getInstance().setTouchable(true);
        setPlayerSpawn(); // Choose location for where players spawn

        // Start or stop systems (if they should be processed or not)
            ECSManager.getInstance().getEngine().getSystem(ShootingSystem.class).setProcessing(false);
            ECSManager.getInstance().getEngine().getSystem(MovementSystem.class).setProcessing(false);
            ECSManager.getInstance().getEngine().getSystem(AimingSystem.class).setProcessing(false);
            ECSManager.getInstance().getEngine().getSystem(PowerUpSystem.class).setProcessing(false);
    }

    @Override
    public void endGame() { // Is called when the game ends
        // Remove entities
        ECSManager.getInstance().removeAllEntities();

        _FBIC.SetHighScore(score);
        timer = 0;
        score = 0;

        ScreenManager.getInstance(null).setScreen(ScreenManager.STATE.LEADERBOARD); // Display the end screen
    }

    @Override
    public void startRound() { // Is called when a new round starts
        GameController.getInstance().startTraining(); // Enable moving buttons (includes aiming button)
    }

    @Override
    public void playerAim() { // Is called when the player should start aiming
        GameController.getInstance().startShooting(); // Enable shooting button

        // Remove or add components to entities
        player.add(new isAimingComponent());

        // Start or stop systems (if they should be processed or not)
             ECSManager.getInstance().getEngine().getSystem(AimingSystem.class).setProcessing(true);
    }

    @Override
    public void playerShooting() { // Is called when the player should start shooting
        // Remove or add components to entities
        player.remove(isAimingComponent.class);
        player.add(new isShootingComponent());

        // Start or stop systems (if they should be processed or not)
             ECSManager.getInstance().getEngine().getSystem(ShootingSystem.class).setProcessing(true);
             ECSManager.getInstance().getEngine().getSystem(AimingSystem.class).setProcessing(false);
    }

    @Override
    public void projectileAirborne() { // Is called when a projectile has been fired and is currently airborne
        // Remove or add components to entities
        player.remove(isShootingComponent.class);

        // Start or stop systems (if they should be processed or not)
             ECSManager.getInstance().getEngine().getSystem(ShootingSystem.class).setProcessing(false);

        GameStateManager.getInstance().setGameState(GameStateManager.STATE.START_ROUND); // Change state
    }

    @Override
    public void switchRound() { // Is called when the game is switching between round
    }

    @Override
    public void updateUI() { // Is called every update
        this.printTimer(); // Print information about how much time is left
        this.printScore(); // Prints the score

    }

    @Override
    public void initEntities() {
             ECSManager.getInstance().mapManager.createMap(mapFile, mapTexture);
             ECSManager.getInstance().gameEntityManager.spawnPlayers(1); // Spawn players

        player =      ECSManager.getInstance().getEngine().getEntitiesFor(Family.one(PlayerComponent.class).get()).first(); // Init the player variable

        scoreFont =      ECSManager.getInstance().getEntityTemplateMapper().getTextFont().createEntity(); // Init the score font variable
             ECSManager.getInstance().positionMapper.get(scoreFont).position = new Vector2(Application.camera.viewportWidth / 2f,
                Application.camera.viewportHeight * 0.93f); // Set position of score font

        target =      ECSManager.getInstance().gameEntityManager.spawnTarget(); // Init the target entity
    }

    // Print information about how much time is left in a round, etc...
    private void printTimer() {
        FontComponent timerFont =      ECSManager.getInstance().fontMapper.get(     ECSManager.getInstance().UIManager.getTimer());
        timerFont.text = "Timer: " + this.df.format(trainingLength - timer) + "s";
        timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
    }

    // Print information about how much time is left in a round, etc...
    private void printScore() {
        FontComponent scoreFont =      ECSManager.getInstance().fontMapper.get(this.scoreFont);
        scoreFont.text = "Score: " + this.df.format(this.score);
        scoreFont.layout = new GlyphLayout(scoreFont.font, scoreFont.text);
    }

    // Set the spawnpoint of players
    private void setPlayerSpawn() {
        PositionComponent pos =      ECSManager.getInstance().positionMapper.get(player);
        SpriteComponent sprite =      ECSManager.getInstance().spriteMapper.get(player);

        // Spawn players with equal distance between them
             ECSManager.getInstance().b2dMapper.get(player).body.setTransform((sprite.size.x) / PPM, pos.position.y / PPM, 0);
    }

    // Update the score
    private void updateScore() {
        // If a projectile collides with the target -> update score equal to projectile damage
        if (     ECSManager.getInstance().collisionMapper.has(target)) {
            Entity collisionEntity =      ECSManager.getInstance().collisionMapper.get(target).collisionEntity; // Get the colliding entity
            if (     ECSManager.getInstance().projectileMapper.has(collisionEntity)) {
                score +=      ECSManager.getInstance().projectileMapper.get(collisionEntity).damage;
            }
        }
    }

    // Move the target
    private void moveTarget() {
        PositionComponent pos =      ECSManager.getInstance().positionMapper.get(target);
        // Move the target to a random position within posFromEdge boundary
        float posFromEdge = (float) Math.floor(Math.random() * GameConstants.VIRTUAL_WORLD_WIDTH / 2);
             ECSManager.getInstance().b2dMapper.get(target).body.setTransform((GameConstants.VIRTUAL_WORLD_WIDTH - posFromEdge) / PPM, pos.position.y / PPM, 0);
    }
}
