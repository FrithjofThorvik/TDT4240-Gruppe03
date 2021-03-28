package com.mygdx.game.states.game;

public abstract class AbstractGameState {
    public abstract void startGameState();
    public abstract void endGameState();
    public abstract void update(float dt);
}
