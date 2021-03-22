package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;

//This system controls which player has the turn
//This system is also responsible how long the rounds are, and how much time between each round
public class GameplaySystem extends EntitySystem {
    float timer = 0;//To keep track of time
    float timeBetweenRounds = 0;//How much time for players to prepare between rounds
    float roundLength = 30;//How much time does the player have to move and shoot
    int playerNr = 0;//Decides which player has the turn
    boolean gameStart = false; //Is set to true when timeBetweenRounds has passed
    private ImmutableArray<Entity> entities;

    public GameplaySystem() {
    }

    //will be called automatically by the engine -> fetches entities
    public void addedToEngine(Engine e) {
        entities = e.getEntitiesFor(Family.all(PlayerComponent.class).get());//Get all entities that are players
    }

    //will be called by the engine automatically
    public void update(float deltaTime) {
        timer += deltaTime;
        Entity player = entities.get(playerNr);//Get the player who has the turn
        roundCountDown(player);//

        //System.out.println(timer);
        if (gameStart && timer > roundLength) {
            nextPlayerTurn(player);
        }
    }

    //Check if the timeBetweenRounds has been reached
    //Make the current player be able to control itself
    public void roundCountDown(Entity currentPlayer) {
        if (timer > timeBetweenRounds && !gameStart) {
            gameStart = true;
            timer = 0;
            //Let the current player have control
            currentPlayer.add(new MovementControlComponent());
        }
    }

    //Reset the timer and move on to the next round
    //Move on to the next player and remove control from current player
    public void nextPlayerTurn(Entity currentPlayer) {
        timer = 0;
        gameStart = false;
        currentPlayer.remove(TakeAimComponent.class);
        currentPlayer.remove(MovementControlComponent.class);
        playerNr += 1;
        if (playerNr >= entities.size()) {
            playerNr = 0;
        }
    }
}
