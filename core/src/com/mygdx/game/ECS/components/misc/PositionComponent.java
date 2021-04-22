package com.mygdx.game.ECS.components.misc;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;


/**
 * Add this to entities that has a position in the world
 * Also used for synchronizing Box2D body positions (necessary for the use of Box2D physics)
 **/
public class PositionComponent implements Component {
    public Vector2 position;

    public PositionComponent(float x, float y) {
        this.position = new Vector2(x, y);
    }
}
