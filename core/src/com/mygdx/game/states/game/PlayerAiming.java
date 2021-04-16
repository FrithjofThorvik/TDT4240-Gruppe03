package com.mygdx.game.states.game;


import static com.mygdx.game.managers.ControlManager.CM;
import static com.mygdx.game.managers.GameStateManager.GSM;

/**
 * This is a state responsible for indicating that a player is aiming
 **/
public class PlayerAiming extends AbstractGameState {
    @Override
    public void startGameState() {
        GSM.mode.playerAim();
    }
}
