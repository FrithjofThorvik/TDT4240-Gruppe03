package com.mygdx.game.gamelogic.states.game;


import com.mygdx.game.gamelogic.states.GameStateManager;


/**
 * This state is for cleaning up, and displaying game ending
 **/
public class EndGame extends AbstractGameState {
    @Override
    public void startGameState() {
        GameStateManager.getInstance().getGameMode().endGame(); // Call the gamemode's function
    }
}
