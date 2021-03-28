package com.mygdx.game.states.game;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.managers.GameStateManager;
import static com.mygdx.game.utils.GameConstants.*;

// When it is currently a players turn, the game is in this state
public class RoundPlay extends AbstractGameState {
    private final GameStateManager gsm;

    public RoundPlay(GameStateManager gsm) {
        this.gsm = gsm;
    }

    @Override
    public void startGameState() {}

    @Override
    public void endGameState() {
        gsm.setGameState(GameStateManager.STATE.SWITCH_ROUND);
    }

    @Override
    public void update(float dt) {
        // The time variable keeps control of the time spent in this state
        gsm.time += dt;

        // Logic for displaying amount of time left in the round
        printGameState();

        // Switch state if time has run out
        if (gsm.time > ROUND_TIME){
            endGameState();
        }
    }

    // Function for printing timer for current GameState
    private void printGameState() {
        if (gsm.timers.size() > 0) {
            FontComponent timerFont = gsm.timers.get(0).getComponent(FontComponent.class);
            timerFont.text = "Timer: " + gsm.df.format(ROUND_TIME - gsm.time) + "s";
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        }
    }
}
