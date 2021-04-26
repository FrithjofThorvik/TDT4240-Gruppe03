package com.mygdx.game.gamelogic.states.game;

import com.mygdx.game.gamelogic.states.GameStateManager;



/**
 * When it is currently a players turn, the game is in this state
 **/
public class StartRound extends AbstractGameState {

    @Override
    public void startGameState() {
        GameStateManager.getInstance().getGameMode().startRound();// Call the gamemode's function
    }
}
