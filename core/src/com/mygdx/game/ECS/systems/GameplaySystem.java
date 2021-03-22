package com.mygdx.game.ECS.systems;

import java.text.DecimalFormat;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;

//This system controls which player has the turn
//This system is also responsible how long the rounds are, and how much time between each round
public class GameplaySystem extends EntitySystem {
    float time = 0; // To keep track of time
    float timeBetweenRounds = 2; // How much time for players to prepare between rounds
    float roundLength = 5; // How much time does the player have to move and shoot
    int playerNr = 0; // Decides which player has the turn
    boolean gameStart = false; // Is set to true when timeBetweenRounds has passed

    private static final DecimalFormat df = new DecimalFormat("0.0"); //  Format timer

    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> timers;

    public GameplaySystem() {
    }

    // Will be called automatically by the engine -> fetches entities
    public void addedToEngine(Engine e) {
        players = e.getEntitiesFor(Family.all(PlayerComponent.class).get()); // Get all entities that are players
        timers = e.getEntitiesFor(Family.all(FontComponent.class).get()); // Get all entities that are fonts
    }

    // Will be called by the engine automatically
    public void update(float deltaTime) {
        time += deltaTime;
        Entity player = players.get(playerNr); // Get the player who has the turn
        roundCountDown(player); // Logic for having pauses between player turns

        // System.out.println(timer);
        if (gameStart && time > roundLength) {
            nextPlayerTurn(player);
        }

        // Update timer components
        Entity timer = timers.get(0);
        FontComponent timerFont = timer.getComponent(FontComponent.class);
        //PositionComponent timerPosition = timer.getComponent(PositionComponent.class);

        if (gameStart) {
            timerFont.text = "Timer: " + df.format(roundLength - time) + "s";
        } else {
            timerFont.text = "Switching players in: " + df.format(timeBetweenRounds - time) + "s";
        }
        timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
    }

    // Check if the timeBetweenRounds has been reached
    // Make the current player be able to control itself
    public void roundCountDown(Entity currentPlayer) {
        if (time > timeBetweenRounds && !gameStart) {
            gameStart = true;
            time = 0;

            // Let the current player have control
            currentPlayer.add(new MovementControlComponent());
        }
    }

    // Reset the timer and move on to the next round
    // Move on to the next player and remove control from current player
    public void nextPlayerTurn(Entity currentPlayer) {
        time = 0;
        gameStart = false;
        playerNr += 1;

        currentPlayer.remove(TakeAimComponent.class);
        currentPlayer.remove(MovementControlComponent.class);

        if (playerNr >= players.size()) {
            playerNr = 0;
        }
    }
}
