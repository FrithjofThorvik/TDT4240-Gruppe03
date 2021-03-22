package com.mygdx.game.ECS.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {//Contains the (x,y) coordinate of a entity
   public Vector2 position;

    public PositionComponent(float x, float y) {
        this.position=new Vector2(x,y);
    }
}
