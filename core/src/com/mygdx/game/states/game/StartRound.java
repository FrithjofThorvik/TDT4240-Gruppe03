package com.mygdx.game.states.game;

import com.mygdx.game.ECS.components.MovementControlComponent;

import static com.mygdx.game.managers.GameStateManager.*;

// When it is currently a players turn, the game is in this state
public class StartRound extends AbstractGameState {

    @Override
    public void startGameState() {
        GSM.time = 0;
        GSM.player.add(new MovementControlComponent()); // Enable new player to move
    }

    @Override
    public void endGameState() {
        GSM.player.remove(MovementControlComponent.class); // Disable player movement
    }

    @Override
    public void update(float dt) {}
}
