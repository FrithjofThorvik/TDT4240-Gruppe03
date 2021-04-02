package com.mygdx.game.states.game;


import com.mygdx.game.managers.ScreenManager;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.ScreenManager.SM;

/**
 * This state is for removing everything within the gameplay session
 * This state will also change the screen to MAIN_MENU in ScreenManager
 **/
public class ExitGame extends AbstractGameState {
    @Override
    public void startGameState() {
        EM.removeAll(); // Remove all entities and systems
        SM.setScreen(ScreenManager.STATE.MAIN_MENU); // Change screen to main_menu
    }

    @Override
    public void endGameState() {}

    @Override
    public void update(float dt) {}

    @Override
    public void dispose() {}
}
