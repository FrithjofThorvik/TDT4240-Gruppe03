package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.PowerbarComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.screens.GameScreen;

//This system is responsible for moving the player when it is that players turn
public class ControllerSystem extends EntitySystem {
    private ImmutableArray<Entity> playersInControl;
    private ImmutableArray<Entity> powerBar;


    //Using a component mapper is the fastest way to load entities
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);

    public ControllerSystem() {
    }

    //will be called automatically by the engine
    public void addedToEngine(Engine e) {
        playersInControl = e.getEntitiesFor(Family.all(PlayerComponent.class, MovementControlComponent.class).get());
        powerBar = e.getEntitiesFor(Family.all(PowerbarComponent.class).get());
    }

    //will be called by the engine automatically
    public void update(float deltaTime) {
        for (int i = 0; i < playersInControl.size(); ++i) {
            Entity entity = playersInControl.get(i);
            //Get components
            PositionComponent position = pm.get(entity);
            VelocityComponent vel = vm.get(entity);

            if (Gdx.input.isTouched()) {
                movePlayer(position,vel);
            }

            //When space is pressed -> the player moves on to take aim and loses movement control
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                entity.add(new TakeAimComponent());
                entity.remove(MovementControlComponent.class);
                for (int j = 0; j < powerBar.size(); ++j){
                    Entity powerBarComp = powerBar.get(j);
                    powerBarComp.add(new RenderableComponent());
                }
            }
        }
    }

    public void movePlayer(PositionComponent position, VelocityComponent vel){
        //get the screen position of the touch
        float xTouchPixels = Gdx.input.getX();
        float yTouchPixels = Gdx.input.getY();

        //convert to world position
        Vector3 touchPoint = new Vector3(xTouchPixels, yTouchPixels, 0);
        touchPoint = GameScreen.camera.unproject(touchPoint);

        //Move the player according to its velocity

        if(position.position.x<touchPoint.x){
            position.position.x += vel.velocity.x;
        }
        else if (position.position.x>touchPoint.x){
            position.position.x -= vel.velocity.x;
        }
    }

}
