package com.mygdx.game.states.game;


import static com.mygdx.game.managers.GameStateManager.GSM;

/**
 * In this state the player is shooting (charging power for the shot)
 **/
public class PlayerShooting extends AbstractGameState {

    @Override
    public void startGameState() {
        GSM.pauseTimer = true; // The timer should stop when you start shooting
    }

    @Override
    public void endGameState() {
        GSM.pauseTimer = false; // The timer should start when you stop shooting
    }

    @Override
    public void update(float dt) {}

    @Override
    public void dispose() {}
}
