package com.mygdx.game.gamelogic.states.game;


import static com.mygdx.game.gamelogic.states.GameStateManager.GSM;

/**
 * In this state the player is shooting (charging power for the shot)
 **/
public class PlayerShooting extends AbstractGameState {

    @Override
    public void startGameState() {
        GSM.getGameMode().playerShooting(); // Call the gamemode's function
    }
}
