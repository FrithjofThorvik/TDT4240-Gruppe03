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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.PowerbarComponent;
import com.mygdx.game.ECS.components.ProjectileDamageComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.screens.GameScreen;

/**
 * This system should control the aiming of a projectile
 * A player gets the TakeAimComponent when it is ready to aim
 * A player with the TakeAimComponent is controlled by the AimingSystem
 * */
public class AimingSystem extends EntitySystem {
    float power;
    float maxPower = 2;
    boolean choosePower = false;
    private double aimAngleInRad; // The aim angle in radians. The projectile is shot according to this angle
    Vector3 touchPoint; // Where in the world does a touch on the screen correspond to

    private ImmutableArray<Entity> playersAiming;
    private ImmutableArray<Entity> powerBarEntities;

    //Using a component mapper is the fastest way to load entities
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);

    public AimingSystem() {
    }

    //will be called automatically by the engine
    public void addedToEngine(Engine e) {
        // Store all entities that can take aim
        playersAiming = e.getEntitiesFor(Family.all(TakeAimComponent.class).get());

        // Store all entities that has a powerbar component
        powerBarEntities = e.getEntitiesFor(Family.all(PowerbarComponent.class).get());
    }

    //will be called by the engine automatically
    public void update(float deltaTime) {
        // For all players with the TakeAimComponent (should only be one at a time)
        for (int i = 0; i < playersAiming.size(); ++i) {
            Entity player = playersAiming.get(i);
            PositionComponent position = pm.get(player); // Get the position component of that player

            // Calculate the aim angle when the screen is touched
            if (Gdx.input.isTouched() && !choosePower) {
                //get the screen position of the touch
                float xTouchPixels = Gdx.input.getX();
                float yTouchPixels = Gdx.input.getY();

                // convert to world position
                touchPoint = new Vector3(xTouchPixels, yTouchPixels, 0);
                touchPoint = GameScreen.camera.unproject(touchPoint);

                // Find the aim angle
                aimAngleInRad = calculateAngle(touchPoint.x - position.position.x, touchPoint.y - position.position.y);
            }

            // When the player presses "S" shoot the projectile
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                choosePower = true;
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

                float bottomHeight = (Gdx.graphics.getHeight() - powerBarSprite.size.y) / 2f;
                arrowPosition.position.y = bottomHeight + (powerBarSprite.size.y * (power / maxPower));

                // Shoot if S key sops being pressed
                if (!Gdx.input.isKeyPressed(Input.Keys.S)) {
                    shootProjectile(player, position, (float)Math.pow(10, power) );
                    arrowPosition.position.y = bottomHeight; // Reset power bar height
                    choosePower = false;
                    power = 0;
                }

                // Shoot if power reaches max power
                else if (power >= maxPower) {
                    shootProjectile(player, position, (float)Math.pow(10, maxPower));
                    arrowPosition.position.y = bottomHeight; // Reset power bar height
                    choosePower = false;
                    power = 0;
                }
            }

            // Interface for shot power control
        }
    }

    //Math for calculating the angle given difference in touch position and player position
    private double calculateAngle(float deltaX, float deltaY) {
        return Math.atan2(deltaX, deltaY);
    }

    //Create a projectile and shoot said projectile according to the aim angle
    public void shootProjectile(Entity currentPlayer, PositionComponent position, float power) {
        Entity projectile = new Entity();
        projectile.add(new ProjectileDamageComponent(20, 20, 10));

        //This is a bit redundant since the speed is given above, but it should be possible for projectiles to have different speeds
        projectile.add(new VelocityComponent(power * (float) Math.sin(aimAngleInRad), power * (float) Math.cos(aimAngleInRad)))
                //The velocity component is dependent on the aim angle
                .add(new SpriteComponent(new Texture("cannonball.png"), 25f, 25f))
                .add(new RenderableComponent())
                .add(new PositionComponent(position.position.x,
                        position.position.y));

        //Add the new projectile to the engine
        getEngine().addEntity(projectile);

        //Now that the projectile has been shot -> move on to the next player
        getEngine().getSystem(GameplaySystem.class).nextPlayerTurn(currentPlayer);

        //Now that the projectile has been shot -> remove the TakeAimComponent from the current player
        currentPlayer.remove(TakeAimComponent.class);
    }
}
