package com.mygdx.game.ECS.components.misc;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.gamelogic.states.screens.GameScreen;

import static com.mygdx.game.utils.B2DConstants.PPM;


/**
 * Every entity that should answer to physics, should have this component
 * The body property will be used to apply impulses and decide velocity
 * The fixture property will be used in the CollisionSystem class
 **/
public class Box2DComponent implements Component {
    public Body body;
    public Fixture fixture;

    public Box2DComponent(Vector2 pos, Vector2 spriteSize, Boolean staticBody, float mass, short cBits, short mBits) {
        // Create box2D BodyDef
        BodyDef bodyDef = new BodyDef();

        if (staticBody) bodyDef.type = BodyDef.BodyType.StaticBody;
        else bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(pos.x / PPM, pos.y / PPM); // Set position to given x & y value
        bodyDef.fixedRotation = true; // Should this body be prevented from rotating?
        this.body = GameScreen.world.createBody(bodyDef); // Create the body

        // Create PolygonShape representing a box shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((spriteSize.x / 2f) / PPM, (spriteSize.y / 2f) / PPM); // Set shape to a box with given width & height value

        // Create FixtureDef representing properties such as density, restitution, etc
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape; // Add the box shape to fixture
        fixtureDef.density = mass/(((spriteSize.x / 4f)*(spriteSize.y / 4f) / PPM)); // Add density to fixture (increases mass)
        fixtureDef.friction = 0.5f;
        fixtureDef.filter.categoryBits = cBits; // Is this category of bit
        fixtureDef.filter.maskBits = mBits; // Will collide with these bits
        this.fixture = body.createFixture(fixtureDef); // Add FixtureDef and id to body

        // Dispose shape
        shape.dispose();
    }
}