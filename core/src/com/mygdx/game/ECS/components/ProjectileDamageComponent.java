package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;

// Add this component to projectiles, to indicate damage and blast radius
public class ProjectileDamageComponent implements Component {
    public int damage;
    public double speed; // How fast the projectile moves
    public int blast_radius;

    public ProjectileDamageComponent(int damage, int blast_radius, int speed) {
        this.damage = damage;
        this.speed = speed;
        this.blast_radius = blast_radius;
    }
}
