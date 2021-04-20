package com.mygdx.game.ECS.components.projectiles;

import com.badlogic.ashley.core.Component;
import com.mygdx.game.ECS.EntityUtils.strategies.Projectile.ProjectileType;


/**
 * Add this component to projectiles, to indicate damage, speed and projectile type
 **/
public class ProjectileComponent implements Component {
    public int damage;
    public float speed; // How fast the projectile moves
    public boolean midAirReached = false; // Needed to activate midAir function of a projectile
    public ProjectileType projectileType;

    public ProjectileComponent(int damage, float speed,ProjectileType type) {
        this.damage = damage;
        this.speed = speed;
        this.projectileType = type;
    }
}
