package com.mygdx.game.states.game;

import com.badlogic.ashley.core.Entity;

import static com.mygdx.game.managers.GameStateManager.GSM;

public class PlayerAim extends AbstractGameState {
    Entity player;

    @Override
    public void startGameState() {
        if (GSM.players.size() > 0) {
            player = GSM.players.get(GSM.currentPlayer);
            // TODO: Add AimComponent
        }
    }

    @Override
    public void endGameState() {
        // TODO: Remove AimComponent
    }

    @Override
    public void update(float dt) {
        // TODO: Update AimComponent
        // TODO: Update PowerBarComponent
    }
}
