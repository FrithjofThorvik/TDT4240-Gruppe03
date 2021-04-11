package com.mygdx.game.ECS.entities.Projectiles;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.ProjectileAlgorithms.BouncyType;
import com.mygdx.game.ECS.components.ProjectileAlgorithms.ProjectileType;
import com.mygdx.game.ECS.components.ProjectileComponents.BouncyComponent;


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
    Vector2 position = new Vector2(Application.camera.viewportWidth / 2f, Application.camera.viewportHeight);
    public Texture texture = new Texture("play.png");
    ProjectileType type = new BouncyType(); // The projectile type indicates which algorithm the projectile follows (these algorithms determine the behaviour of a projectile)

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
    public void addClassComponents() { // Variable components specific for this class
        this.entity.add(new BouncyComponent(numOfBounces));

        // Give the box2d component bounciness
        entity.getComponent(Box2DComponent.class).fixture.setRestitution(bounciness);
        entity.getComponent(Box2DComponent.class).fixture.setFriction(friction);
    }
}
