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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.EntityManager;
import com.mygdx.game.ECS.components.HasControlComponent;
import com.mygdx.game.ECS.components.HealthComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.ProjectileDamageComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.screens.GameScreen;

import static com.mygdx.game.utils.B2DConstants.PPM;

public class ControllerSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private EntityManager entityManager;

    //Using a component mapper is the fastest way to load entities
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);

    public ControllerSystem(){}

    public void addedToEngine(Engine e){//will be called automatically by the engine
        entities = e.getEntitiesFor(Family.all(PlayerComponent.class, HasControlComponent.class).get());
    }

    public void update(float deltaTime){//will be called by the engine automatically
        for(int i=0;i<entities.size();++i){
            Entity entity = entities.get(i);
            PositionComponent position=pm.get(entity);
            VelocityComponent vel=vm.get(entity);

            position.position.x+=vel.velocity.x;
            position.position.y+=vel.velocity.y;

            if(Gdx.input.isTouched()) {
                //get the screen position of the touch
                float xTouchPixels = Gdx.input.getX();
                float yTouchPixels = Gdx.input.getY();

                //convert to world position
                Vector3 touchPoint = new Vector3(xTouchPixels,yTouchPixels,0);
                touchPoint = GameScreen.camera.unproject(touchPoint);

                //move the entity
                position.position.x=touchPoint.x;
                position.position.y=touchPoint.y;
            }

            //Only the entity that has the HasControlComponent is allowed to fire a projectile
            //This is just some shit example code :P
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                entity.add(new TakeAimComponent());
                entity.remove(HasControlComponent.class);
            }
        }
    }

}
