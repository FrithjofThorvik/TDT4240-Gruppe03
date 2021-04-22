package com.mygdx.game.gamelogic.states.game;


import com.mygdx.game.gamelogic.states.GameStateManager;


/**
 * This is a state responsible for indicating that a player is aiming
 **/
public class PlayerAiming extends AbstractGameState {
    @Override
    public void startGameState() {
        GameStateManager.getInstance().getGameMode().playerAim();// Call the gamemode's function
    }
}
