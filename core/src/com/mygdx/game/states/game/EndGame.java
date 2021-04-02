package com.mygdx.game.states.game;


import com.mygdx.game.managers.ScreenManager;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.ScreenManager.SM;

/**
 * This state is for cleaning up, and displaying game ending
 **/
public class EndGame extends AbstractGameState {
    @Override
    public void startGameState() {
        SM.setScreen(ScreenManager.STATE.END_SCREEN); // Display the end screen
    }

    @Override
    public void endGameState() {}

    @Override
    public void update(float dt) {}

    @Override
    public void dispose() {}
}
