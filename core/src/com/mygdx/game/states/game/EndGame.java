package com.mygdx.game.states.game;


import static com.mygdx.game.managers.GameStateManager.GSM;

/**
 * This state is for cleaning up, and displaying game ending
 **/
public class EndGame extends AbstractGameState {
    @Override
    public void startGameState() {
        GSM.getGameMode().endGame(); // Display the end screen
    }
}
