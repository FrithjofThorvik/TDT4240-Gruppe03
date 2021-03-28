package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.MassData;
import com.mygdx.game.ECS.components.AimComponent;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.PowerBarComponent;
import com.mygdx.game.ECS.components.ProjectileDamageComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.states.screens.GameScreen;

import java.util.Arrays;

import javax.swing.plaf.synth.SynthTextAreaUI;

import static com.mygdx.game.utils.GameConstants.*;

/**
 * This system should control the aiming of a projectile
 * A player gets the TakeAimComponent when it is ready to aim
 * A player with the TakeAimComponent is controlled by the AimingSystem
 * TODO: Implement better shooting mechanics for projectiles
 * */
public class AimingSystem extends EntitySystem {
    private float power; // Represents the player's current shooting power
    private double aimAngleInRad; // The aim angle in radians. The projectile is shot according to this angle
    private float startPositionArrow; // Calculated starting position for power bar arrow
    private boolean choosePower = false; // Represents whether the player is changing shooting power or not

    // Arrays for storing player & power bar entities
    private ImmutableArray<Entity> playersAiming; // Array for all player entities that are aiming
    private ImmutableArray<Entity> powerBarEntities; // Array for all power bars
    private ImmutableArray<Entity> aims; // Array for all power bars

    // Entities used in AimingSystem
    private Entity player;
    private Entity aim;
    private Entity powerBar;
    private Entity powerBarArrow;

    // Components used from entities
    private PositionComponent playerPosition;
    private PositionComponent aimPosition;
    private PositionComponent arrowPosition;
    private SpriteComponent playerSprite;
    private SpriteComponent aimSprite;
    private SpriteComponent powerBarSprite;
    private AimComponent aimAngle;

