package com.mygdx.game.gamelogic.states.game;

import static com.mygdx.game.gamelogic.states.GameStateManager.GSM;

/**
 * This state is for displaying the start of the game session
 * This will show a countdown before game starts and starts the first round
 **/
public class StartGame extends AbstractGameState {
    @Override
    public void startGameState() {
        GSM.getGameMode().startGame();// Call the gamemode's function
    }
}
