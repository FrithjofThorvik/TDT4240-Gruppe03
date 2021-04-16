package com.mygdx.game.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.RenderComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
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

/**
 * This class controls the game logic of a training game
 * This class also spawns the entities required for a training game
 **/
public class Training implements GameMode {
    private final DecimalFormat df = new DecimalFormat("0.0"); // Format timer that displays on the time on the screen
    float timer = 0; // The timer
    int score =0; // This will increase when you hit the target
    float trainingLength = 60;

    Entity player;
    Entity scoreFont;
    Entity target;

    public Training() {
    }

    @Override
    public void update(float dt) {
        updateUI(); // Update UI elements
        // Check if aim button is pressed
        if (CM.aimPressed)
            GSM.setGameState(GameStateManager.STATE.PLAYER_AIMING);

        timer += dt;
        if (timer >= trainingLength) // End game when timer runs out
            GSM.setGameState(GameStateManager.STATE.END_GAME);
    }

    @Override
    public void startGame() { // Is called when the game starts
        setPlayerSpawn(); // Choose location for where players spawn

        // Start or stop systems (if they should be processed or not)
        EM.engine.getSystem(ShootingSystem.class).setProcessing(false);
        EM.engine.getSystem(MovementSystem.class).setProcessing(false);
        EM.engine.getSystem(AimingSystem.class).setProcessing(false);
    }

    @Override
    public void endGame() { // Is called when the game ends
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
        CM.startMoving(); // Enable moving buttons (includes aiming button)
    }

    @Override
    public void playerAim() { // Is called when the player should start aiming
        CM.startShooting(); // Enable shooting button

        // Remove or add components to entities
        player.add(new isAimingComponent());
        EM.powerBar.add(new RenderComponent()); // Render power bar
        EM.powerBarArrow.add(new RenderComponent()); // Render power bar arrow
        EM.aimArrow.add(new RenderComponent()); // Render the aim arrow

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
        EM.aimArrow.remove(RenderComponent.class);
        EM.powerBar.remove(RenderComponent.class);
        EM.powerBarArrow.remove(RenderComponent.class);

        // Start or stop systems (if they should be processed or not)
        EM.engine.getSystem(ShootingSystem.class).setProcessing(false);
    }

    @Override
    public void switchRound() { // Is called when the game is switching between round
        // Do nothing
    }

    @Override
    public void updateUI() { // Is called every update
        this.printTimer(); // Print information about how much time is left
        this.printScore(); // Prints the score

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
    }

    @Override
    public void initEntities() {
        EM.spawnPlayers(1);
        player = EM.engine.getEntitiesFor(Family.one(PlayerComponent.class).get()).first();
        EM.createUIEntities();
        scoreFont = EM.entityCreator.getTextFont().createEntity();
        EM.positionMapper.get(scoreFont).position = new Vector2(Application.camera.viewportWidth / 2f,
                Application.camera.viewportHeight * 0.93f);
        target = EM.spawnTarget(1);
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
}
