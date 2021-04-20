package com.mygdx.game.ECS.EntityUtils.templates.Players;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Application;


/**
 * Your standard run of the mill player
 **/
public class DefaultPlayer extends AbstractPlayer {
    int health = 100;
    Vector2 velocity = new Vector2(2f, 0);
    Vector2 size = new Vector2(50f, 50f);
    Vector2 position = new Vector2(Application.camera.viewportWidth / 2f, Application.camera.viewportHeight / 1.2f);
    Texture texture = new Texture("player1.png");


    @Override
    public int getPlayerHealth() {
        return this.health;
    }

    @Override
    public Vector2 getPlayerVelocity() {
        return this.velocity;
    }

    @Override
    public Vector2 getPlayerSize() {
        return this.size;
    }

    @Override
    public Vector2 getPlayerPosition() {
        return this.position;
    }

    @Override
    public Texture getPlayerTexture() {
        return this.texture;
    }

    @Override
    public void addClassComponents() { // Variable components specific for this class
        // Nothing
    }
}
