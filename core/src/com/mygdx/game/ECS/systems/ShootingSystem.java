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
import com.mygdx.game.ECS.components.AimComponent;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.ProjectileDamageComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.states.screens.GameScreen;

import static com.mygdx.game.utils.GameConstants.*;
import static com.mygdx.game.managers.GameStateManager.*;

/**
 * This system should control the aiming of a projectile
 * A player gets the TakeAimComponent when it is ready to aim
 * A player with the TakeAimComponent is controlled by the AimingSystem
 * TODO: Improve shooting physics
 * */
public class ShootingSystem extends EntitySystem {
    private boolean choosePower = false; // Represents whether the player is changing shooting power or not
    private double aimAngleInRad = 0f; // The aim angle in radians. The projectile is shot according to this angle

    ImmutableArray<Entity> aimingPlayers;

    // Using a component mapper is the fastest way to load entities
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<AimComponent> am = ComponentMapper.getFor(AimComponent.class);

    // Add entities to arrays
    public void addedToEngine(Engine e) {
        aimingPlayers = e.getEntitiesFor(Family.all(TakeAimComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float dt) {
        if (aimingPlayers.size() > 0) {
            AimComponent aimAngle = am.get(GSM.aim); // Access angle in AimComponent
            PositionComponent playerPosition = pm.get(GSM.player); // Get the position component of that player

            // Handle events before choosing shooting power
            if (!choosePower) {
                // Calculate the aim angle when the screen is touched
                if (Gdx.input.isTouched()) {
                    aimAngleInRad = calculateAimAngle(playerPosition);
                    aimAngle.angle = 90f - (float) aimAngleInRad / (float) Math.PI * 180f; // Update aimAngle to rotate aim
                }

                // When the player presses "S" activate shooting power functionality
                if (Gdx.input.isKeyPressed(Input.Keys.S))
                    choosePower = true; // S has been pressed
            }

            // Start shoot power handling when S key is pressed
            if (choosePower) {
                GSM.power += dt; // Increment power on holding S key

                // Shoot if S key stops being pressed, power reaches max, or round time is reached
                if (!Gdx.input.isKeyPressed(Input.Keys.S) || GSM.power >= MAX_SHOOTING_POWER || GSM.time > ROUND_TIME) {
                    shootProjectile(); // Shoot projectile
                    GSM.setGameState(STATE.SWITCH_ROUND); // Switch game state
                    choosePower = false;
                }
            }
        }
    }

    // Create a projectile and shoot said projectile according to the aim angle
    public void shootProjectile() {
        // Create and add entities to engine
        GSM.projectile = createProjectile(); // Create projectile
        getEngine().addEntity(GSM.projectile); // Add the new projectile to the engine

        // Shoot projectile with Box2D impulse
        Box2DComponent b2d = GSM.projectile.getComponent(Box2DComponent.class); // Get Box2D component
        Vector2 vel = calculateAngleVelocity(GSM.projectile.getComponent(VelocityComponent.class).velocity); // Calculate velocity
        b2d.body.applyLinearImpulse(vel, b2d.body.getWorldCenter(),false); // Apply impulse to body
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
                        pm.get(GSM.aim).position.x,
                        pm.get(GSM.aim).position.y)
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
        float impulse = vel.x * GSM.power * 10000;

        return new Vector2(
                impulse * (float) Math.sin(aimAngleInRad),
                impulse * (float) Math.cos(aimAngleInRad)
        );
    }
}
