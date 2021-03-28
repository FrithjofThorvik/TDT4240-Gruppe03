package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;

// This component should be added to a player when he picks up a power-up
public class EffectComponent implements Component {
    // TODO: PowerUpEffect effect;
    int duration;

    public EffectComponent(int duration) {
        // TODO: this.effect = effect;
        this.duration = duration;
    }
}
