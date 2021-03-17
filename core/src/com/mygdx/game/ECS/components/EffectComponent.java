package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;

public class EffectComponent implements Component {
    //PowerUpEffect effect;
    int duration;

    public EffectComponent(//PowerUpEffect effect
                           int duration) {
        //this.effect=effect;
        this.duration = duration;
    }
}
