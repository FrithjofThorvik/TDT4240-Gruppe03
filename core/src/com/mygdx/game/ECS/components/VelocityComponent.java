package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

//Attach to entity to give it a velocity
public class VelocityComponent implements Component {
    public Vector2 velocity;

    public VelocityComponent(float x, float y) {
        this.velocity = new Vector2(x, y);
    }
}
