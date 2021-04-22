package com.mygdx.game.gamelogic.states.game;

import com.mygdx.game.gamelogic.states.GameStateManager;

/**
 * In this state the player is shooting (charging power for the shot)
 **/
public class PlayerShooting extends AbstractGameState {

    @Override
    public void startGameState() {
        GameStateManager.getInstance().getGameMode().playerShooting(); // Call the gamemode's function
    }
}
