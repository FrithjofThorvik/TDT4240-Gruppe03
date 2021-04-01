package com.mygdx.game.ECS;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.HealthComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.ProjectileDamageComponent;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameStateManager;

import static  com.mygdx.game.managers.GameStateManager.GSM;


/**
 * This system should control a projectile after it has been created/fired
 * TODO: Implement better functionality for knowing what player should be damaged
 * TODO: Implement blast radius damage
 * TODO: Implement functionality for handling multiple projectiles at the same time (only takes first element)
 **/
public class CollisionHandler implements ContactListener {
    private final Engine engine;

    // Prepare arrays for entities
    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> projectiles;

    // Prepare component mappers
    private final ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);
    private final ComponentMapper<FontComponent> fm = ComponentMapper.getFor(FontComponent.class);
    private final ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);
    private final ComponentMapper<ProjectileDamageComponent> pdm = ComponentMapper.getFor(ProjectileDamageComponent.class);

    public CollisionHandler(Engine engine) {
        this.engine = engine;
        this.getEntities();
    }

    // Will be called automatically by the engine
    private void getEntities() {
        this.players = this.engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
        this.projectiles = this.engine.getEntitiesFor(Family.all(ProjectileDamageComponent.class).get());
    }

    @Override
    public void beginContact(Contact contact) {
        Array<Entity> enemies = new Array<Entity>(); // Array for storing players that aren't the current player
        Array<Fixture> playerFixtures = new Array<Fixture>(); // Array for all active players
        Array<Fixture> projectileFixtures = new Array<Fixture>(); // Array for all active projectiles
        Array<Fixture> groundFixtures = new Array<Fixture>(); // Array for all ground components
        Array<Fixture> collisionFixtures = new Array<Fixture>(); // Array for collision event

        // Get fixture instances that collide
        collisionFixtures.add(contact.getFixtureA());
        collisionFixtures.add(contact.getFixtureB());

        groundFixtures.add(b2dm.get(EntityManager.ground).fixture); // Add ground fixture to array

        // Loop through all entities, and add fixture property to array for comparison
        for (int i = 0; i < this.players.size(); i++) { // Get fixtures for all player entities
            playerFixtures.add(b2dm.get(this.players.get(i)).fixture);

            if (i != GSM.currentPlayer) enemies.add(players.get(i)); // Add enemies
        }

        for (int i = 0; i < this.projectiles.size(); i++) { // Get fixtures for all projectile instances
            projectileFixtures.add(b2dm.get(this.projectiles.get(i)).fixture);
        }

        // Check if players collide
        if (playerFixtures.containsAll(collisionFixtures, true)) {

            Entity currentPlayer = this.players.get(GSM.currentPlayer);  // Get player entity
            Entity enemyPlayer = enemies.get(0); // Get enemy entity

            // Get player components
            HealthComponent playerHealth = this.hm.get(currentPlayer);
            HealthComponent enemyHealth = this.hm.get(enemyPlayer);
            FontComponent playerFont;
            FontComponent enemyFont;
            
            if (GSM.currentPlayer == 0) {
                playerFont = this.fm.get(EntityManager.health1);
                enemyFont = this.fm.get(EntityManager.health2);
            } else {
                playerFont = this.fm.get(EntityManager.health2);
                enemyFont = this.fm.get(EntityManager.health1);
            }

            // Modify player components
            playerHealth.hp -= 10; // TODO: Make dynamic (add damage component to player)
            enemyHealth.hp -= 15; // TODO: Make dynamic (add damage component to player)

            // Update font text and rendering center
            playerFont.text = playerHealth.hp + " hp";
            enemyFont.text = enemyHealth.hp + " hp";
            playerFont.layout = new GlyphLayout(playerFont.font, playerFont.text);
            enemyFont.layout = new GlyphLayout(enemyFont.font, enemyFont.text);
        }

        // Check if projectile collides with a player entity
        else if (projectileFixtures.containsAny(collisionFixtures, true) &&
                playerFixtures.containsAny(collisionFixtures, true)) {

            Entity currentPlayer = this.players.get(GSM.currentPlayer);  // Get player entity
            Entity enemyPlayer = enemies.get(0); // Get enemy entity
            Entity projectile = this.projectiles.get(0); // Get projectile entity

            // Get player components
            HealthComponent playerHealth = this.hm.get(currentPlayer);
            HealthComponent enemyHealth = this.hm.get(enemyPlayer);
            FontComponent playerFont;
            FontComponent enemyFont;

            if (GSM.currentPlayer == 0) {
                playerFont = this.fm.get(EntityManager.health1);
                enemyFont = this.fm.get(EntityManager.health2);
            } else {
                playerFont = this.fm.get(EntityManager.health2);
                enemyFont = this.fm.get(EntityManager.health1);
            }

            // Modify player components
            enemyHealth.hp -= this.pdm.get(projectile).damage; // TODO: Make dynamic (add damage component to player)

            // Update font text and rendering center
            playerFont.text = playerHealth.hp + " hp";
            enemyFont.text = enemyHealth.hp + " hp";
            playerFont.layout = new GlyphLayout(playerFont.font, playerFont.text);
            enemyFont.layout = new GlyphLayout(enemyFont.font, enemyFont.text);

            this.removeProjectile(projectile); // Remove projectile, resume timer, change state
        }

        // Check if projectile hits ground entity
        else if (projectileFixtures.containsAny(collisionFixtures, true) &&
                groundFixtures.containsAny(collisionFixtures, true)) {

            Entity projectile = this.projectiles.get(0); // Get projectile
            this.removeProjectile(projectile); // Remove projectile, resume timer, change state
        }
    }

    // Remove all projectile components, resume timer, change game state
    private void removeProjectile(Entity projectile) {
        projectile.removeAll(); // Remove projectile
        GSM.setGameState(GameStateManager.STATE.SWITCH_ROUND); // Switch game state
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
