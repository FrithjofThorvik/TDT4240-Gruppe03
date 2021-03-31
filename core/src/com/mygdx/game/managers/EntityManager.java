package com.mygdx.game.managers;

import com.badlogic.ashley.core.ComponentMapper;
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
import com.mygdx.game.ECS.components.RenderComponent;
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


/**
 * This class will systems and components, and takes in an engine
 **/
public class EntityManager {
    public static EntityManager EM;
    private final Engine engine;
    private final SpriteBatch batch;

    // These entities are UI elements and are static in order to be accessible everywhere
    public static Entity aimArrow;
    public static Entity powerBar;
    public static Entity powerBarArrow;
    public static Entity timer;
    public static Entity health1;
    public static Entity health2;
    public static Entity ground;

    // Preparing component mappers
    private final ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);
    private final ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);

    // Takes in an engine from Ashley (instantiate engine in GameScreen)
    // Takes in batch because the RenderSystem will draw to screen
    public EntityManager(Engine engine, SpriteBatch batch) {
        EM = this;
        this.engine = engine;
        this.batch = batch;

        // Create and add ECS systems and entities
        this.addSystems();
        this.createEntities();
        this.createEntityListeners();
    }

    // Add all ECS systems
    private void addSystems() {
        // Instantiate all ECS systems
        ControllerSystem cs = new ControllerSystem();
        RenderingSystem rs = new RenderingSystem(this.batch);
        ProjectileSystem ps = new ProjectileSystem();
        GameplaySystem gms = new GameplaySystem();
        AimingSystem ams = new AimingSystem();
        PhysicsSystem phs = new PhysicsSystem();
        ShootingSystem ss = new ShootingSystem();
        UISystem us = new UISystem();


        // Add all ECS systems to the engine
        this.engine.addSystem(cs);
        this.engine.addSystem(rs);
        this.engine.addSystem(ps);
        this.engine.addSystem(gms);
        this.engine.addSystem(ams);
        this.engine.addSystem(phs);
        this.engine.addSystem(ss);
        this.engine.addSystem(us);
    }

    // Create entities with ECS components
    private void createEntities() {
        // Instantiate all ECS entities
        Entity player1 = new Entity();
        Entity player2 = new Entity();
        timer = new Entity();
        powerBar = new Entity();
        powerBarArrow = new Entity();
        ground = new Entity();
        aimArrow = new Entity();
        health1 = new Entity();
        health2 = new Entity();

        // Textures
        Texture tankTexture = new Texture("tank.png");
        Texture powerBarTexture = new Texture("powerbar.png");
        Texture rightArrowTexture = new Texture("right-arrow.png");

        // Instantiate player entities
        player1.add(new SpriteComponent(
                        tankTexture,
                        50f,
                        50f)
                )
                .add(new PositionComponent(
                        sm.get(player1).size.x,
                        Gdx.graphics.getHeight() / 2f)
                )
                .add(new Box2DComponent(
                        pm.get(player1).position,
                        sm.get(player1).size,
                        false,
                        100f)
                )
                .add(new VelocityComponent(1000, 0))
                .add(new HealthComponent(100))
                .add(new ShootingComponent(0, 0))
                .add(new RenderComponent())
                .add(new PlayerComponent());

        player2.add(new SpriteComponent(
                        tankTexture,
                        50f,
                        50f)
                )
                .add(new PositionComponent(
                        Gdx.graphics.getWidth() - 100f,
                        Gdx.graphics.getHeight() / 2f)
                )
                .add(new Box2DComponent(
                        pm.get(player2).position,
                        sm.get(player2).size,
                        false,
                        100f)
                )
                .add(new VelocityComponent(1000, 0))
                .add(new HealthComponent(100))
                .add(new ShootingComponent(0, 0))
                .add(new RenderComponent())
                .add(new PlayerComponent());

        timer.add(new PositionComponent(
                        Gdx.graphics.getWidth() / 2f,
                        Gdx.graphics.getHeight() * 0.97f)
                )
                .add(new FontComponent("Time: 0.0s"))
                .add(new RenderComponent());

        powerBar.add(new SpriteComponent(
                        powerBarTexture,
                        40f,
                        350f)
                )
                .add(new PositionComponent(
                        Gdx.graphics.getWidth() - 50f,
                        Gdx.graphics.getHeight() / 2f)
                );

        powerBarArrow.add(new SpriteComponent(
                    rightArrowTexture,
                    40f,
                    40f)
                )
                .add(new PositionComponent(
                        Gdx.graphics.getWidth() - 70f,
                        (Gdx.graphics.getHeight() - sm.get(powerBar).size.y) / 2f)
                );

        ground.add(new SpriteComponent(
                    tankTexture,
                    Gdx.graphics.getWidth() * 2f,
                    10f)
                )
                .add(new PositionComponent(
                        Gdx.graphics.getWidth() / 2f,
                        Gdx.graphics.getHeight() / 2f)
                )
                .add(new Box2DComponent(
                        pm.get(ground).position,
                        sm.get(ground).size,
                        true,
                        1000000)
                )
                .add(new RenderComponent());

        aimArrow
                .add(new PositionComponent(
                        Gdx.graphics.getWidth() / 2f,
                        Gdx.graphics.getHeight() / 2f)
                )
                .add(new SpriteComponent(
                        rightArrowTexture,
                        10f,
                        10f)
                )
                .add(new RenderComponent());

        health1
                .add(new FontComponent(
                        hm.get(player1).hp + " hp")
                )
                .add(new PositionComponent(
                        50f,
                        Gdx.graphics.getHeight() - 20f
                ))
                .add(new RenderComponent());

        health2
                .add(new FontComponent(
                        hm.get(player2).hp + " hp")
                )
                .add(new PositionComponent(
                        Gdx.graphics.getWidth() - 50f,
                        Gdx.graphics.getHeight() - 20f
                ))
                .add(new RenderComponent());

        // Add all ECS entities to the engine
        this.engine.addEntity(player1);
        this.engine.addEntity(player2);
        this.engine.addEntity(timer);
        this.engine.addEntity(powerBar);
        this.engine.addEntity(powerBarArrow);
        this.engine.addEntity(ground);
        this.engine.addEntity(aimArrow);
        this.engine.addEntity(health1);
        this.engine.addEntity(health2);
    }

    // Add entity listeners for observe & listen to when adding and removing entities
    private void createEntityListeners() {
        // Stops the entity from moving when it loses the MovementControlComponent
        EntityListener listener = new EntityListener() {
            @Override
            public void entityRemoved(Entity entity) {
                // Set the linear velocity of the entity's box2d body to 0 in x direction
                Box2DComponent box2DComponent = b2dm.get(entity);
                box2DComponent.body.setLinearVelocity(0, box2DComponent.body.getLinearVelocity().y);
            }

            @Override
            public void entityAdded(Entity entity) {}
        };

        // The family decides which components the entity listener should listen for
        Family HasControl = Family.all(MovementControlComponent.class).get();
        this.engine.addEntityListener(HasControl, listener);
    }

    // On update, call the engines update method
    public void update(float dt) {
        engine.update(dt);
    }
}
