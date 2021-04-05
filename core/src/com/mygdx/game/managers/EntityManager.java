package com.mygdx.game.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.ECS.components.CollisionComponent;
import com.mygdx.game.ECS.components.EffectComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.ParentComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileComponent;
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
import com.mygdx.game.ECS.systems.MovementSystem;
import com.mygdx.game.ECS.systems.ShootingSystem;
import com.mygdx.game.ECS.systems.CollisionSystem;
import com.mygdx.game.ECS.systems.GamePlaySystem;
import com.mygdx.game.ECS.systems.PhysicsSystem;
import com.mygdx.game.ECS.systems.PowerUpSystem;
import com.mygdx.game.ECS.systems.ProjectileSystem;
import com.mygdx.game.ECS.systems.RenderingSystem;
import com.mygdx.game.ECS.systems.UISystem;

import static com.mygdx.game.managers.GameStateManager.GSM;

import java.util.HashMap;


/**
 * This class will systems and components, and takes in an engine
 **/
public class EntityManager {
    public static EntityManager EM;

    public final Engine engine;
    private final SpriteBatch batch;

    // Entity listeners
    private EntityListener movementControlListener;
    private EntityListener box2DComponentListener;

    // Entity systems
    private MovementSystem movementSystem;
    private AimingSystem aimingSystem;
    private CollisionSystem collisionSystem;
    private RenderingSystem renderingSystem;
    private ProjectileSystem projectileSystem;
    private GamePlaySystem gameplaySystem;
    private PowerUpSystem powerUpSystem;
    private PhysicsSystem physicsSystem;
    private ShootingSystem shootingSystem;
    private UISystem userInterfaceSystem;

    // These entities are UI elements and are static in order to be accessible everywhere
    public static Entity player1;
    public static Entity player2;
    public static Entity health1;
    public static Entity health2;
    public static Entity aimArrow;
    public static Entity powerBar;
    public static Entity powerBarArrow;
    public static Entity timer;
    public static Entity controller;
    public static Entity ground;

