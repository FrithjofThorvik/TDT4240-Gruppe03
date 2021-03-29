package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.EntitySystem;

import static com.mygdx.game.managers.GameStateManager.GSM;

/**
 * This system controls which player has the turn
 * This system is also responsible how long the rounds are, and how much time between each round
 **/
public class GameplaySystem extends EntitySystem {
    // Run GameStateManager update functions
    public void update(float dt) {
        GSM.update(dt);
    }
}
