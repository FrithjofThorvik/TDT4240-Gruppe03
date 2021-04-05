package com.mygdx.game.states.game;

import static com.mygdx.game.managers.ControlManager.CM;
import static com.mygdx.game.managers.GameStateManager.GSM;


/**
 * This state is for when projectiles are in air
 * GameStateManager timer will be paused
 * Timer will not be printed to screen
 **/
public class ProjectileAirborne extends AbstractGameState {
    @Override
    public void startGameState() {
        CM.idle(); // Make all controller buttons idle
        GSM.pauseTimer = true;
    }

    @Override
    public void endGameState() {
        GSM.pauseTimer = false;
    }

    @Override
    public void update(float dt) {}

    @Override
    public void dispose() {}
}
