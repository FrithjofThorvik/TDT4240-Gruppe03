package com.mygdx.game.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.ECS.components.CollisionComponent;

import static com.mygdx.game.managers.EntityManager.entityFixtureHashMap;


/**
 * This system should add and remove a collision component to colliding entities
 **/
public class CollisionHandler implements ContactListener {

    @Override
    public void beginContact(Contact contact) {

        // Check first if the entityFixtureHashMap contains values
        if (entityFixtureHashMap.size() > 0) {
            // Get the entities corresponding to the fixtures colliding

            Entity entity1 = entityFixtureHashMap.get(contact.getFixtureA());
            Entity entity2 = entityFixtureHashMap.get(contact.getFixtureB());

            // Add the collision component to the entities
            entity1.add(new CollisionComponent(entity2));
            entity2.add(new CollisionComponent(entity1));
        }
    }

    @Override
    public void endContact(Contact contact) {
        // Check first if the entityFixtureHashMap contains values
        if (entityFixtureHashMap.size() > 0) {
            // Get the entities corresponding to the fixtures colliding
            Entity entity1 = entityFixtureHashMap.get(contact.getFixtureA());
            Entity entity2 = entityFixtureHashMap.get(contact.getFixtureB());

            // Remove the collision component to the entities
            entity1.remove(CollisionComponent.class);
            entity2.remove(CollisionComponent.class);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
