package com.mygdx.game.ECS.entities.Projectiles;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import static com.mygdx.game.utils.ScaledConstants.xSmall;
/**
 * ...
 **/
public class DefaultProjectile extends AbstractProjectile {

    int damage = 5;
    float speed = 1f;
    Vector2 size = new Vector2(xSmall, xSmall);
    Vector2 position = new Vector2(0f, 0f);
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
        // Nothing
    }
}
