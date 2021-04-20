package com.mygdx.game.gamelogic.states.game;


import static com.mygdx.game.gamelogic.states.GameStateManager.GSM;

/**
 * This state is for cleaning up, and displaying game ending
 **/
public class EndGame extends AbstractGameState {
    @Override
    public void startGameState() {
        GSM.getGameMode().endGame(); // Call the gamemode's function
    }
}
