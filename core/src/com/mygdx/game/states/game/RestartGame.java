package com.mygdx.game.states.game;


import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.managers.ScreenManager;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.managers.ScreenManager.SM;

/**
 * This state is for restarting the game session
 * Health, positions, etc
 **/
public class RestartGame extends AbstractGameState {
    @Override
    public void startGameState() {
        EM.removeAll(); // Remove all entities and systems
        SM.setScreen(ScreenManager.STATE.PLAY); // Display the end screen
    }

    @Override
    public void endGameState() {
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void dispose() {
    }
}
