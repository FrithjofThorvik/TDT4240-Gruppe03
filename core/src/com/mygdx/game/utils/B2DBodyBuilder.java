package com.mygdx.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.game.utils.B2DConstants.PPM;

public class B2DBodyBuilder {

    private B2DBodyBuilder() {}

    public static Body createBox(World world, float x, float y, float width, float height, short cBits, short mBits, String id) {
        Body body;

        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set(x, y);
        body = world.createBody(bDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.filter.categoryBits = cBits;  // Is Category Bit
        fDef.filter.maskBits = mBits;        // Collides With Category Bit
        body.createFixture(fDef).setUserData(id);
        shape.dispose();

        return body;
    }

    public static Body createBall(World world, float x, float y, float radius, short cBits, short mBits, String id) {
        Body body;
        BodyDef bDef = new BodyDef();

        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(x, y);

        body = world.createBody(bDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.filter.categoryBits = cBits;
        fDef.filter.maskBits = mBits;
        body.createFixture(fDef).setUserData(id);
        shape.dispose();

        return body;
    }

    public static Body createWall(World world, Vector2[] wall, short cBits, short mBits, String id) {

        // Body
        Body body;
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody( bDef);

        ChainShape shape = new ChainShape();
        shape.createChain(wall);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.filter.categoryBits = cBits;
        fDef.filter.maskBits = mBits;
        body.createFixture(fDef).setUserData(id);
        shape.dispose();

        return body;
    }
}
