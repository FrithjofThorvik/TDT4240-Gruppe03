package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;

public class VelocityComponent implements Component {//Attach to entity to give it a velocity
    public float velocity = 0.0f;

    public VelocityComponent(float velocity) {
        this.velocity = velocity;
    }
}
