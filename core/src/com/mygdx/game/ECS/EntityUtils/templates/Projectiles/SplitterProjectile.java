package com.mygdx.game.ECS.EntityUtils.templates.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.EntityUtils.strategies.Projectile.ProjectileType;
import com.mygdx.game.ECS.EntityUtils.strategies.Projectile.SplitterType;
import com.mygdx.game.ECS.components.projectiles.SplitterComponent;

/**
 * A projectile that splits -> how splendid
 **/
public class SplitterProjectile extends AbstractProjectile {
    int numOfSplits = 10;
    int damage = 15;
    float speed = 1f;
    Vector2 size = new Vector2(15f, 15f);
    Vector2 position = new Vector2(0f, 0f);
    public Texture texture = new Texture("splitter.png");
    ProjectileType type = new SplitterType(); // The projectile type indicates which algorithm the projectile follows (these algorithms determine the behaviour of a projectile)

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
        entity.add(new SplitterComponent(numOfSplits));
    }
}
