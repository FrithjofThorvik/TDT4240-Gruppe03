package com.mygdx.game.gamelogic.states.game;

import static com.mygdx.game.managers.GameStateManager.GSM;


/**
 * This state is for when projectiles are in air
 **/
public class ProjectileAirborne extends AbstractGameState {
    @Override
    public void startGameState() {
        GSM.getGameMode().projectileAirborne();// Call the gamemode's function
    }
}