    // Using a component mapper is the fastest way to load entities
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);
    private final ComponentMapper<AimComponent> am = ComponentMapper.getFor(AimComponent.class);

    // Add entities to arrays
    public void addedToEngine(Engine e) {
        playersAiming = e.getEntitiesFor(Family.all(TakeAimComponent.class).get());
        powerBarEntities = e.getEntitiesFor(Family.all(PowerBarComponent.class).get());
        aims = e.getEntitiesFor(Family.all(AimComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float dt) {
        // Check if any players with aim components have been registered
        if (playersAiming.size() > 0) {
            setPlayerComponents();
            setPowerBarComponents();
            setAimingComponents();
            updateAim();

            // Handle events before choosing shooting power
            if (!choosePower) {
                // Calculate the aim angle when the screen is touched
                if (Gdx.input.isTouched()) {
                    aimAngleInRad = calculateAimAngle(playerPosition);
                    aimAngle.angle = 90f - (float) aimAngleInRad / (float) Math.PI * 180f;
                }

                // When the player presses "S" activate shooting power functionality
                if (Gdx.input.isKeyPressed(Input.Keys.S)) choosePower = true;
            }

            // Start shoot power handling when S key is pressed
            if (choosePower) {
                power += dt; // Increase shoot power
                updatePowerBar(); // Update position of power bar components

                // Shoot if S key stops being pressed
                if (!Gdx.input.isKeyPressed(Input.Keys.S) || power >= MAX_SHOOTING_POWER) {
                    shootProjectile(); // Shoot projectile
                }
            }
        }
    }

    // Create a projectile and shoot said projectile according to the aim angle
    public void shootProjectile() {
        // Create and add entities to engine
        Entity projectile = createProjectile(); // Create projectile
        getEngine().addEntity(projectile); // Add the new projectile to the engine
        removePowerBar(); // Remove all power bar entities after shooting

        // Shoot projectile with Box2D impulse
        Box2DComponent b2d = projectile.getComponent(Box2DComponent.class); // Get Box2D component
        Vector2 vel = calculateAngleVelocity(projectile.getComponent(VelocityComponent.class).velocity); // Calculate velocity
        b2d.body.applyLinearImpulse(vel, b2d.body.getWorldCenter(),false); // Apply impulse to body

        // Switches round, removes player components, and resets values
        endRound();
    }

    // Creates a projectile
    private Entity createProjectile() {
        Entity projectile = new Entity();

        projectile
                .add(new ProjectileDamageComponent(20, 20, 1000))
                .add(new VelocityComponent(
                        (float)projectile.getComponent(ProjectileDamageComponent.class).speed,
                        (float)projectile.getComponent(ProjectileDamageComponent.class).speed)
                )
                .add(new PositionComponent(
                        aimPosition.position.x,
                        aimPosition.position.y)
                )
                .add(new SpriteComponent(
                        new Texture("cannonball.png"),
                        projectile.getComponent(PositionComponent.class).position,
                        25f,
                        25f)
                )
                .add(new Box2DComponent(
                        projectile.getComponent(PositionComponent.class).position,
                        projectile.getComponent(SpriteComponent.class).size,
                        false)
                )
                .add(new RenderableComponent());

        return projectile;
    }

    // End current round
    private void endRound() {
        // Switch rounds in GameStateManager
        getEngine().getSystem(GameplaySystem.class).gameStateManager.setGameState(GameStateManager.STATE.SWITCH_ROUND);

        // Reset values
        power = 0;
        choosePower = false;
        resetPowerBar();
    }

    // Update aiming arrow
    private void updateAim() {
        aimSprite.sprite.setRotation(aimAngle.angle);
    }

    // Update position for power bar components
    private void updatePowerBar() {
        arrowPosition.position.y = startPositionArrow + (powerBarSprite.size.y * (power / MAX_SHOOTING_POWER));
    }

    // Reset position for power bar components
    private void resetPowerBar() {
        arrowPosition.position.y = startPositionArrow; // Reset power bar height
    }

    // Remove all power bar entities after shooting
    private void removePowerBar() {
        for (int i = 0; i < powerBarEntities.size(); ++i){
            Entity powerBarComp = powerBarEntities.get(i);
            powerBarComp.remove(RenderableComponent.class);
        }
    }

    // Calculate angle of click relative to player position
    private double calculateAimAngle(PositionComponent position) {
        // Get the screen position of touch/click
        float xTouchPixels = Gdx.input.getX();
        float yTouchPixels = Gdx.input.getY();

        // Convert touch/click to world position
        Vector3 touchPoint = new Vector3(xTouchPixels, yTouchPixels, 0);
        touchPoint = GameScreen.camera.unproject(touchPoint);

        // Find the aim angle in radians
        return Math.atan2(touchPoint.x - position.position.x, touchPoint.y - position.position.y);
    }

    // Calculate velocity vector with angle
    private Vector2 calculateAngleVelocity(Vector2 vel) {
        float impulse = vel.x * power * 10000;

        return new Vector2(
                impulse * (float) Math.sin(aimAngleInRad),
                impulse * (float) Math.cos(aimAngleInRad)
        );
    }

    // Set global power bar components
    protected void setPowerBarComponents() {
        if (powerBarEntities.size() > 0) {
            powerBar = powerBarEntities.get(0); // PowerBar
            powerBarArrow = powerBarEntities.get(1); // PowerBar Arrow

            // Get power bar positions and sprites
            arrowPosition = pm.get(powerBarArrow);
            powerBarSprite = sm.get(powerBar);

            // Calculate and apply height of power bar arrow
            startPositionArrow = (Gdx.graphics.getHeight() - powerBarSprite.size.y) / 2f;
        }
    }

    // Set global player components
    protected void setPlayerComponents() {
        if (playersAiming.size() > 0) {
            player = playersAiming.get(0); // Get current player entity
            playerPosition = pm.get(player); // Get the position component of that player
            playerSprite = sm.get(player); // Get the sprite component of that player
        }
    }

    // Set global aiming components
    protected void setAimingComponents() {
        if (aims.size() > 0) {
            aim = aims.get(0);
            aim.add(new RenderableComponent());
            aimPosition = pm.get(aim);
            aimSprite = sm.get(aim);
            aimAngle = am.get(aim);

            aimPosition.position.x = playerPosition.position.x;
            aimPosition.position.y = playerPosition.position.y + playerSprite.size.y / 2;
        }
    }
}
