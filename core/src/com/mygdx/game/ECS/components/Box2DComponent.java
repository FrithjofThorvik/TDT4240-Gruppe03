package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.states.screens.GameScreen;

//Every player should have this component, indicating amount of HealthPoints
public class Box2DComponent implements Component {
    public Body body;
    public Fixture fixture;

    public Box2DComponent(Vector2 pos, Vector2 spriteSize, Boolean staticBody, float mass) {
        // Create box2D BodyDef
        BodyDef bodyDef = new BodyDef();

        if (staticBody) bodyDef.type = BodyDef.BodyType.StaticBody;
        else bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(pos.x, pos.y); // Set position to given x & y value
        bodyDef.fixedRotation = true; // Should this body be prevented from rotating?
        this.body = GameScreen.world.createBody(bodyDef); // Create the body

        // Create PolygonShape representing a box shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(spriteSize.x / 2f, spriteSize.y / 2f); // Set shape to a box with given width & height value

        // Create FixtureDef representing properties such as density, restitution, etc
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape; // Add the box shape to fixture
        fixtureDef.density = mass / 1000; // Add density to fixture (increases mass)
        fixtureDef.friction = 0.5f;
        this.fixture = body.createFixture(fixtureDef); // Add FixtureDef and id to body

        // Dispose shape
        shape.dispose();
    }
}