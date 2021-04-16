package com.mygdx.game.states;

/**
 * This interface is used for controlling game logic for different game modes
 **/
public interface GameMode {
    void update(float dt);

    void startGame();

    void endGame();

    void startRound();

    void playerAim();

    void playerShooting();

    void projectileAirborne();

    void switchRound();

    void updateUI();

    void initEntities();
}
