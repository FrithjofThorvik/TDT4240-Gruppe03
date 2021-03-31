package com.mygdx.game.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.HealthComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.ECS.systems.AimingSystem;
import com.mygdx.game.ECS.systems.ControllerSystem;
import com.mygdx.game.ECS.systems.GameplaySystem;
import com.mygdx.game.ECS.systems.PhysicsSystem;
import com.mygdx.game.ECS.systems.ProjectileSystem;
import com.mygdx.game.ECS.systems.RenderingSystem;
import com.mygdx.game.ECS.systems.ShootingSystem;
import com.mygdx.game.ECS.systems.UISystem;

// This class will systems and components, and takes in an engine
public class EntityManager {
    private final Engine engine;
    private final SpriteBatch batch;

    // These entities are UI elements and are static in order to be accessible everywhere
    public static Entity aimArrow;
    public static Entity powerBar;
    public static Entity powerBarArrow;
    public static Entity timer;

    // Takes in an engine from Ashley (instantiate engine in GameScreen)
    // Takes in batch because the rendering system will draw to screen
    public EntityManager(Engine engine, SpriteBatch batch) {
        this.engine = engine;
        this.batch = batch;

        // Create and add ECS systems and entities
        addSystems();
        createEntities();
        createEntityListeners();
    }

    // Add all ECS systems
    private void addSystems() {
        // Instantiate all ECS systems
        ControllerSystem cs = new ControllerSystem();
        RenderingSystem rs = new RenderingSystem(batch);
        ProjectileSystem ps = new ProjectileSystem();
        GameplaySystem gms = new GameplaySystem();
        AimingSystem ams = new AimingSystem();
        PhysicsSystem phs = new PhysicsSystem();
        ShootingSystem ss = new ShootingSystem();
        UISystem us = new UISystem();


        // Add all ECS systems to the engine
        engine.addSystem(cs);
        engine.addSystem(rs);
        engine.addSystem(ps);
        engine.addSystem(gms);
        engine.addSystem(ams);
        engine.addSystem(phs);
        engine.addSystem(ss);
        engine.addSystem(us);
    }

    // Create entities with ECS components
    private void createEntities() {
        // Instantiate all ECS entities
        Entity player1 = new Entity();
        Entity player2 = new Entity();
        timer = new Entity();
        powerBar = new Entity();
        powerBarArrow = new Entity();
        Entity ground = new Entity();
        aimArrow = new Entity();

        // Instantiate player entities
        player1.add(new VelocityComponent(1000, 0))
                .add(new SpriteComponent(new Texture("tank.png"), 50f, 50f))
                .add(new PositionComponent(0 + player1.getComponent(SpriteComponent.class).size.x,
                        Gdx.graphics.getHeight() / 2f))
                .add(new HealthComponent(100))
                .add(new RenderableComponent())
                .add(new PlayerComponent())
                .add(new ShootingComponent(0, 0))
                .add(new Box2DComponent(
                        player1.getComponent(PositionComponent.class).position,
                        player1.getComponent(SpriteComponent.class).size,
                        false, 100f));

        player2.add(new VelocityComponent(1000, 0))
                .add(new SpriteComponent(new Texture("tank.png"), 50f, 50f))
                .add(new PositionComponent(Gdx.graphics.getWidth() - 100f,
                        Gdx.graphics.getHeight() / 2f))
                .add(new HealthComponent(100))
                .add(new RenderableComponent())
                .add(new PlayerComponent())
                .add(new ShootingComponent(0, 0))
                .add(new Box2DComponent(
                        player2.getComponent(PositionComponent.class).position,
                        player2.getComponent(SpriteComponent.class).size,
                        false, 100f));

        timer.add(new FontComponent("Time: 0.0s"))
                .add(new PositionComponent(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 0.97f))
                .add(new RenderableComponent());

        powerBar.add(new SpriteComponent(new Texture("powerbar.png"), 40f, 350f))
                .add(new PositionComponent(Gdx.graphics.getWidth() - 50f, Gdx.graphics.getHeight() / 2f));

        powerBarArrow.add(new SpriteComponent(new Texture("right-arrow.png"), 40f, 40f))
                .add(new PositionComponent(
                        Gdx.graphics.getWidth() - 70f,
                        (Gdx.graphics.getHeight() - powerBar.getComponent(SpriteComponent.class).size.y) / 2f));

        ground.add(new SpriteComponent(
                new Texture("tank.png"),
                Gdx.graphics.getWidth() * 2f,
                10f))
                .add(new PositionComponent(
                        Gdx.graphics.getWidth() / 2f,
                        Gdx.graphics.getHeight() / 2f)
                )
                .add(new Box2DComponent(
                        ground.getComponent(PositionComponent.class).position,
                        ground.getComponent(SpriteComponent.class).size,
                        true, 1000000))
                .add(new RenderableComponent());

        aimArrow
                .add(new PositionComponent(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2))
                .add(new SpriteComponent(
                        new Texture("right-arrow.png"),
                        10f, 10f)
                )
                .add(new RenderableComponent());

        // Add all ECS entities to the engine
        engine.addEntity(player1);
        engine.addEntity(player2);
        engine.addEntity(timer);
        engine.addEntity(powerBar);
        engine.addEntity(powerBarArrow);
        engine.addEntity(ground);
        engine.addEntity(aimArrow);
    }

    // Add entity listeners for observe & listen to when adding and removing entities
    private void createEntityListeners() {
        // Stops the entity from moving when it loses the MovementControlComponent
        EntityListener listener = new EntityListener() {
            @Override
            public void entityRemoved(Entity entity) {
                // Set the linear velocity of the entity's box2d body to 0 in x direction
                Box2DComponent box2DComponent = entity.getComponent(Box2DComponent.class);
                box2DComponent.body.setLinearVelocity(0, box2DComponent.body.getLinearVelocity().y);
            }

            @Override
            public void entityAdded(Entity entity) {
            }
        };

        // The family decides which components the entity listener should listen for
        Family HasControl = Family.all(MovementControlComponent.class).get();
        engine.addEntityListener(HasControl, listener);
    }

    // On update, call the engines update method
    public void update() {
        engine.update(Gdx.graphics.getDeltaTime());
    }
}
