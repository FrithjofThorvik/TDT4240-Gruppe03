package com.mygdx.game.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
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
import com.badlogic.gdx.physics.box2d.Fixture;
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
import com.mygdx.game.ECS.systems.MovementSystem;
import com.mygdx.game.ECS.systems.ShootingSystem;
import com.mygdx.game.ECS.systems.PhysicsSystem;
import com.mygdx.game.ECS.systems.PowerUpSystem;
import com.mygdx.game.ECS.systems.ProjectileSystem;
import com.mygdx.game.ECS.systems.RenderingSystem;

import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.utils.B2DConstants.*;
import static com.mygdx.game.utils.GameConstants.MAX_SHOOTING_POWER;

import java.util.HashMap;


/**
 * This class creates initializes all the ECS -> adds things to the engine
 * Adds all the systems required for the gameplay
 * Adds all the UIentities required for gameplay
 * Adds entity listeners required for gameplay
 * Contains component mappers
 **/
public class EntityManager {
    public static EntityManager EM;

    public final Engine engine;
    private final SpriteBatch batch;
    public EntityCreator entityCreator;

    // These entities are UI elements and are static in order to be accessible everywhere
    public Entity aimArrow;
    public Entity powerBar;
    public Entity powerBarArrow;
    public Entity timer;
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
    public final ComponentMapper<PlayerComponent> playerMapper = ComponentMapper.getFor(PlayerComponent.class);

    //Tuple for storing entity/fixture pair
    public static HashMap<Fixture, Entity> entityFixtureHashMap = new HashMap<Fixture, Entity>();

    // Textures
    Texture powerBarTexture = new Texture("powerbar.png");
    Texture rightArrowTexture = new Texture("right-arrow.png");
    Texture aimArrowTexture = new Texture("aim-arrow.png");

    // Takes in an engine from Ashley (instantiate engine in GameScreen)
    // Takes in batch because the RenderSystem will draw to screen
    public EntityManager(SpriteBatch batch) {
        EM = this;
        this.engine = new Engine();
        this.batch = batch;
        this.entityCreator = new EntityCreator();

        // Create ECS entity listeners -> has to be done before createEntities to function correctly
        this.createEntityListeners();

        // Create and add ECS systems and entities
        this.addSystems();
        this.createUIEntities();
    }


    // Add all ECS systems
    private void addSystems() {
        // Instantiates all ECS systems
        MovementSystem movementSystem = new MovementSystem();
        AimingSystem aimingSystem = new AimingSystem();
        RenderingSystem renderingSystem = new RenderingSystem(this.batch);
        ProjectileSystem projectileSystem = new ProjectileSystem();
        PowerUpSystem powerUpSystem = new PowerUpSystem();
        PhysicsSystem physicsSystem = new PhysicsSystem();
        ShootingSystem shootingSystem = new ShootingSystem();

        // Add all ECS systems to the engine
        this.engine.addSystem(movementSystem);
        this.engine.addSystem(aimingSystem);
        this.engine.addSystem(renderingSystem);
        this.engine.addSystem(projectileSystem);
        this.engine.addSystem(powerUpSystem);
        this.engine.addSystem(physicsSystem);
        this.engine.addSystem(shootingSystem);

    }

