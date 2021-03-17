package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.screens.GameScreen;

import static com.mygdx.game.utils.B2DConstants.PPM;

public class ControllerSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    //Using a component mapper is the fastest way to load entities
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);

    public ControllerSystem(){}

    public void addedToEngine(Engine e){//will be called automatically by the engine
        entities = e.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    public void update(float deltaTime){//will be called by the engine automatically
        for(int i=0;i<entities.size();++i){
            Entity entity = entities.get(i);
            PositionComponent position=pm.get(entity);
            VelocityComponent velocity=vm.get(entity);

            if(Gdx.input.isTouched()) {
                //get the screen position of the touch
                float xTouchPixels = Gdx.input.getX();
                float yTouchPixels = Gdx.input.getY();

                //convert to world position
                Vector3 touchPoint = new Vector3(xTouchPixels,yTouchPixels,0);
                touchPoint = GameScreen.camera.unproject(touchPoint);

                //move the entity
                position.x=touchPoint.x;
                position.y=touchPoint.y;
            }
        }
    }

}
