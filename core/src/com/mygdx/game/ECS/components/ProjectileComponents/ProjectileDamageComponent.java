package com.mygdx.game.ECS.components.ProjectileComponents;

import com.badlogic.ashley.core.Component;
import com.mygdx.game.ECS.Projectiles.ProjectileCreator;


/**
 * Add this component to projectiles, to indicate damage and blast radius
 * Used in CollisionSystem
 **/
public class ProjectileDamageComponent implements Component {
    public int damage;
    public double speed; // How fast the projectile moves

    public ProjectileDamageComponent(int damage, int speed) {
        this.damage = damage;
        this.speed = speed;
    }
}
