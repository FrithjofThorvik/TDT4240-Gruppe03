package com.mygdx.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.utils.B2DBodyBuilder;

import static com.mygdx.game.utils.B2DConstants.BIT_BALL;
import static com.mygdx.game.utils.B2DConstants.BIT_PADDLE;
import static com.mygdx.game.utils.B2DConstants.BIT_GOAL;
import static com.mygdx.game.utils.B2DConstants.BIT_WALL;


import java.util.Random;

import static com.mygdx.game.utils.B2DConstants.PPM;

public class Ball {

    public Body body;
    public Vector2 position;
    public Vector2 velocity;
    public float radius;
    public String id;

    public Ball(World world, float pos_x, float pos_y, float radius, String id) {
        this.position = new Vector2(pos_x / PPM, pos_y / PPM);
        this.velocity = new Vector2(-5f / PPM, -4f / PPM);
        this.radius = radius / PPM;
        this.id = id;
        this.createBody(world);
    }

    private void createBody(World world) {
        this.body = B2DBodyBuilder.createBall(
                world,
                this.position.x,
                this.position.y,
                this.radius,
                (short) BIT_BALL,
                (short) (BIT_PADDLE | BIT_GOAL | BIT_WALL),
                this.id);
    }

    public void move() {
        this.position.add(velocity);
        this.body.setTransform(position, 0);
    }

    public void bounce() {
        this.velocity.y = -this.velocity.y;
    }

    public void changeDirection() {
        float rand = (float)Math.random() * 2f + 0.5f;
        this.velocity.x = -this.velocity.x;

        if (this.velocity.y > 1.5f) {
            this.velocity.y = -5f / PPM;
        } else {
            this.velocity.y = 5f / PPM;
        }

        this.velocity.y = rand * this.velocity.y;
    }
}
