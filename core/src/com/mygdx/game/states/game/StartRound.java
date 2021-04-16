package com.mygdx.game.states.game;

import static com.mygdx.game.managers.ControlManager.CM;
import static com.mygdx.game.managers.GameStateManager.GSM;


/**
 * When it is currently a players turn, the game is in this state
 **/
public class StartRound extends AbstractGameState {

    @Override
    public void startGameState() {
        GSM.getGameMode().startRound();
    }
}
