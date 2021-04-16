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
        GSM.getGameMode().switchRound();
    }
}
