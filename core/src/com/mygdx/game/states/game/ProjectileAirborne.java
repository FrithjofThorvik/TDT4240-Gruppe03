package com.mygdx.game.states.game;

import static com.mygdx.game.managers.ControlManager.CM;
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
