package com.mygdx.game.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mygdx.game.ECS.components.AimComponent;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PowerBarComponent;
import com.mygdx.game.states.game.AbstractGameState;
import com.mygdx.game.states.game.PlayerAim;
import com.mygdx.game.states.game.StartRound;
import com.mygdx.game.states.game.SwitchRound;
import java.text.DecimalFormat;
import java.util.HashMap;

import static com.mygdx.game.utils.GameConstants.ROUND_TIME;
import static com.mygdx.game.utils.GameConstants.TIME_BETWEEN_ROUNDS;

/**
 * This manages game states
 * Will be used to set different states
 **/
public class GameStateManager {
    public static GameStateManager GSM; // Makes the GameStateManager accessed globally
    private final Engine engine;

    public float power = 0; // Decides power of projectile shot
    public float time = 0; // To keep track of time
    public int currentPlayer = 0; // Decides which player has the turn

    public Entity player; // Current player entity
    public Entity timer; // Timer for displaying game states
    public Entity aim; // Aim arrow for players
    public Entity powerBar; // Power bar sprite (green -> red)
    public Entity powerBarArrow; // Arrow displaying current shot power
    public Entity projectile; // Active projectile (added in AimingSystem -> shootProjectile())
    public AbstractGameState gameState; // Represents the current game state

    public ImmutableArray<Entity> players; // List of players

    public DecimalFormat df = new DecimalFormat("0.0"); // Format timer that displays on the time on the screen

    // Create defined game states
    public enum STATE {
        START_ROUND,
        SWITCH_ROUND,
        PLAYER_AIM
    }

    // Store game states in HashMap
    private HashMap<STATE, AbstractGameState> gameStates;

    // GameStateManager constructor
    public GameStateManager(Engine e) {
        GSM = this;
        engine = e;

        getEntities();
        initGameStates();
    }

    private void getEntities() {
        // Get all entities
        ImmutableArray<Entity> players =  engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
        ImmutableArray<Entity> aims =  engine.getEntitiesFor(Family.all(AimComponent.class).get());
        ImmutableArray<Entity> timers =  engine.getEntitiesFor(Family.all(FontComponent.class).get());
        ImmutableArray<Entity> powerBars =  engine.getEntitiesFor(Family.all(PowerBarComponent.class).get());

        // Store entities
        this.players = players;
        this.player = players.get(currentPlayer);

        this.powerBar = powerBars.get(0);
        this.powerBarArrow = powerBars.get(1);

        this.timer = timers.get(0);
        this.aim = aims.get(0);
    }

    // Initialise and map all STATEs to respective GameState
    private void initGameStates() {
        // Map all states to respective GameState
        this.gameStates = new HashMap<STATE, AbstractGameState>();
        this.gameStates.put(STATE.START_ROUND, new StartRound());
        this.gameStates.put(STATE.SWITCH_ROUND, new SwitchRound());
        this.gameStates.put(STATE.PLAYER_AIM, new PlayerAim());

        // Set current game state
        this.gameState = gameStates.get(STATE.SWITCH_ROUND);
        this.gameState.startGameState();
    }

    // Set game state and reset timer
    public void setGameState(STATE gameState) {
        this.gameState.endGameState(); // End previous STATE
        this.gameState = gameStates.get(gameState); // Set new STATE
        this.gameState.startGameState(); // Start current STATE
    }

    // Get game state
    public void update(float dt) {
        gameState.update(dt); // Run update functions for all AbstractGameState()
        GSM.time += dt; // The time variable keeps control of the time spent in this state
        printTimer();

        if (GSM.time > ROUND_TIME)
            GSM.setGameState(STATE.SWITCH_ROUND); // Switch state
    }

    private void printTimer() {
        if (gameState == gameStates.get(STATE.SWITCH_ROUND)) {
            FontComponent timerFont = GSM.timer.getComponent(FontComponent.class);
            timerFont.text = "Switching players in: " + GSM.df.format(TIME_BETWEEN_ROUNDS - GSM.time) + "s";
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        } else {
            FontComponent timerFont = GSM.timer.getComponent(FontComponent.class);
            timerFont.text = "Timer: " + GSM.df.format(ROUND_TIME - GSM.time) + "s";
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        }
    }
}
