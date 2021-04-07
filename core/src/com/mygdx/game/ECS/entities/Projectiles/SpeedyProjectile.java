package com.mygdx.game.ECS.entities.Projectiles;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.components.ProjectileAlgorithms.ProjectileType;
import com.mygdx.game.ECS.components.ProjectileAlgorithms.StandardType;

/**
 * ...
 **/
public class SpeedyProjectile extends AbstractProjectile {

    int damage = 5;
    float speed = 1.5f;
    Vector2 size = new Vector2(10f, 10f);
    Vector2 position = new Vector2(0f, 0f);
    Texture texture = new Texture("cannonball.png");
    ProjectileType type = new StandardType();

    @Override
    public int setDamage() {
        return damage;
    }

    @Override
    public float setSpeed() {
        return speed;
    }

    @Override
    public Vector2 setSize() {
        return size;
    }

    @Override
    public Vector2 setPosition() {
        return position;
    }

    @Override
    public Texture setTexture() {
        return texture;
    }

    @Override
    public ProjectileType setType() {
        return type;
    }

    @Override
    public void addClassComponents() {
        // Nothing
    }
}
