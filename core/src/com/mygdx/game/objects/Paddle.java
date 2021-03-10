package com.mygdx.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Application;
import com.mygdx.game.utils.B2DBodyBuilder;

import static com.mygdx.game.utils.B2DConstants.BIT_BALL;
import static com.mygdx.game.utils.B2DConstants.BIT_PADDLE;

import static com.mygdx.game.utils.B2DConstants.PPM;

public class Paddle {

    public Body body;
    public Vector2 position;
    public float width;
    public float height;
    public String id;

    public Paddle(World world, float x, float y, float width, float height, String id) {
        this.position = new Vector2(x / PPM, y / PPM);
        this.width = width / PPM;
        this.height = height / PPM;
        this.id = id;
        this.createBody(world);
    }

    private void createBody(World world) {
        this.body = B2DBodyBuilder.createBox(
                world,
                this.position.x,
                this.position.y,
                this.width,
                this.height,
                (short) BIT_PADDLE,
                (short) BIT_BALL,
                this.id);
    }

    public void move(float y) {
        this.position.add(0f, y);
        if (this.position.y + this.height < Application.V_HEIGHT / PPM) {
            this.body.setTransform(this.position, 0f);
        } else if (this.position.y - this.height > 0f) {
            this.body.setTransform(this.position, 0f);
        }
    }
}
