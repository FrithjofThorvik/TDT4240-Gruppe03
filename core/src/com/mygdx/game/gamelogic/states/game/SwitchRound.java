package com.mygdx.game.gamelogic.states.game;


import com.mygdx.game.gamelogic.states.GameStateManager;

/**
 * This state handles logic for what happens when you switch between rounds (from player 1 to player 2)
 **/
public class SwitchRound extends AbstractGameState {

    @Override
    public void startGameState() {
        GameStateManager.getInstance().getGameMode().switchRound(); // Call the gamemode's function
    }
}
