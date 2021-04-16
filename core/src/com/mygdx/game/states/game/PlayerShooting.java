package com.mygdx.game.states.game;


import static com.mygdx.game.managers.GameStateManager.GSM;

/**
 * In this state the player is shooting (charging power for the shot)
 **/
public class PlayerShooting extends AbstractGameState {

    @Override
    public void startGameState() {
        GSM.mode.playerShooting();
    }
}
