package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.states.screens.GameScreen;

import static com.mygdx.game.managers.GameStateManager.*;

/**
 * This system is responsible for moving the player when it is that players turn
 **/
public class ControllerSystem extends EntitySystem {
    // Prepare array for storing entities with movement components
    ImmutableArray<Entity> movingPlayers;

    // Prepare component mappers
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private final ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);
    private final ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);

    // Store all entities with respective components to entity arrays
    public void addedToEngine(Engine e) {
        movingPlayers = e.getEntitiesFor(Family.all(MovementControlComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float dt) {
        if (movingPlayers.size() > 0) {
            // Move player when screen is touched
            if (Gdx.input.isTouched()) movePlayer(GSM.player);

            // When space is pressed -> the player moves on to take aim and loses movement control
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                GSM.setGameState(STATE.PLAYER_AIM);
            }
        }
    }

    // Move player with velocity component
    public void movePlayer(Entity player){
        // Get entity components
        PositionComponent pos = pm.get(player);
        VelocityComponent vel = vm.get(player);
        Box2DComponent b2d = b2dm.get(player);
        SpriteComponent sc = sm.get(player);

        // Get the screen position of the touch
        float xTouchPixels = Gdx.input.getX();
        float yTouchPixels = Gdx.input.getY();

        // Convert to world position
        Vector3 touchPoint = new Vector3(xTouchPixels, yTouchPixels, 0);
        touchPoint = GameScreen.camera.unproject(touchPoint);

        // Apply force to the players box2D body according to its velocity component
        if(pos.position.x < touchPoint.x){
            b2d.body.applyLinearImpulse(vel.velocity, b2d.body.getWorldCenter(),false);

            // Flip sprite if it is already flipped from it's original state
            if (sc.sprite.isFlipX()) {
                sc.sprite.flip(true, false);
            }
        }
        else if (pos.position.x > touchPoint.x){
            Vector2 negativeImpulse = new Vector2(-vel.velocity.x, vel.velocity.y);
            b2d.body.applyLinearImpulse(negativeImpulse, b2d.body.getWorldCenter(),false);

            // Flip sprite if it is not flipped from it's initial state
            if (!sc.sprite.isFlipX()) {
                sc.sprite.flip(true, false);
            }
        }
    }
}
