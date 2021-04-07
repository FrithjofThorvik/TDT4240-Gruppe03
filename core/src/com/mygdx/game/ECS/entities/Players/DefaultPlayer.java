package com.mygdx.game.ECS.entities.Players;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import static com.mygdx.game.utils.ScaledConstants.medium;


/**
 * ...
 **/
public class DefaultPlayer extends AbstractPlayer {
    int health = 100;
    Vector2 velocity = new Vector2(0.5f, 0);
    Vector2 size = new Vector2(medium, medium);
    Vector2 position = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 1.2f);
    Texture texture = new Texture("tank.png");


    @Override
    public int setPlayerHealth() {
        return this.health;
    }

    @Override
    public Vector2 setPlayerVelocity() {
        return this.velocity;
    }

    @Override
    public Vector2 setPlayerSize() {
        return this.size;
    }

    @Override
    public Vector2 setPlayerPosition() {
        return this.position;
    }

    @Override
    public Texture setPlayerTexture() {
        return this.texture;
    }

    @Override
    public void addClassComponents(Entity entity) {
        // Nothing
    }
}
