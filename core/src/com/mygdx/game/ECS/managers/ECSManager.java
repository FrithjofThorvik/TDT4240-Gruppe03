package com.mygdx.game.ECS.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.ECS.EntityUtils.EntityTemplateMapper;
import com.mygdx.game.ECS.components.misc.CollisionComponent;
import com.mygdx.game.ECS.components.flags.MovementControlComponent;
import com.mygdx.game.ECS.components.misc.ParentComponent;
import com.mygdx.game.ECS.components.flags.PowerUpComponent;
import com.mygdx.game.ECS.components.projectiles.ProjectileComponent;
import com.mygdx.game.ECS.components.misc.ShootingComponent;
import com.mygdx.game.ECS.components.misc.Box2DComponent;
import com.mygdx.game.ECS.components.misc.FontComponent;
import com.mygdx.game.ECS.components.misc.HealthComponent;
import com.mygdx.game.ECS.components.flags.PlayerComponent;
import com.mygdx.game.ECS.components.misc.PositionComponent;
import com.mygdx.game.ECS.components.misc.SpriteComponent;
import com.mygdx.game.ECS.components.misc.VelocityComponent;
import com.mygdx.game.ECS.systems.AimingSystem;
import com.mygdx.game.ECS.systems.MovementSystem;
import com.mygdx.game.ECS.systems.ShootingSystem;
import com.mygdx.game.ECS.systems.PhysicsSystem;
import com.mygdx.game.ECS.systems.PowerUpSystem;
import com.mygdx.game.ECS.systems.ProjectileSystem;
import com.mygdx.game.ECS.systems.RenderingSystem;

import static com.mygdx.game.gamelogic.states.GameStateManager.GSM;

import java.util.HashMap;


/**
 * This class creates initializes all the ECS -> adds things to the engine
 * Adds all the systems required for the gameplay
 * Adds all the UIentities required for gameplay
 * Adds entity listeners required for gameplay
 * Contains component mappers
 **/
public class ECSManager {
    public static ECSManager ECSManager;
    public MapManager mapManager;
    public GameEntitiesManager gameEntityManager;
    public UIManager UIManager;


    private final Engine engine;
    private final SpriteBatch batch;
    private final EntityTemplateMapper entityTemplateMapper;

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
    public final ComponentMapper<PowerUpComponent> effectMapper = ComponentMapper.getFor(PowerUpComponent.class);
    public final ComponentMapper<FontComponent> fontMapper = ComponentMapper.getFor(FontComponent.class);
    public final ComponentMapper<PlayerComponent> playerMapper = ComponentMapper.getFor(PlayerComponent.class);

    //Tuple for storing entity/fixture pair
    public static HashMap<Fixture, Entity> entityFixtureHashMap = new HashMap<Fixture, Entity>();

    // Takes in an engine from Ashley (instantiate engine in GameScreen)
    // Takes in batch because the RenderSystem will draw to screen
    public ECSManager(SpriteBatch batch) {
        ECSManager = this;
        this.engine = new Engine();
        this.batch = batch;
        this.entityTemplateMapper = new EntityTemplateMapper();

        // Create ECS entity listeners -> has to be done before createEntities to function correctly
        this.createEntityListeners();

        // Create and add ECS systems and entities
        this.addSystems();

        mapManager = new MapManager();
        gameEntityManager = new GameEntitiesManager();
        UIManager = new UIManager();
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

    public Engine getEngine() {
        return engine;
    }

    public EntityTemplateMapper getEntityTemplateMapper() {
        return entityTemplateMapper;
    }

    public void removeAllEntities() {
        engine.removeAllEntities();
    }

}
