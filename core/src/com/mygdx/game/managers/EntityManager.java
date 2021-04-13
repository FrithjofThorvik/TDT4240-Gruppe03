package com.mygdx.game.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.Application;
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
import com.mygdx.game.ECS.entities.EntityCreator;
import com.mygdx.game.ECS.systems.AimingSystem;
import com.mygdx.game.ECS.systems.MapSystem;
import com.mygdx.game.ECS.systems.MovementSystem;
import com.mygdx.game.ECS.systems.ShootingSystem;
import com.mygdx.game.ECS.systems.CollisionSystem;
import com.mygdx.game.ECS.systems.GamePlaySystem;
import com.mygdx.game.ECS.systems.PhysicsSystem;
import com.mygdx.game.ECS.systems.PowerUpSystem;
import com.mygdx.game.ECS.systems.ProjectileSystem;
import com.mygdx.game.ECS.systems.RenderingSystem;
import com.mygdx.game.ECS.systems.UISystem;
import com.mygdx.game.states.screens.GameScreen;

import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.utils.B2DConstants.*;

import java.util.HashMap;


/**
 * This class will systems and components, and takes in an engine
 **/
public class EntityManager {
    public static EntityManager EM;

    public final Engine engine;
    private final SpriteBatch batch;
    public EntityCreator entityCreator;

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
    private MapSystem mapSystem;

    // These entities are UI elements and are static in order to be accessible everywhere
    public Entity player1;
    public Entity player2;
    public Entity health1;
    public Entity health2;
    public Entity aimArrow;
    public Entity powerBar;
    public Entity powerBarArrow;
    public Entity timer;
    public Entity ground;
    public Entity map;

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
    Texture mapTexture = new Texture("mapsprite.png");

