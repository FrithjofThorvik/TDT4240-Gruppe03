package com.mygdx.game.states.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;
import com.mygdx.game.managers.GameStateManager;

import static com.mygdx.game.utils.GameConstants.*;

// This state handles logic for what happens when you switch between rounds (from player 1 to player 2)
public class RoundSwitch extends AbstractGameState {
    private final GameStateManager gsm;
    private Entity player;

    public RoundSwitch(GameStateManager gsm) {
        this.gsm = gsm;
    }

    @Override
    public void startGameState() {
        if (gsm.players.size() > 0) {
            player = gsm.players.get(gsm.currentPlayer);

            // Since we are switching between players, we want to remove the ability for the current player to move and aim
            player.remove(TakeAimComponent.class);
            player.remove(MovementControlComponent.class);
        }
    }

    @Override
    public void endGameState() {
        // Update current player to next player
        gsm.currentPlayer += 1;
        if (gsm.currentPlayer >= gsm.players.size()) gsm.currentPlayer = 0;

        // Fetch the new player and give that player movement control
        player = gsm.players.get(gsm.currentPlayer);
        player.add(new MovementControlComponent());

        // Switch state
        gsm.setGameState(GameStateManager.STATE.PLAY_ROUND);
    }

    @Override
    public void update(float dt) {
        // The time variable keeps control of the time spent in this state
        gsm.time += dt;

        // Logic for displaying switch time
        printGameState();

        // When the switch time has passed, switch to the next player and give that player movement control
        if (gsm.time > TIME_BETWEEN_ROUNDS) {
            endGameState();
        }
    }

    // Print timer for current GameState
    private void printGameState() {
        if (gsm.timers.size() > 0) {
            FontComponent timerFont = gsm.timers.get(0).getComponent(FontComponent.class);
            timerFont.text = "Switching players in: " + gsm.df.format(TIME_BETWEEN_ROUNDS - gsm.time) + "s";
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        }
    }
}
