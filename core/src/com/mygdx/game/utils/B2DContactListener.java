package com.mygdx.game.utils;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.ProjectileDamageComponent;

public class B2DContactListener implements ContactListener {
    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> projectiles;
    private Array<Fixture> playerFix;
    private Array<Fixture> projectileFix;
    Engine e;

    public B2DContactListener(Engine e) {
        this.e = e;
    }

    // Called when two Fixtures collide
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
        projectiles = e.getEntitiesFor(Family.all(ProjectileDamageComponent.class).get());
        /*for (int i = 0; i < players.size(); ++i) {
            playerFix.add(players.get(i).getComponent(Box2DComponent.class).fixture);
        }
        for (int i = 0; i < projectiles.size(); ++i) {
            projectileFix.add(projectiles.get(i).getComponent(Box2DComponent.class).fixture);
        }*/

        /*if ((playerFix.contains(fixtureA,true) && projectileFix.contains(fixtureB,true)) || (playerFix.contains(fixtureB,true) && projectileFix.contains(fixtureA,true))) {
            System.out.println("Player has collided with projectile");
        }*/


    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
