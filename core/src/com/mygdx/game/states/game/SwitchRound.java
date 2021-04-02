package com.mygdx.game.states.game;

import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.managers.GameStateManager.STATE;
import static com.mygdx.game.utils.GameConstants.TIME_BETWEEN_ROUNDS;


/**
 * This state handles logic for what happens when you switch between rounds (from player 1 to player 2)
 **/
public class SwitchRound extends AbstractGameState {

    @Override
    public void startGameState() {
        GSM.time = 0;
    }

    @Override
    public void endGameState() {
        GSM.currentPlayer++; // Update current player to next player

        // Reset player counter if max limit reached
        if (GSM.currentPlayer >= GSM.numberOfPlayers)
            GSM.currentPlayer = 0;
    }

    @Override
    public void update(float dt) {
        if (GSM.time > TIME_BETWEEN_ROUNDS)
            GSM.setGameState(STATE.START_ROUND);
    }

    @Override
    public void dispose() {}
}
