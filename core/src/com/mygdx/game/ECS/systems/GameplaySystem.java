package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.ECS.EntityManager;
import com.mygdx.game.ECS.components.HasControlComponent;
import com.mygdx.game.ECS.components.PlayerComponent;

public class GameplaySystem extends EntitySystem {
    float time =0;
    float countDown = 2;
    float roundTime = 10;
    int i = 0;
    boolean gameStart=false;
    private ImmutableArray<Entity> entities;
    private EntityManager entityManager;

    //Using a component mapper is the fastest way to load entities
    //private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

    public GameplaySystem(){}

    public void addedToEngine(Engine e){//will be called automatically by the engine
        entities = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    public void update(float deltaTime){//will be called by the engine automatically
        time+=deltaTime;
        Entity player = entities.get(i);
        if(time>countDown && !gameStart){
            gameStart=true;
            time=0;
            player.add(new HasControlComponent());
        }

        System.out.println(time);
        if(gameStart){
            if(time>roundTime){
                time=0;
                gameStart=false;
                player.remove(HasControlComponent.class);
                i+=1;
                if(i>=entities.size()){i=0;}
            }
        }
    }
}
