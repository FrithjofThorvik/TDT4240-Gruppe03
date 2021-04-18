package com.mygdx.game.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    public Entity ground;

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
    Texture tankTexture = new Texture("tank.png");
    Texture powerBarTexture = new Texture("powerbar.png");
    Texture rightArrowTexture = new Texture("right-arrow.png");

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
        ground = new Entity();
        aimArrow = new Entity();

        timer = entityCreator.getTextFont().createEntity();
        positionMapper.get(timer).position = new Vector2(Application.camera.viewportWidth / 2f,
                Application.camera.viewportHeight * 0.97f);

        powerBar.add(new SpriteComponent(
                this.powerBarTexture,
                40f,
                350f)
        )
                .add(new PositionComponent(
                        Application.camera.viewportWidth - 50f,
                        Application.camera.viewportHeight / 2f)
                );

        powerBarArrow.add(new SpriteComponent(
                this.rightArrowTexture,
                40f,
                40f)
        )
                .add(new PositionComponent(
                        Application.camera.viewportWidth - 70f,
                        Application.camera.viewportHeight - (spriteMapper.get(powerBar).size.y) / 2f)
                );

        ground.add(new SpriteComponent(
                this.tankTexture,
                Application.camera.viewportWidth * 2f,
                10f)
        )
                .add(new PositionComponent(
                        Application.camera.viewportWidth / 2f,
                        Application.camera.viewportHeight / 2f)
                )
                .add(new Box2DComponent(
                        positionMapper.get(ground).position,
                        spriteMapper.get(ground).size,
                        true,
                        10000,
                        BIT_GROUND,
                        (short) (BIT_PLAYER | BIT_PROJECTILE))
                )
                .add(new RenderComponent());

        aimArrow.add(new PositionComponent(
                Application.camera.viewportWidth / 2f,
                Application.camera.viewportHeight / 2f)
        )
                .add(new SpriteComponent(
                        this.rightArrowTexture,
                        10f,
                        10f)
                );

        // Add all ECS entities to the engine
        this.engine.addEntity(powerBar);
        this.engine.addEntity(powerBarArrow);
        this.engine.addEntity(ground);
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

    // On update, call the engines update method
    public void update(float dt) {
        engine.update(dt);
    }

    // Dispose everything
    public void dispose() {
        this.tankTexture.dispose();
        this.rightArrowTexture.dispose();
        this.powerBarTexture.dispose();
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

    // Call to make the ai marrow update according to player aim angle
    public void repositionAimArrow(Entity player) {
        PositionComponent position = positionMapper.get(player);
        // Get the angle (in degrees) and power of the currentPlayer's shootingComponent
        double aimAngleInDegrees = 90f - (float) shootingMapper.get(player).angle / (float) Math.PI * 180f;

        // Set rotation and position of AimArrow (displayed above the player -> rotated by where the player aims)
        spriteMapper.get(aimArrow).sprite.setRotation((float) aimAngleInDegrees);
        positionMapper.get(aimArrow).position.x = position.position.x;
        positionMapper.get(aimArrow).position.y = position.position.y + 25;
    }

    // Display the powerbar according to player power
    public void updatePowerBar(Entity player){
        // Calculate the startingPosition of an powerBar arrow (this is done here so that if the screen is resized the arrowPosition is updated)
        float startPositionArrow = positionMapper.get(powerBar).position.y - spriteMapper.get(powerBar).size.y / 2;

        //Set position of powerBarArrow -> given the power of the shootingComponent
        float power = shootingMapper.get(player).power;
        positionMapper.get(powerBarArrow).position.y = startPositionArrow + (spriteMapper.get(powerBar).size.y * (power / MAX_SHOOTING_POWER));
    }

    // Utility function for spawning players
    public void spawnPlayers(int numberOfPlayers) {
        for (int i = 0; i < numberOfPlayers; i++) {
            entityCreator.getPlayerClass(EntityCreator.PLAYERS.DEFAULT).createEntity();
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
        target.add(new SpriteComponent(new Texture("target.png"), 50, 50))
                .add(new Box2DComponent(
                        new Vector2((float) Application.VIRTUAL_WORLD_WIDTH / 2, ground.getComponent(PositionComponent.class).position.y + 25), new Vector2(50f, 50f), true, 100f,
                        BIT_PLAYER, (short) (BIT_PROJECTILE))
                )
                .add(new PositionComponent(target.getComponent(Box2DComponent.class).body.getPosition().x, target.getComponent(Box2DComponent.class).body.getPosition().y))
                .add(new RenderComponent());
        engine.addEntity(target);
        return target;
    }
}
