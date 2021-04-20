package com.mygdx.game.gamelogic.states.game;

import static com.mygdx.game.gamelogic.states.GameStateManager.GSM;


/**
 * When it is currently a players turn, the game is in this state
 **/
public class StartRound extends AbstractGameState {

    @Override
    public void startGameState() {
        GSM.getGameMode().startRound();// Call the gamemode's function
    }
}
