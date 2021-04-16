package com.mygdx.game.states.game;


import com.mygdx.game.managers.ScreenManager;
import com.mygdx.game.states.Mode;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.managers.ScreenManager.SM;

/**
 * This state is for cleaning up, and displaying game ending
 **/
public class EndGame extends AbstractGameState {
    @Override
    public void startGameState() {
        GSM.mode.endGame(); // Display the end screen
    }
}