    // Create entities with ECS components
    public void createUIEntities() {
        // Instantiate all UI entities
        powerBar = new Entity();
        powerBarArrow = new Entity();
        aimArrow = new Entity();

        timer = entityCreator.getTextFont().createEntity();
        positionMapper.get(timer).position = new Vector2(Application.camera.viewportWidth / 2f,
                Application.camera.viewportHeight * 0.97f);

        powerBar.add(new SpriteComponent(
                this.powerBarTexture,
                40f,
                350f, 1)
        ).add(new PositionComponent(
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

        aimArrow.add(new PositionComponent(
                Application.camera.viewportWidth / 2f,
                Application.camera.viewportHeight / 2f)
        )
                .add(new SpriteComponent(
                        this.aimArrowTexture,
                        40f,
                        20f,
                        1)
                );

        // Add all ECS entities to the engine
        this.engine.addEntity(powerBar);
        this.engine.addEntity(powerBarArrow);
        this.engine.addEntity(aimArrow);
    }

    // Init Mode specific entities
    public void createModeEntities() {
        GSM.getGameMode().initEntities();
    }

    // Add entity listeners for observe & listen to when adding and removing entities
    private void createEntityListeners() {
        // Stops the entity from moving when it loses the MovementControlComponent
        EntityListener movementControlListener = new EntityListener() {

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
        this.engine.addEntityListener(HasControl, movementControlListener);

        // This should activate when a box2d component is added or removed from an entity
        EntityListener box2DComponentListener = new EntityListener() {
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
    public void createMap(String filename, String mapSprite) {
        map = new Entity();
        Texture mapTexture = new Texture(mapSprite);
        // Instantiate map-sprite entity
        map.add(new SpriteComponent(
                mapTexture,
                Application.camera.viewportWidth,
                Application.camera.viewportHeight,
                0)
        )
                .add(new PositionComponent(
                        Application.camera.viewportWidth / 2f,
                        Application.camera.viewportHeight / 2f)
                )
                .add(new RenderComponent());
        this.engine.addEntity(map);

        TiledMap tiledMap = new TmxMapLoader().load(filename);
        MapObjects objects = tiledMap.getLayers().get("ground").getObjects();

        // Loop through all ground objects, and give each object a Box2D body
        for (MapObject object : objects) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            /** Since the map texture scales too the screen -> when we create box2D elements, they will have to scale the same amount **/
            // Calculate scaleUp ratio
            float scaleUpX = spriteMapper.get(map).size.x / mapTexture.getWidth();
            float scaleUpY = spriteMapper.get(map).size.y / mapTexture.getHeight();

            // Scale the mapObject rectangle shape
            rectangle.width *= scaleUpX;
            rectangle.height *= scaleUpY;
            rectangle.x *= scaleUpX;
            rectangle.y *= scaleUpY;

            // Create a new map entity with box2d component equal to rectangle
            Entity mapObject = new Entity();
            mapObject.add(new Box2DComponent(
                    rectangle.getCenter(new Vector2()),
                    new Vector2(rectangle.width, rectangle.height),
                    true,
                    10000f,
                    BIT_GROUND,
                    (short) (BIT_PLAYER | BIT_PROJECTILE))
            );
            engine.addEntity(mapObject);
        }
    }

    // On update, call the engines update method
    public void update(float dt) {
        engine.update(dt);
    }

    // Dispose everything
    public void dispose() {
        this.rightArrowTexture.dispose();
        this.powerBarTexture.dispose();
        this.aimArrowTexture.dispose();
    }

    public void removeAllEntities() {
        engine.removeAllEntities();
    }

    public void removeShootingRender() {
        aimArrow.remove(RenderComponent.class);
        powerBar.remove(RenderComponent.class);
        powerBarArrow.remove(RenderComponent.class);
    }

    public void addShootingRender() {
        aimArrow.add(new RenderComponent());
        powerBar.add(new RenderComponent());
        powerBarArrow.add(new RenderComponent());
    }

    // Display the powerbar according to player power
    public void updatePowerBar(Entity player) {
        // Calculate the startingPosition of an powerBar arrow (this is done here so that if the screen is resized the arrowPosition is updated)
        float startPositionArrow = positionMapper.get(powerBar).position.y - spriteMapper.get(powerBar).size.y / 2;

        //Set position of powerBarArrow -> given the power of the shootingComponent
        float power = shootingMapper.get(player).power;
        positionMapper.get(powerBarArrow).position.y = startPositionArrow + (spriteMapper.get(powerBar).size.y * (power / MAX_SHOOTING_POWER));
    }

    // Utility function for spawning players
    public void spawnPlayers(int numberOfPlayers) {
        for (int i = 0; i < numberOfPlayers; i++) {
            if (i % 2 == 0)
                entityCreator.getPlayerClass(EntityCreator.PLAYERS.DEFAULT).createEntity();
            else
                entityCreator.getPlayerClass(EntityCreator.PLAYERS.SPEEDY).createEntity();

        }
    }

    // Utility function for creating health displayers
    public void createHealthDisplayers() {
        ImmutableArray<Entity> players = engine.getEntitiesFor(Family.one(PlayerComponent.class).get());
        for (int i = 0; i < players.size(); i++) {
            Entity player = players.get(i); // Get a player

            // Create the health displayer and add the player as the parent -> such that the health font is attached to the player
            EM.entityCreator.getHealthFont().createEntity().add(new ParentComponent(player));
        }
    }

    // Utility function for creating a target (used in training mode)
    public Entity spawnTarget() {
        Entity target = new Entity();
        target.add(new SpriteComponent(new Texture("target.png"), 75, 75, 1))
                .add(new Box2DComponent(
                        new Vector2((float) Application.VIRTUAL_WORLD_WIDTH / 2, Application.APP_DESKTOP_HEIGHT / 2 + 25), spriteMapper.get(target).size, false, 10000000f,
                        BIT_PLAYER, (short) (BIT_PROJECTILE | BIT_GROUND))
                )
                .add(new PositionComponent(target.getComponent(Box2DComponent.class).body.getPosition().x, target.getComponent(Box2DComponent.class).body.getPosition().y))
                .add(new RenderComponent());
        engine.addEntity(target);
        return target;
    }
}
