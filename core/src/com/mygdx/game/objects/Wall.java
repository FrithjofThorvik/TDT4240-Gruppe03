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

public class Wall {

    public Body body;
    public Vector2[] wall;
    public String id;

    public Wall(World world, float x1, float y1, float x2, float y2, String id) {
        this.wall = new Vector2[2];

        this.wall[0] = new Vector2(x1 / PPM, y1 / PPM);
        this.wall[1] = new Vector2(x2 / PPM, y2 / PPM);
        this.id = id;
        this.createBody(world);
    }

    private void createBody(World world) {
        this.body = B2DBodyBuilder.createWall(
                world,
                this.wall,
                (short) (BIT_GOAL | BIT_WALL),
                (short) BIT_BALL,
                this.id);
    }
}
