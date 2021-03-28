package com.mygdx.game.states.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;

import static com.mygdx.game.managers.GameStateManager.*;
import static com.mygdx.game.utils.GameConstants.*;

// This state handles logic for what happens when you switch between rounds (from player 1 to player 2)
public class RoundSwitch extends AbstractGameState {
    private Entity player;

    @Override
    public void startGameState() {
        if (GSM.players.size() > 0) {
            player = GSM.players.get(GSM.currentPlayer);

            // Since we are switching between players, we want to remove the ability for the current player to move and aim
            player.remove(TakeAimComponent.class);
            player.remove(MovementControlComponent.class);
            if (GSM.aims.size() > 0) {
                Entity aim = GSM.aims.get(0);
                aim.remove(RenderableComponent.class); // Remove render from aim component
            }
        }
    }

    @Override
    public void endGameState() {
        // Update current player to next player
        GSM.currentPlayer++;
        if (GSM.currentPlayer >= GSM.players.size()) GSM.currentPlayer = 0;

        // Fetch the new player and give that player movement control
        player = GSM.players.get(GSM.currentPlayer);
        player.add(new MovementControlComponent());

        // Switch state
        GSM.setGameState(STATE.PLAY_ROUND);
    }

    @Override
    public void update(float dt) {
        // The time variable keeps control of the time spent in this state
        GSM.time += dt;

        // Logic for displaying switch time
        printGameState();

        // When the switch time has passed, switch to the next player and give that player movement control
        if (GSM.time > TIME_BETWEEN_ROUNDS) {
            endGameState();
        }
    }

    // Print timer for current GameState
    private void printGameState() {
        if (GSM.timers.size() > 0) {
            FontComponent timerFont = GSM.timers.get(0).getComponent(FontComponent.class);
            timerFont.text = "Switching players in: " + GSM.df.format(TIME_BETWEEN_ROUNDS - GSM.time) + "s";
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        }
    }
}
