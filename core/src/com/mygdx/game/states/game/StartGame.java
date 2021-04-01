package com.mygdx.game.states.game;

import com.mygdx.game.managers.GameStateManager;

import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.utils.GameConstants.START_GAME_TIME;

/**
 * This state is for displaying the start of the game session
 * This will show a countdown before game starts and starts the first round
 **/
public class StartGame extends AbstractGameState {
    @Override
    public void startGameState() { GSM.time = 0; }

    @Override
    public void endGameState() {}

    @Override
    public void update(float dt) {
        if (GSM.time > START_GAME_TIME)
            GSM.setGameState(GameStateManager.STATE.START_ROUND);
    }

    @Override
    public void dispose() {}
}