    // Preparing component mappers -> the fastest way for getting entities
    public final ComponentMapper<Box2DComponent> b2dMapper = ComponentMapper.getFor(Box2DComponent.class);
    public final ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    public final ComponentMapper<SpriteComponent> spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
    public final ComponentMapper<HealthComponent> healthMapper = ComponentMapper.getFor(HealthComponent.class);
    public final ComponentMapper<ParentComponent> parentMapper = ComponentMapper.getFor(ParentComponent.class);
    public final ComponentMapper<ShootingComponent> shootingMapper = ComponentMapper.getFor(ShootingComponent.class);
    public final ComponentMapper<ProjectileComponent> projectileMapper = ComponentMapper.getFor(ProjectileComponent.class);
    public final ComponentMapper<CollisionComponent> collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
    public final ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);
    public final ComponentMapper<EffectComponent> effectMapper = ComponentMapper.getFor(EffectComponent.class);
    public final ComponentMapper<FontComponent> fontMapper = ComponentMapper.getFor(FontComponent.class);

    //Tuple for storing entity/fixture pair
    public static HashMap<Fixture, Entity> entityFixtureHashMap = new HashMap<Fixture, Entity>();

    // Textures
    Texture tankTexture = new Texture("tank.png");
    Texture powerBarTexture = new Texture("powerbar.png");
    Texture rightArrowTexture = new Texture("right-arrow.png");

    // Takes in an engine from Ashley (instantiate engine in GameScreen)
    // Takes in batch because the RenderSystem will draw to screen
    public EntityManager(Engine engine, SpriteBatch batch) {
        EM = this;
        this.engine = engine;
        this.batch = batch;

        // Create ECS entity listeners -> has to be done before createEntities to function correctly
        this.createEntityListeners();

        // Create and add ECS systems and entities
        this.addSystems();
        this.createEntities();
    }

    // Add all ECS systems
    private void addSystems() {
        // Instantiates all ECS systems
        this.movementSystem = new MovementSystem();
        this.aimingSystem = new AimingSystem();
        this.collisionSystem = new CollisionSystem();
        this.renderingSystem = new RenderingSystem(this.batch);
        this.projectileSystem = new ProjectileSystem();
        this.gameplaySystem = new GamePlaySystem();
        this.powerUpSystem = new PowerUpSystem();
        this.physicsSystem = new PhysicsSystem();
        this.shootingSystem = new ShootingSystem();
        this.userInterfaceSystem = new UISystem();

        // Add all ECS systems to the engine
        this.engine.addSystem(this.movementSystem);
        this.engine.addSystem(this.aimingSystem);
        this.engine.addSystem(this.collisionSystem);
        this.engine.addSystem(this.renderingSystem);
        this.engine.addSystem(this.projectileSystem);
        this.engine.addSystem(this.gameplaySystem);
        this.engine.addSystem(this.powerUpSystem);
        this.engine.addSystem(this.physicsSystem);
        this.engine.addSystem(this.shootingSystem);
        this.engine.addSystem(this.userInterfaceSystem);

    }

    // Create entities with ECS components
    private void createEntities() {
        // Instantiate all ECS entities
        player1 = new Entity();
        player2 = new Entity();
        health1 = new Entity();
        health2 = new Entity();

        // Instantiate all UI entities
        timer = new Entity();
        controller = new Entity();
        powerBar = new Entity();
        powerBarArrow = new Entity();
        ground = new Entity();
        aimArrow = new Entity();

        // Instantiate player entities
        player1.add(new SpriteComponent(
                this.tankTexture,
                50f,
                50f)
        )
                .add(new PositionComponent(
                        spriteMapper.get(player1).size.x,
                        Gdx.graphics.getHeight() / 1.2f)
                )
                .add(new Box2DComponent(
                        positionMapper.get(player1).position,
                        spriteMapper.get(player1).size,
                        false,
                        100f)
                )
                .add(new VelocityComponent(1000, 0))
                .add(new HealthComponent(100))
                .add(new ShootingComponent(0, 0))
                .add(new RenderComponent())
                .add(new PlayerComponent());

        player2.add(new SpriteComponent(
                this.tankTexture,
                50f,
                50f)
        )
                .add(new PositionComponent(
                        Gdx.graphics.getWidth() - 100f,
                        Gdx.graphics.getHeight() / 1.2f)
                )
                .add(new Box2DComponent(
                        positionMapper.get(player2).position,
                        spriteMapper.get(player2).size,
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
                this.powerBarTexture,
                40f,
                350f)
        )
                .add(new PositionComponent(
                        Gdx.graphics.getWidth() - 50f,
                        Gdx.graphics.getHeight() / 2f)
                );

        powerBarArrow.add(new SpriteComponent(
                this.rightArrowTexture,
                40f,
                40f)
        )
                .add(new PositionComponent(
                        Gdx.graphics.getWidth() - 70f,
                        (Gdx.graphics.getHeight() - spriteMapper.get(powerBar).size.y) / 2f)
                );

        ground.add(new SpriteComponent(
                this.tankTexture,
                Gdx.graphics.getWidth() * 2f,
                10f)
        )
                .add(new PositionComponent(
                        Gdx.graphics.getWidth() / 2f,
                        Gdx.graphics.getHeight() / 2f)
                )
                .add(new Box2DComponent(
                        positionMapper.get(ground).position,
                        spriteMapper.get(ground).size,
                        true,
                        1000000)
                )
                .add(new RenderComponent());

        aimArrow.add(new PositionComponent(
                Gdx.graphics.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f)
        )
                .add(new SpriteComponent(
                        this.rightArrowTexture,
                        10f,
                        10f)
                );

        health1
                .add(new ParentComponent(player1))
                .add(new FontComponent(
                        healthMapper.get(parentMapper.get(health1).parent).hp + " hp")
                )
                .add(new PositionComponent(
                        50f,
                        Gdx.graphics.getHeight() - 20f
                ))
                .add(new RenderComponent());

        health2
                .add(new ParentComponent(player2))
                .add(new FontComponent(
                        healthMapper.get(parentMapper.get(health1).parent).hp + " hp")
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
        this.engine.addEntity(controller);
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
        this.movementControlListener = new EntityListener() {

            @Override
            public void entityRemoved(Entity entity) {
                // Set the linear velocity of the entity's box2d body to 0 in x direction
                Box2DComponent box2DComponent = b2dMapper.get(entity);
                box2DComponent.body.setLinearVelocity(0, box2DComponent.body.getLinearVelocity().y);
            }

            @Override
            public void entityAdded(Entity entity) {
            }
        };

        // The family decides which components the entity listener should listen for
        Family HasControl = Family.all(MovementControlComponent.class).get();
        this.engine.addEntityListener(HasControl, this.movementControlListener);

        // This should activate when a box2d component is added or removed from an entity
        this.box2DComponentListener = new EntityListener() {
            // Create a hashmap to keep track of Entities and their box2d bodies
            HashMap<Entity, Body> bodyEntityHashMap = new HashMap<Entity, Body>();


            @Override
            public void entityRemoved(Entity entity) {
                entityFixtureHashMap.remove(entity); // Remove from fixture HashMap -> needed for collision detection
                bodyEntityHashMap.get(entity).getWorld().destroyBody(bodyEntityHashMap.get(entity)); // Destroy the box2d body
            }

            @Override
            public void entityAdded(Entity entity) {
                bodyEntityHashMap.put(entity, b2dMapper.get(entity).body); // Store the body so we can destroy it later
                entityFixtureHashMap.put(b2dMapper.get(entity).fixture, entity); // Add to fixture HashMap -> needed for collision detection
            }
        };

        // The family decides which components the entity listener should listen for
        Family Box2D = Family.all(Box2DComponent.class).get();
        this.engine.addEntityListener(Box2D, box2DComponentListener);
    }

    // On update, call the engines update method
    public void update(float dt) {
        // Check if game is paused
        if (!GSM.pauseGame)
            engine.update(dt);
    }

    // Reset everything
    public void removeAll() {
        this.engine.removeEntityListener(this.movementControlListener); // Remove all listeners

        // Remove all engine systems
        this.engine.removeSystem(this.movementSystem);
        this.engine.removeSystem(this.collisionSystem);
        this.engine.removeSystem(this.renderingSystem);
        this.engine.removeSystem(this.projectileSystem);
        this.engine.removeSystem(this.gameplaySystem);
        this.engine.removeSystem(this.powerUpSystem);
        this.engine.removeSystem(this.physicsSystem);
        this.engine.removeSystem(this.shootingSystem);
        this.engine.removeSystem(this.userInterfaceSystem);

        this.engine.removeAllEntities(); // Remove all entities
    }

    // Dispose everything
    public void dispose() {
        this.tankTexture.dispose();
        this.rightArrowTexture.dispose();
        this.powerBarTexture.dispose();
    }
}
