package com.mygdx.game.states;

public interface Mode {
    void update(float dt);

    void startGame();

    void endGame();

    void startRound();

    void playerAim();

    void playerShooting();

    void projectileAirborne();

    void switchRound();

    void updateUI();
}
