package com.mygdx.game.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.components.AimComponent;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.HealthComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.PowerBarComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.ECS.systems.AimingSystem;
import com.mygdx.game.ECS.systems.ControllerSystem;
import com.mygdx.game.ECS.systems.GameplaySystem;
import com.mygdx.game.ECS.systems.PhysicsSystem;
import com.mygdx.game.ECS.systems.ProjectileSystem;
import com.mygdx.game.ECS.systems.RenderingSystem;

// This class will systems and components, and takes in an engine
public class EntityManager {
    private final Engine engine;
    private final SpriteBatch batch;

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

        // Add all ECS systems to the engine
        engine.addSystem(cs);
        engine.addSystem(rs);
        engine.addSystem(ps);
        engine.addSystem(gms);
        engine.addSystem(ams);
        engine.addSystem(phs);
    }

    // Create entities with ECS components
    private void createEntities() {
        // Instantiate all ECS entities
        Entity player1 = new Entity();
        Entity player2 = new Entity();
        Entity aim = new Entity();
        Entity timer = new Entity();
        Entity powerBar = new Entity();
        Entity powerArrow = new Entity();
        Entity ground = new Entity();

        // Instantiate player entities
        player1
                .add(new VelocityComponent(1000, 0))
                .add(new PositionComponent(
                        100f,
                        Gdx.graphics.getHeight() / 2f)
                )
                .add(new SpriteComponent(
                        new Texture("tank.png"),
                        player1.getComponent(PositionComponent.class).position,
                        50f,
                        50f)
                )
                .add(new Box2DComponent(
                        player1.getComponent(PositionComponent.class).position,
                        player1.getComponent(SpriteComponent.class).size,
                        false)
                )
                .add(new HealthComponent(100))
                .add(new RenderableComponent())
                .add(new PlayerComponent());

        player2
                .add(new VelocityComponent(1000, 0))
                .add(new PositionComponent(
                        Gdx.graphics.getWidth() - 100f,
                        Gdx.graphics.getHeight() / 2f)
                )
                .add(new SpriteComponent(
                        new Texture("tank.png"),
                        player2.getComponent(PositionComponent.class).position,
                        50f,
                        50f)
                )
                .add(new Box2DComponent(
                        player2.getComponent(PositionComponent.class).position,
                        player2.getComponent(SpriteComponent.class).size,
                        false)
                )
                .add(new HealthComponent(100))
                .add(new RenderableComponent())
                .add(new PlayerComponent());

        ground
                .add(new PositionComponent(
                    Gdx.graphics.getWidth() / 2f,
                    Gdx.graphics.getHeight() / 2f)
                )
                .add(new SpriteComponent(
                        new Texture("tank.png"),
                        ground.getComponent(PositionComponent.class).position,
                        Gdx.graphics.getWidth() * 2f,
                        10f))
                .add(new Box2DComponent(
                        ground.getComponent(PositionComponent.class).position,
                        ground.getComponent(SpriteComponent.class).size,
                        true))
                .add(new RenderableComponent());

        aim
                .add(new PositionComponent(0f, 0f))
                .add(new AimComponent(0.5f))
                .add(new SpriteComponent(
                        new Texture("right-arrow.png"),
                        aim.getComponent(PositionComponent.class).position,
                        10f,
                        10f)
                );

        timer
                .add(new FontComponent("Time: 0.0s"))
                .add(new PositionComponent(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 0.97f))
                .add(new RenderableComponent());

        powerBar
                .add(new PositionComponent(
                    Gdx.graphics.getWidth() - 50f,
                    Gdx.graphics.getHeight() / 2f)
                )
                .add(new SpriteComponent(
                        new Texture("powerbar.png"),
                        powerBar.getComponent(PositionComponent.class).position,
                        40f,
                        350f)
                )
                .add(new PowerBarComponent());

        powerArrow
                .add(new PositionComponent(
                    Gdx.graphics.getWidth() - 70f,
                    (Gdx.graphics.getHeight() - powerBar.getComponent(SpriteComponent.class).size.y) / 2f)
                )
                .add(new SpriteComponent(
                        new Texture("right-arrow.png"),
                        powerArrow.getComponent(PositionComponent.class).position,
                        40f,
                        40f)
                )
                .add(new PowerBarComponent());

        // Add all ECS entities to the engine
        engine.addEntity(player1);
        engine.addEntity(player2);
        engine.addEntity(ground);
        engine.addEntity(aim);
        engine.addEntity(timer);
        engine.addEntity(powerBar);
        engine.addEntity(powerArrow);
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
            public void entityAdded(Entity entity) {}
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
