package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;

public class AimComponent implements Component {
    public float angle;

    public AimComponent(float angle) {
        this.angle = angle;
    }
}
