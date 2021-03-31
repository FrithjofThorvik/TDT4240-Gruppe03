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
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.states.screens.GameScreen;

import static com.mygdx.game.managers.GameStateManager.GSM;

/**
 * This system is responsible for moving the player when it is that players turn
 **/
public class ControllerSystem extends EntitySystem {
    // Prepare arrays for entities
    private ImmutableArray<Entity> players;

    // Prepare component mappers
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private final ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);
    private final ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);

    // ControllerSystem constructor
    public ControllerSystem() {
    }

    // Store all entities with respective components to entity arrays
    public void addedToEngine(Engine e) {
        players = e.getEntitiesFor(Family.all(PlayerComponent.class, MovementControlComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float deltaTime) {
        // Loop through all player entities
        for (int i = 0; i < players.size(); ++i) {
            // Get player entity with controller component
            Entity player = players.get(i);

            // Move player when screen is touched
            if (Gdx.input.isTouched()) movePlayer(player);

            // When space is pressed -> change state to aiming
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                GSM.setGameState(GameStateManager.STATE.PLAYER_AIM);
            }
        }
    }

    public void movePlayer(Entity player) {
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
        if (pos.position.x < touchPoint.x) {
            b2d.body.applyLinearImpulse(vel.velocity, b2d.body.getWorldCenter(), false);

            // Flip sprite if it is already flipped from it's original state
            if (sc.sprite.isFlipX()) {
                sc.sprite.flip(true, false);
            }
        } else if (pos.position.x > touchPoint.x) {
            Vector2 negativeImpulse = new Vector2(-vel.velocity.x, vel.velocity.y);
            b2d.body.applyLinearImpulse(negativeImpulse, b2d.body.getWorldCenter(), false);

            // Flip sprite if it is not flipped from it's initial state
            if (!sc.sprite.isFlipX()) {
                sc.sprite.flip(true, false);
            }
        }
    }

}
