package com.mygdx.game.utils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.objects.Ball;

public class B2DContactListener implements ContactListener {

    // Stores the object for manipulating it on collision
    private Ball ball;

    public B2DContactListener(Ball ball) {
        this.ball = ball;
    }

    // Called when two Fixtures collide
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() == "paddle") {
            ball.changeDirection();
        } else if (fixtureA.getUserData() == "playerGoal"){
            System.out.println("YOU LOST!!!");
            ball.changeDirection();
        } else if (fixtureA.getUserData() == "enemyGoal") {
            System.out.println("YOU WON!!!");
            ball.changeDirection();
        }
        else if (fixtureA.getUserData() == "wall") {
            ball.bounce();
        }
    }

    @Override public void endContact(Contact contact){}

    @Override public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override public void postSolve(Contact contact, ContactImpulse impulse) {}
}
