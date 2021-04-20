package com.mygdx.game.gamelogic.states.game;

import static com.mygdx.game.managers.GameStateManager.GSM;


/**
 * This state handles logic for what happens when you switch between rounds (from player 1 to player 2)
 **/
public class SwitchRound extends AbstractGameState {

    @Override
    public void startGameState() {
        GSM.getGameMode().switchRound(); // Call the gamemode's function
    }
}
