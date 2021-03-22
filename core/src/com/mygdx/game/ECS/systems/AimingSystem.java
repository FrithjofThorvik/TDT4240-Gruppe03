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
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.ProjectileDamageComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.screens.GameScreen;

//This system should control the aiming of a projectile
//A player gets the TakeAimComponent when it is ready to aim
//A player with the TakeAimComponent is controlled by the AimingSystem
public class AimingSystem extends EntitySystem {
    private ImmutableArray<Entity> playersAiming;
    Vector3 touchPoint;//Where in the world does a touch on the screen correspond to
    private double aimAngleInRad;//The aim angle in radians. The projectile is shot according to this angle
    float power;
    float maxPower = 2;
    boolean choosePower = false;

    //Using a component mapper is the fastest way to load entities
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

    public AimingSystem() {
    }

    //will be called automatically by the engine
    public void addedToEngine(Engine e) {
        playersAiming = e.getEntitiesFor(Family.all(TakeAimComponent.class).get());
    }

    //will be called by the engine automatically
    public void update(float deltaTime) {
        //For all players with the TakeAimComponent (should only be one at a time)
        for (int i = 0; i < playersAiming.size(); ++i) {
            Entity player = playersAiming.get(i);
            PositionComponent position = pm.get(player);//Get the position component of that player

            //Calculate the aim angle when the screen is touched
            if (Gdx.input.isTouched()) {
                //get the screen position of the touch
                float xTouchPixels = Gdx.input.getX();
                float yTouchPixels = Gdx.input.getY();

                //convert to world position
                touchPoint = new Vector3(xTouchPixels, yTouchPixels, 0);
                touchPoint = GameScreen.camera.unproject(touchPoint);

                //find the aim angle
                aimAngleInRad = calculateAngle(touchPoint.x - position.position.x, touchPoint.y - position.position.y);
            }

            //When the player presses "S" shoot the projectile
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                choosePower = true;
            }

            if (choosePower) {
                power += deltaTime;
                if (!Gdx.input.isKeyPressed(Input.Keys.S)) {
                    shootProjectile(player, position, (float)Math.pow(10, power) );
                    choosePower = false;
                    power = 0;
                } else if (power >= maxPower) {
                    shootProjectile(player, position, (float)Math.pow(10, maxPower));
                    choosePower = false;
                    power = 0;
                }
            }
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
                .add(new SpriteComponent(new Texture("cannonball.png"), 25f))
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
