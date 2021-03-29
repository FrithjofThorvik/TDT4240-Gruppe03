package com.mygdx.game.utils;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.GroundComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.ProjectileDamageComponent;

import static com.mygdx.game.managers.GameStateManager.*;

public class B2DContactListener implements ContactListener {
    Engine engine;

    public B2DContactListener(Engine engine) {
        this.engine = engine;
    }

    // Called when two Fixtures collide
    @Override
    public void beginContact(Contact contact) {
        // Local arrays for storing entities and fixtures
        ImmutableArray<Entity> players; // Array for player entities
        ImmutableArray<Entity> grounds; // Array for ground entities
        ImmutableArray<Entity> projectiles; // Array for projectile entities

        Array<Fixture> playerFixtures = new Array<Fixture>(); // Array for all active players
        Array<Fixture> groundFixtures = new Array<Fixture>(); // Array for all ground elements
        Array<Fixture> projectileFixtures = new Array<Fixture>(); // Array for all active projectiles
        Array<Fixture> collisionFixtures = new Array<Fixture>(); // Array for collision event

        // Get fixture instances that collide
        collisionFixtures.add(contact.getFixtureA());
        collisionFixtures.add(contact.getFixtureB());

        // Store all instances of players and projectiles in world
        players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
        grounds = engine.getEntitiesFor(Family.all(GroundComponent.class).get());
        projectiles = engine.getEntitiesFor(Family.all(ProjectileDamageComponent.class).get());

        // Get fixtures for all player entities
        for (int i = 0; i < players.size(); i++) {
            playerFixtures.add(players.get(i).getComponent(Box2DComponent.class).fixture);
        }

        // Get fixtures for all projectile instances
        for (int i = 0; i < projectiles.size(); i++) {
            projectileFixtures.add(projectiles.get(i).getComponent(Box2DComponent.class).fixture);
        }

        // Get fixtures for all ground instances
        for (int i = 0; i < grounds.size(); i++) {
            groundFixtures.add(grounds.get(i).getComponent(Box2DComponent.class).fixture);
        }

        // Check if players collide
        if (playerFixtures.containsAll(collisionFixtures, true)) {
            System.out.println("💥 >>> 🧙🧙");
            GSM.projectile.removeAll();
        }

        // Check if projectile collides with a ground
        if (projectileFixtures.containsAny(collisionFixtures, true) &&
                groundFixtures.containsAny(collisionFixtures, true)) {
            System.out.println("💥 >>> 🏔🏔");
            GSM.projectile.removeAll();
        }

        // Check if projectile collides with a player
        if (projectileFixtures.containsAny(collisionFixtures, true) &&
                playerFixtures.containsAny(collisionFixtures, true)) {
            System.out.println("💥 >>> 🚀🧙");
            GSM.projectile.removeAll();
        }
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
