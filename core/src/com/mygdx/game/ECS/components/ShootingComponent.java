package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;

public class ShootingComponent implements Component {
    public double angle; // Aim angle in radians
    public float power = 0; // Decides power of projectile shot

    public ShootingComponent(double angle, float power) {
        this.angle = angle;
        this.power = power;
    }
}
