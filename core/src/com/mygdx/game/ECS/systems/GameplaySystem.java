package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.ECS.EntityManager;
import com.mygdx.game.ECS.components.HasControlComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;

public class GameplaySystem extends EntitySystem {
    float timer =0;
    float timeBetweenRounds = 0;
    float roundLength = 30;
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
        timer +=deltaTime;
        Entity player = entities.get(i);
        roundCountDown(player);
        
        //System.out.println(timer);
        if(gameStart&& timer > roundLength){
            nextPlayerTurn(player);
        }
    }

    public void nextPlayerTurn(Entity currentPlayer){
        timer =0;
        gameStart=false;
        currentPlayer.remove(TakeAimComponent.class);
        currentPlayer.remove(HasControlComponent.class);
        i+=1;
        if(i>=entities.size()){i=0;}
    }
    
    public void roundCountDown(Entity currentPlayer){
        if(timer > timeBetweenRounds && !gameStart){
            gameStart=true;
            timer =0;
            currentPlayer.add(new HasControlComponent());
        }
    }
}
