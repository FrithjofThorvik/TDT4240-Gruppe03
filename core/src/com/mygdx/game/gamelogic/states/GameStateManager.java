package com.mygdx.game.gamelogic.states;

import com.mygdx.game.gamelogic.states.game.AbstractGameState;
import com.mygdx.game.gamelogic.states.game.EndGame;
import com.mygdx.game.gamelogic.states.game.PlayerAiming;
import com.mygdx.game.gamelogic.states.game.PlayerShooting;
import com.mygdx.game.gamelogic.states.game.ProjectileAirborne;
import com.mygdx.game.gamelogic.states.game.StartGame;
import com.mygdx.game.gamelogic.states.game.StartRound;
import com.mygdx.game.gamelogic.states.game.SwitchRound;
import com.mygdx.game.gamelogic.strategies.gamemodes.LocalMultiplayer;
import com.mygdx.game.gamelogic.strategies.gamemodes.GameMode;
import com.mygdx.game.gamelogic.strategies.gamemodes.OnlineMultiplayer;
import com.mygdx.game.gamelogic.strategies.gamemodes.Training;

import java.util.HashMap;


/**
 * This manages game states
 * Will be used to set different states
 * Will also be used as a data_layer to store & get game information
 **/
public class GameStateManager {
    private static GameStateManager GSM; // Makes the GameStateManager accessed globally
    public AbstractGameState gameState; // Represents the current game state
    private GameMode gameMode; // Is used to pick the game mode

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

    // Create defined game states
    public enum GAMEMODE {
        LOCAL,
        ONLINE,
        TRAINING
    }

    // Store game states in HashMap
    private HashMap<STATE, AbstractGameState> gameStates;

    // Store game modes in HashMap
    private HashMap<GAMEMODE, GameMode> gameModes;

    // GameStateManager constructor instantiates a static instance of itself and initializes all states
    private GameStateManager() {
        this.initGameStates();
        this.initGameModes();
    }

    // Get game state
    public void update(float dt) {
        gameMode.update(dt);
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
    }

    private void initGameModes() {
        // Map all states to respective GameState
        this.gameModes = new HashMap<GAMEMODE, GameMode>();

        this.gameModes.put(GAMEMODE.LOCAL, new LocalMultiplayer());
        this.gameModes.put(GAMEMODE.ONLINE, new OnlineMultiplayer());
        this.gameModes.put(GAMEMODE.TRAINING, new Training());
    }

    // Set game state and reset timer
    public void setGameState(STATE gameState) {
        this.gameState = gameStates.get(gameState); // Set new STATE
        this.gameState.startGameState(); // Start current STATE
    }

    // Get a game state from the hash map
    public AbstractGameState getGameState(STATE gameState) {
        return this.gameStates.get(gameState);
    }

    // Set a the gamemode
    public void setGameMode(GAMEMODE gameMode) {
        this.gameMode = gameModes.get(gameMode);
    }

    // Get the current gamemode
    public GameMode getGameMode() {
        return this.gameMode;
    }

    public static GameStateManager getInstance() {
        if (GSM == null)
            GSM = new GameStateManager();
        return GSM;
    }
}
