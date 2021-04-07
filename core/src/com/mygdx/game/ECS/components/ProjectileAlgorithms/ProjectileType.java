package com.mygdx.game.ECS.components.ProjectileAlgorithms;


import com.badlogic.ashley.core.Entity;

public interface ProjectileType {
    // What happens when the projectile collides with something
    void collision(Entity projectile);

    // Implements functionality for when the projectile reaches peak height
    void midAir(Entity projectile);
}
