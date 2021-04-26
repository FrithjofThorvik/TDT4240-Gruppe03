package com.mygdx.game.ECS.EntityUtils.templates.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.components.misc.Box2DComponent;
import com.mygdx.game.ECS.EntityUtils.strategies.Projectile.BouncyType;
import com.mygdx.game.ECS.EntityUtils.strategies.Projectile.ProjectileType;
import com.mygdx.game.ECS.components.projectiles.BouncyComponent;


/**
 * This projectile can bounce :)
 **/
public class BouncerProjectile extends AbstractProjectile {
    int numOfBounces = 3;
    float bounciness = 0.6f;
    float friction = 0.01f;
    int damage = 5;
    float speed = 1f;
    Vector2 size = new Vector2(10f, 10f);
    Vector2 position = new Vector2(0f, 0f);
    public Texture texture = new Texture("bouncer.png");
    ProjectileType type = new BouncyType(); // The projectile type indicates which algorithm the projectile follows (these algorithms determine the behaviour of a projectile)

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public Vector2 getSize() {
        return size;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public ProjectileType getType() {
        return type;
    }

    @Override
    public void addClassComponents() { // Variable components specific for this class
        this.entity.add(new BouncyComponent(numOfBounces));

        // Give the box2d component bounciness
        entity.getComponent(Box2DComponent.class).fixture.setRestitution(bounciness);
        entity.getComponent(Box2DComponent.class).fixture.setFriction(friction);
    }
}
