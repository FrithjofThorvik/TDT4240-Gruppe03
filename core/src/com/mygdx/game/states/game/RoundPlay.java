package com.mygdx.game.states.game;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mygdx.game.ECS.components.FontComponent;

import static com.mygdx.game.managers.GameStateManager.*;
import static com.mygdx.game.utils.GameConstants.*;

// When it is currently a players turn, the game is in this state
public class RoundPlay extends AbstractGameState {

    @Override
    public void startGameState() {}

    @Override
    public void endGameState() {
        GSM.setGameState(STATE.SWITCH_ROUND);
    }

    @Override
    public void update(float dt) {
        // The time variable keeps control of the time spent in this state
        GSM.time += dt;

        // Logic for displaying amount of time left in the round
        printGameState();

        // Switch state if time has run out
        if (GSM.time > ROUND_TIME){
            endGameState();
        }
    }

    // Function for printing timer for current GameState
    private void printGameState() {
        if (GSM.timers.size() > 0) {
            FontComponent timerFont = GSM.timers.get(0).getComponent(FontComponent.class);
            timerFont.text = "Timer: " + GSM.df.format(ROUND_TIME - GSM.time) + "s";
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        }
    }
}
