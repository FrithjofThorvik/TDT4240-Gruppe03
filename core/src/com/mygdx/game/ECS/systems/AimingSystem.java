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

import static com.mygdx.game.managers.GameStateManager.GSM;

/**
 * This system should control the aiming of a projectile
 * A player gets the TakeAimComponent when it is ready to aim
 * A player with the TakeAimComponent is controlled by the AimingSystem
 * TODO: Implement better shooting mechanics for projectiles
 * */
public class AimingSystem extends EntitySystem {
    private float power; // Represents the player's current shooting power
    float maxPower = 2; // Represents maximum shooting power
    private boolean choosePower = false; // Represents whether the player is changing shooting power or not
    private double aimAngleInRad; // The aim angle in radians. The projectile is shot according to this angle

    private ImmutableArray<Entity> playersAiming; // Array for all player entities that are aiming
    private ImmutableArray<Entity> powerBarEntities; // Array for all power bars

    //Using a component mapper is the fastest way to load entities
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);

    // AimingSystem constructor
    public AimingSystem() {}

    // Add entities to arrays
    public void addedToEngine(Engine e) {
        playersAiming = e.getEntitiesFor(Family.all(TakeAimComponent.class).get());
        powerBarEntities = e.getEntitiesFor(Family.all(PowerBarComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float deltaTime) {
        if (playersAiming.size() > 0) {
            Entity player = playersAiming.get(0); // Get current player entity
            PositionComponent position = pm.get(player); // Get the position component of that player

            // Handle events before choosing shooting power
            if (!choosePower) {
                // Calculate the aim angle when the screen is touched
                if (Gdx.input.isTouched()) aimAngleInRad = calculateAimAngle(position);

                // When the player presses "S" activate shooting power functionality
                if (Gdx.input.isKeyPressed(Input.Keys.S)) choosePower = true;
            }

            // Start shoot power handling when S key is pressed
            if (choosePower) {
                // Increase shoot power
                power += deltaTime;

                // Get power bar entities
                Entity powerBar = powerBarEntities.get(0); // PowerBar
                Entity powerBarArrow = powerBarEntities.get(1); // PowerBar Arrow

                // Get power bar positions and sprites
                PositionComponent arrowPosition = pm.get(powerBarArrow);
                SpriteComponent powerBarSprite = sm.get(powerBar);

                // Calculate and apply height of power bar arrow
                float bottomHeight = (Gdx.graphics.getHeight() - powerBarSprite.size.y) / 2f;
                arrowPosition.position.y = bottomHeight + (powerBarSprite.size.y * (power / maxPower));

                // Shoot if S key stops being pressed
                if (!Gdx.input.isKeyPressed(Input.Keys.S)) {
                    // Shoot projectile
                    shootProjectile(player);

                    // Reset values
                    power = 0;
                    choosePower = false;
                    arrowPosition.position.y = bottomHeight; // Reset power bar height

                } else if (power >= maxPower) {
                    shootProjectile(player);

                    // Reset values
                    power = 0;
                    choosePower = false;
                    arrowPosition.position.y = bottomHeight; // Reset power bar height
                }
            }
        }
    }

    // Create a projectile and shoot said projectile according to the aim angle
    public void shootProjectile(Entity player) {
        // Get player components
        PositionComponent playerPosition = pm.get(player); // Get the position component of that player
        SpriteComponent playerSprite = sm.get(player); // Get the sprite component of that player

        // Remove all power bar entities after shooting
        for (int i = 0; i < powerBarEntities.size(); ++i){
            Entity powerBarComp = powerBarEntities.get(i);
            powerBarComp.remove(RenderableComponent.class);
        }

        // Create projectile entity
        Entity projectile = new Entity();
        projectile.add(new ProjectileDamageComponent(20, 20, 10))
                .add(new VelocityComponent(1000, 1000))
                .add(new SpriteComponent(new Texture("cannonball.png"), 25f, 25f))
                .add(new PositionComponent(
                        playerPosition.position.x + 0f,
                        playerPosition.position.y + playerSprite.size.y / 1.5f)
                )
                .add(new Box2DComponent(
                        projectile.getComponent(PositionComponent.class).position,
                        projectile.getComponent(SpriteComponent.class).size,
                        false)
                )
                .add(new RenderableComponent());

        // Create projectile impulse to shoot projectile
        Box2DComponent b2d = projectile.getComponent(Box2DComponent.class);
        Vector2 vel = calculateAngleVelocity(projectile.getComponent(VelocityComponent.class).velocity);

        b2d.body.applyLinearImpulse(vel, b2d.body.getWorldCenter(),false); // Apply impulse to body

        // Add the new projectile to the engine
        getEngine().addEntity(projectile);

        // Switch rounds in GameStateManager
        GSM.setGameState(GameStateManager.STATE.SWITCH_ROUND);

        // Remove the TakeAimComponent from the current player after shot
        player.remove(TakeAimComponent.class);
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
        return new Vector2(
                (float) Math.pow(vel.x, power) * (float) Math.sin(aimAngleInRad),
                (float) Math.pow(vel.y, power) * (float) Math.cos(aimAngleInRad)
        );
    }
}
