package com.mygdx.game.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.states.game.AbstractGameState;
import com.mygdx.game.states.game.RoundPlay;
import com.mygdx.game.states.game.RoundSwitch;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * This manages game states
 * Will be used to set different states
 **/
public class GameStateManager {
    public static GameStateManager GSM;

    public float time = 0; // To keep track of time
    public int currentPlayer = 0; // Decides which player has the turn
    public AbstractGameState gameState;
    public ImmutableArray<Entity> timers;
    public ImmutableArray<Entity> players;
    public ImmutableArray<Entity> aims;

    public DecimalFormat df = new DecimalFormat("0.0"); // Format timer that displays on the time on the screen

    // Create defined game states
    public enum STATE {
        PLAY_ROUND,
        SWITCH_ROUND
    }

    // Store game states in HashMap
    private HashMap<STATE, AbstractGameState> gameStates;

    // GameStateManager constructor
    public GameStateManager(ImmutableArray<Entity> players, ImmutableArray<Entity> timers, ImmutableArray<Entity> aims) {
        GSM = this;

        this.players = players;
        this.timers = timers;
        this.aims = aims;

        initGameStates();
        setGameState(STATE.SWITCH_ROUND);
    }

    // Initialise and map all STATEs to respective GameState
    private void initGameStates() {
        this.gameStates = new HashMap<STATE, AbstractGameState>();
        this.gameStates.put(STATE.PLAY_ROUND, new RoundPlay());
        this.gameStates.put(STATE.SWITCH_ROUND, new RoundSwitch());
    }

    // Set game state and reset timer
    public void setGameState(STATE gameState) {
        this.time = 0;
        this.gameState = gameStates.get(gameState);
        this.gameState.startGameState();
    }

    // Get game state
    public void update(float dt) {
        gameState.update(dt);
    }
}
