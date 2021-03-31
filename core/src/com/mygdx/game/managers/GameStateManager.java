package com.mygdx.game.managers;

import com.mygdx.game.states.game.AbstractGameState;
import com.mygdx.game.states.game.EndGame;
import com.mygdx.game.states.game.PlayerAim;
import com.mygdx.game.states.game.PlayerShoot;
import com.mygdx.game.states.game.StartRound;
import com.mygdx.game.states.game.SwitchRound;

import java.util.HashMap;

import static com.mygdx.game.utils.GameConstants.ROUND_TIME;


/**
 * This manages game states
 * Will be used to set different states
 **/
public class GameStateManager {
    public static GameStateManager GSM; // Makes the GameStateManager accessed globally

    public float time = 0; // To keep track of time
    public int currentPlayer = 0; // Decides which player has the turn
    public int numberOfPlayers = 0; // To keep track of number of players
    public AbstractGameState gameState; // Represents the current game state

    // Create defined game states
    public enum STATE {
        START_ROUND,
        SWITCH_ROUND,
        PLAYER_AIM,
        PLAYER_SHOOT,
        END_GAME
    }

    // Store game states in HashMap
    private HashMap<STATE, AbstractGameState> gameStates;

    // GameStateManager constructor
    public GameStateManager() {
        GSM = this;
        this.initGameStates();
    }

    // Get game state
    public void update(float dt) {
        this.gameState.update(dt); // Run update functions for all AbstractGameState()
        this.time += dt; // The time variable keeps control of the time spent in this state

        if (this.time > ROUND_TIME)
            this.setGameState(STATE.SWITCH_ROUND); // Switch state
    }

    // Initialise and map all STATEs to respective GameState
    private void initGameStates() {
        // Map all states to respective GameState
        this.gameStates = new HashMap<STATE, AbstractGameState>();
        this.gameStates.put(STATE.START_ROUND, new StartRound());
        this.gameStates.put(STATE.SWITCH_ROUND, new SwitchRound());
        this.gameStates.put(STATE.PLAYER_AIM, new PlayerAim());
        this.gameStates.put(STATE.PLAYER_SHOOT, new PlayerShoot());
        this.gameStates.put(STATE.END_GAME, new EndGame());

        this.gameState = gameStates.get(STATE.SWITCH_ROUND); // Set new STATE
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
}
