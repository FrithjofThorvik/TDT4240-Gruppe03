package com.mygdx.game.ECS.components.misc;

import com.badlogic.ashley.core.Component;


/**
 * This component is attached to players -> used to control shooting projectiles
 * Used in AngleSystem and ShootingSystem
 **/
public class ShootingComponent implements Component {
    public double angle; // Aim angle in radians
    public float power = 0; // Decides power of projectile shot
    public float damageMult=1;

    public ShootingComponent(double angle, float power) {
        this.angle = angle;
        this.power = power;
    }
}