    // Takes in an engine from Ashley (instantiate engine in GameScreen)
    // Takes in batch because the RenderSystem will draw to screen
    public EntityManager(Engine engine, SpriteBatch batch) {
        EM = this;
        this.engine = engine;
        this.batch = batch;
        this.entityCreator = new EntityCreator();

        // Create ECS entity listeners -> has to be done before createEntities to function correctly
        this.createEntityListeners();

        // Create and add ECS systems and entities
        this.addSystems();
        this.createEntities();
        this.createMap();
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
        //this.mapSystem = new MapSystem();

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
        //this.engine.addSystem(this.mapSystem);

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
        powerBar = new Entity();
        powerBarArrow = new Entity();
        ground = new Entity();
        map = new Entity();
        aimArrow = new Entity();

        // Instantiate map-sprite entity
        map.add(new SpriteComponent(
                this.mapTexture,
                Application.camera.viewportWidth,
                Application.camera.viewportHeight,
                0)
        )
                .add(new PositionComponent(
                        Application.camera.viewportWidth / 2f,
                        Application.camera.viewportHeight / 2f)
                );
                //.add(new RenderComponent());

        // Instantiate player entities
        player1.add(new SpriteComponent(
                    this.tankTexture,
                    50f,
                    50f,
                        1)
                )
                .add(new PositionComponent(
                        spriteMapper.get(player1).size.x,
                        Application.camera.viewportHeight / 1.2f)
                )
                .add(new Box2DComponent(
                        positionMapper.get(player1).position,
                        spriteMapper.get(player1).size,
                        false,
                        100f,
                        BIT_PLAYER,
                        (short) (BIT_PLAYER | BIT_GROUND | BIT_PROJECTILE))
                )
                .add(new VelocityComponent(25f, 0))
                .add(new HealthComponent(100))
                .add(new ShootingComponent(0, 0))
                .add(new RenderComponent())
                .add(new PlayerComponent());

        player2.add(new SpriteComponent(
                    this.tankTexture,
                    50f,
                    50f,
                        1)
                )
                .add(new PositionComponent(
                        Application.camera.viewportWidth - 100f,
                        Application.camera.viewportHeight / 1.2f)
                )
                .add(new Box2DComponent(
                        positionMapper.get(player2).position,
                        spriteMapper.get(player2).size,
                        false,
                        100f,
                        BIT_PLAYER,
                        (short) (BIT_PLAYER | BIT_GROUND | BIT_PROJECTILE))
                )
                .add(new VelocityComponent(25f, 0))
                .add(new HealthComponent(100))
                .add(new ShootingComponent(0, 0))
                .add(new RenderComponent())
                .add(new PlayerComponent());


        timer.add(new PositionComponent(
                        Application.camera.viewportWidth / 2f,
                    Application.camera.viewportHeight * 0.97f)
                )
                .add(new FontComponent("Time: 0.0s"))
                .add(new RenderComponent());

        powerBar.add(new SpriteComponent(
                    this.powerBarTexture,
                    40f,
                    350f,
                        1)
                )
                .add(new PositionComponent(
                        Application.camera.viewportWidth - 50f,
                        Application.camera.viewportHeight / 2f)
                );

        powerBarArrow.add(new SpriteComponent(
                    this.rightArrowTexture,
                    40f,
                    40f,
                        1)
                )
                .add(new PositionComponent(
                        Application.camera.viewportWidth - 70f,
                        Application.camera.viewportHeight - (spriteMapper.get(powerBar).size.y) / 2f)
                );


        ground.add(new PositionComponent(
                        Application.camera.viewportWidth / 2f,
                        Application.camera.viewportHeight / 2f)
                )
                .add(new Box2DComponent(
                        positionMapper.get(ground).position,
                        new Vector2(10, 10),
                        true,
                        10000,
                        BIT_GROUND,
                        (short) (BIT_PLAYER | BIT_PROJECTILE))
                )
                .add(new RenderComponent());


        aimArrow.add(new PositionComponent(
                Application.camera.viewportWidth/ 2f,
                    Application.camera.viewportHeight / 2f)
                )
                .add(new SpriteComponent(
                        this.rightArrowTexture,
                        10f,
                        10f,
                        1)
                );

        health1
                .add(new ParentComponent(player1))
                .add(new FontComponent(
                        healthMapper.get(parentMapper.get(health1).parent).hp + " hp")
                )
                .add(new PositionComponent(
                        50f,
                        Application.camera.viewportHeight - 20f
                ))
                .add(new RenderComponent());

        health2
                .add(new ParentComponent(player2))
                .add(new FontComponent(
                        healthMapper.get(parentMapper.get(health1).parent).hp + " hp")
                )
                .add(new PositionComponent(
                        Application.camera.viewportWidth  - 50f,
                        Application.camera.viewportHeight - 20f
                ))
                .add(new RenderComponent());

        // Add all ECS entities to the engine
        this.engine.addEntity(player1);
        this.engine.addEntity(player2);
        this.engine.addEntity(timer);
        this.engine.addEntity(powerBar);
        this.engine.addEntity(powerBarArrow);
        this.engine.addEntity(ground);
        this.engine.addEntity(map);
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
            // Create a HashMap to keep track of Entities and their box2d bodies
            final HashMap<Entity, Body> bodyEntityHashMap = new HashMap<Entity, Body>();


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

    // Creates the map with all ground instances
    private void createMap() {
        TiledMap tiledMap = new TmxMapLoader().load("scifi.tmx");
        MapObjects objects = tiledMap.getLayers().get("ground").getObjects();

        // Loop through all ground objects, and give each object a Box2D body
        for (MapObject object : objects) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            rectangle.height *= 2;
            rectangle.width *= 2;
            rectangle.x *= 2;
            rectangle.y *= 2;

            Entity mapObject = new Entity();
            mapObject.add(new Box2DComponent(
                    rectangle.getCenter(new Vector2()),
                    new Vector2(rectangle.width * 2, rectangle.height * 2),
                    true,
                    10000f,
                    BIT_GROUND,
                    (short) (BIT_PLAYER | BIT_PROJECTILE))
            );
            engine.addEntity(mapObject);

            /*

            float scale = Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
            System.out.println(scale);
            rectangle.height *= 1.9;
            rectangle.width *= 2;
            rectangle.x *= 2;
            rectangle.y *= 1.9;

            //create a dynamic within the world body (also can be KinematicBody or StaticBody

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            Body body = GameScreen.world.createBody(bodyDef);

            //create a fixture for each body from the shape
            // Create FixtureDef representing properties such as density, restitution, etc
            FixtureDef fixtureDef = new FixtureDef();

            PolygonShape polygonShape = new PolygonShape();
            polygonShape.setAsBox(rectangle.width * 0.5f / PPM, rectangle.height * 0.5f / PPM);

            fixtureDef.shape = polygonShape; // Add the box shape to fixture
            fixtureDef.density = 10000; // Add density to fixture (increases mass)
            fixtureDef.friction = 0.1f;
            fixtureDef.filter.categoryBits = BIT_GROUND; // Is this category of bit
            fixtureDef.filter.maskBits = (short) (BIT_PLAYER | BIT_PROJECTILE); // Will collide with these bits
            body.createFixture(fixtureDef); // Add FixtureDef and id to body

            //setting the position of the body's origin. In this case with zero rotation
            Vector2 center = new Vector2();
            rectangle.getCenter(center);
            center.scl(1 / PPM);
            body.setTransform(center, 0);
            */
        }
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
        this.engine.removeSystem(this.mapSystem);

        this.engine.removeAllEntities(); // Remove all entities
    }

    // Dispose everything
    public void dispose() {
        this.tankTexture.dispose();
        this.rightArrowTexture.dispose();
        this.powerBarTexture.dispose();
    }
}
