package com.mygdx.game.states.game;

import static com.mygdx.game.managers.ControlManager.CM;
import static com.mygdx.game.managers.GameStateManager.GSM;


/**
 * When it is currently a players turn, the game is in this state
 **/
public class StartRound extends AbstractGameState {

    @Override
    public void startGameState() {
        CM.startMoving(); // Enable moving buttons
        GSM.time = 0; // Reset the timer when a round starts
    }

    @Override
    public void endGameState() {}

    @Override
    public void update(float dt) {}

    @Override
    public void dispose() {}
}
