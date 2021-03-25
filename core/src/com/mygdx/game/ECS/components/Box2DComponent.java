package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

//Every player should have this component, indicating amount of HealthPoints
public class Box2DComponent implements Component {
    public Body body;

    public Box2DComponent(World world, Vector2 pos, Vector2 spriteSize) {
        // Create box2D BodyDef
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos.x, pos.y); // Set position to given x & y value
        body = world.createBody(bodyDef); // Create the body

        // Create PolygonShape representing a box shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(spriteSize.x / 2f, spriteSize.y / 2f); // Set shape to a box with given width & height value

        // Create FixtureDef representing properties such as density, restitution, etc
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape; // Add the box shape to fixture
        fixtureDef.density = 0.1f; // Add density to fixture (increases mass)
        body.createFixture(fixtureDef); // Add FixtureDef to body

        // Dispose shape
        shape.dispose();
    }
}