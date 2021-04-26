package com.mygdx.game.gamelogic.states.game;


import com.mygdx.game.gamelogic.states.GameStateManager;

/**
 * This state is for when projectiles are in air
 **/
public class ProjectileAirborne extends AbstractGameState {
    @Override
    public void startGameState() {
        GameStateManager.getInstance().getGameMode().projectileAirborne();// Call the gamemode's function
    }
}
