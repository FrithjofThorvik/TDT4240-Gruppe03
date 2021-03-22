package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;

//This component should be added to a player when he picks up a powerup
public class EffectComponent implements Component {
    //PowerUpEffect effect; //this is commented out because the class PowerUpEffect does not exist yet
    int duration;

    public EffectComponent(//PowerUpEffect effect
                           int duration) {
        //this.effect=effect;
        this.duration = duration;
    }
}
