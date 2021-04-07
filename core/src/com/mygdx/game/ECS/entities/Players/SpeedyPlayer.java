package com.mygdx.game.ECS.entities.Players;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Application;


/**
 * ...
 **/
public class SpeedyPlayer extends AbstractPlayer {
    int health = 70;
    Vector2 velocity = new Vector2(15f, 0);
    Vector2 size = new Vector2(50f, 50f);
    Vector2 position = new Vector2(Application.camera.viewportWidth / 2f, Application.camera.viewportHeight / 1.2f);
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
    public void addClassComponents() {
        // Nothing
    }
}
