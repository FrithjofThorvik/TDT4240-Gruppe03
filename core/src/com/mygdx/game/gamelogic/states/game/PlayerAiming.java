package com.mygdx.game.gamelogic.states.game;


import static com.mygdx.game.managers.GameStateManager.GSM;

/**
 * This is a state responsible for indicating that a player is aiming
 **/
public class PlayerAiming extends AbstractGameState {
    @Override
    public void startGameState() {
        GSM.getGameMode().playerAim();// Call the gamemode's function
    }
}
