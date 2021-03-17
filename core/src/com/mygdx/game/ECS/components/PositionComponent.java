package com.mygdx.game.ECS.components;


import com.badlogic.ashley.core.Component;

public class PositionComponent implements Component {//Contains the (x,y) coordinate of a entity
    public float x = 0.0f;
    public float y = 0.0f;

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
