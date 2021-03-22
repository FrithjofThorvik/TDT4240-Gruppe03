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
import com.mygdx.game.ECS.components.HasControlComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.ProjectileDamageComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.screens.GameScreen;

public class AimingSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    Vector3 touchPoint;
    private double aimAngleInRad;

    //Using a component mapper is the fastest way to load entities
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

    public AimingSystem(){}

    public void addedToEngine(Engine e){//will be called automatically by the engine
        entities = e.getEntitiesFor(Family.all(TakeAimComponent.class).get());
    }

    public void update(float deltaTime){//will be called by the engine automatically
        for(int i=0;i<entities.size();++i){
            Entity player = entities.get(i);
            PositionComponent position=pm.get(player);

            //take aim
            if(Gdx.input.isTouched()) {
                //get the screen position of the touch
                float xTouchPixels = Gdx.input.getX();
                float yTouchPixels = Gdx.input.getY();

                //convert to world position
                touchPoint = new Vector3(xTouchPixels,yTouchPixels,0);
                touchPoint = GameScreen.camera.unproject(touchPoint);

                //find the aim angle
                aimAngleInRad = calculateAngle(touchPoint.x-position.position.x,touchPoint.y-position.position.y);
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.S)){
                shootProjectile(player,position);
            }
        }
    }

    private double calculateAngle(float deltaX, float deltaY){
        return Math.atan2(deltaX,deltaY);
    }
    public void shootProjectile(Entity currentPlayer, PositionComponent position){
        Entity projectile = new Entity();
        projectile.add(new VelocityComponent(10*(float)Math.sin(aimAngleInRad),10*(float)Math.cos(aimAngleInRad)))
                .add(new SpriteComponent(new Texture("badlogic.jpg"), 20f))
                .add(new RenderableComponent())
                .add(new PositionComponent(position.position.x,
                        position.position.y))
                .add(new ProjectileDamageComponent(20,20));
        getEngine().addEntity(projectile);
        getEngine().getSystem(GameplaySystem.class).nextPlayerTurn(currentPlayer);
        currentPlayer.remove(TakeAimComponent.class);
    }
}
