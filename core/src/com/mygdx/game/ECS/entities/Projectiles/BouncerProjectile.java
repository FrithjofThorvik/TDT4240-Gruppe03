package com.mygdx.game.ECS.entities.Projectiles;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.components.ProjectileComponents.BouncyComponent;

import static com.mygdx.game.utils.ScaledConstants.xSmall;
/**
 * ...
 **/
public class BouncerProjectile extends AbstractProjectile {
    int numOfBounces = 2;
    int damage = 5;
    float speed = 1f;
    Vector2 size = new Vector2(xSmall, xSmall);
    Vector2 position = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight());
    Texture texture = new Texture("cannonball.png");

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
    public void addClassComponents(Entity entity) {
        entity.add(new BouncyComponent(numOfBounces));
    }
}
