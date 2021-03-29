package com.mygdx.game.states.game;

import static com.mygdx.game.managers.GameStateManager.*;
import static com.mygdx.game.utils.GameConstants.*;

// This state handles logic for what happens when you switch between rounds (from player 1 to player 2)
public class SwitchRound extends AbstractGameState {

    @Override
    public void startGameState() {
        GSM.time = 0;
    }

    @Override
    public void endGameState() {
        GSM.currentPlayer++; // Update current player to next player

        // Reset player counter if max limit reached
        if (GSM.currentPlayer >= GSM.players.size())
            GSM.currentPlayer = 0;

        // Fetch the new player and give that player movement control
        GSM.player = GSM.players.get(GSM.currentPlayer);
    }

    @Override
    public void update(float dt) {
        if (GSM.time > TIME_BETWEEN_ROUNDS)
            GSM.setGameState(STATE.START_ROUND);
    }
}
