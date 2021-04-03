package com.mygdx.game.ECS.components.ProjectileComponents;

import com.badlogic.ashley.core.Component;
import com.mygdx.game.ECS.Projectiles.Projectile;


/**
 * Add this component to projectiles, to indicate damage, speed and projectile type
 **/
public class ProjectileComponent implements Component {
    public int damage;
    public double speed; // How fast the projectile moves
    public Projectile projectileType;
    public boolean midAirReached = false; // Needed to activate midAir function of a projectile

    public ProjectileComponent(int damage, int speed, Projectile projectileType) {
        this.damage = damage;
        this.speed = speed;
        this.projectileType = projectileType;
    }
}
