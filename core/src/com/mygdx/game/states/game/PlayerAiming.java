package com.mygdx.game.states.game;


import static com.mygdx.game.managers.ControlManager.CM;

/**
 * This is a state responsible for indicating that a player is aiming
 **/
public class PlayerAiming extends AbstractGameState {
    @Override
    public void startGameState() {
        CM.startShooting(); // Enable shooting button
    }

    @Override
    public void endGameState() {}

    @Override
    public void update(float dt) {}

    @Override
    public void dispose() {}
}
