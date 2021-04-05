package com.mygdx.game.managers;

import com.mygdx.game.states.game.AbstractGameState;
import com.mygdx.game.states.game.EndGame;
import com.mygdx.game.states.game.PlayerAiming;
import com.mygdx.game.states.game.PlayerShooting;
import com.mygdx.game.states.game.ProjectileAirborne;
import com.mygdx.game.states.game.StartGame;
import com.mygdx.game.states.game.StartRound;
import com.mygdx.game.states.game.SwitchRound;

import java.util.HashMap;

import static com.mygdx.game.utils.GameConstants.ROUND_TIME;


/**
 * This manages game states
 * Will be used to set different states
 * Will also be used as a data_layer to store & get game information
 **/
public class GameStateManager {
    public static GameStateManager GSM; // Makes the GameStateManager accessed globally
    public AbstractGameState gameState; // Represents the current game state

    public boolean pauseTimer = false;
    public boolean pauseGame = false;
    public float time = 0; // To keep track of time
    public int currentPlayer = 0; // Decides which player has the turn
    public int numberOfPlayers = 0; // To keep track of number of players
    // TODO: Add more to data_layer

    // Create defined game states
    public enum STATE {
        START_GAME,
        END_GAME,
        START_ROUND,
        SWITCH_ROUND,
        PLAYER_AIMING,
        PLAYER_SHOOTING,
        PROJECTILE_AIRBORNE
    }

    // Store game states in HashMap
    private HashMap<STATE, AbstractGameState> gameStates;

    // GameStateManager constructor instantiates a static instance of itself and initializes all states
    public GameStateManager() {
        GSM = this;

        this.initGameStates();
    }

    // Get game state
    public void update(float dt) {
        // Check if game is paused
        if (!this.pauseGame) {
            this.gameState.update(dt); // Run update functions for all AbstractGameState()

            if (!this.pauseTimer)
                this.time += dt; // The time variable keeps control of the time spent in this state

            if (this.time > ROUND_TIME)
                this.setGameState(STATE.SWITCH_ROUND); // Force round_switch if timer is larger that round_time
        }
    }

    // Initialise and map all STATEs to respective GameState
    private void initGameStates() {
        // Map all states to respective GameState
        this.gameStates = new HashMap<STATE, AbstractGameState>();

        this.gameStates.put(STATE.START_GAME, new StartGame());
        this.gameStates.put(STATE.END_GAME, new EndGame());

        this.gameStates.put(STATE.START_ROUND, new StartRound());
        this.gameStates.put(STATE.SWITCH_ROUND, new SwitchRound());
        this.gameStates.put(STATE.PLAYER_AIMING, new PlayerAiming());
        this.gameStates.put(STATE.PLAYER_SHOOTING, new PlayerShooting());
        this.gameStates.put(STATE.PROJECTILE_AIRBORNE, new ProjectileAirborne());

        this.gameState = gameStates.get(STATE.START_GAME); // Set new STATE
        this.gameState.startGameState(); // Start current STATE
    }

    // Set game state and reset timer
    public void setGameState(STATE gameState) {
        this.gameState.endGameState(); // End previous STATE
        this.gameState = gameStates.get(gameState); // Set new STATE
        this.gameState.startGameState(); // Start current STATE
    }

    // Get a game state from the hash map
    public AbstractGameState getGameState(STATE gameState) {
        return this.gameStates.get(gameState);
    }

    // Loos through all GameStates and runs respective dispose() functions
    public void dispose() {
        for (AbstractGameState state : this.gameStates.values()) {
            if (state != null)
                state.dispose();
        }
    }
}
