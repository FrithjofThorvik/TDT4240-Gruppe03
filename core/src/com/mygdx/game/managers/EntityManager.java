package com.mygdx.game.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.HealthComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.PowerbarComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.ECS.systems.AimingSystem;
import com.mygdx.game.ECS.systems.ControllerSystem;
import com.mygdx.game.ECS.systems.GameplaySystem;
import com.mygdx.game.ECS.systems.PhysicsSystem;
import com.mygdx.game.ECS.systems.ProjectileSystem;
import com.mygdx.game.ECS.systems.RenderingSystem;

import javax.swing.Box;

//This class will systems and components, and takes in an engine
public class EntityManager {
    private final Engine engine;

    //Takes in an engine from Ashley (instantiate engine in GameScreen)
    //Takes in batch because the rendering system will draw to screen
    public EntityManager(Engine engine, SpriteBatch batch) {
        this.engine = engine;

        //Instantiate systems and add them to engine
        ControllerSystem cs = new ControllerSystem();
        RenderingSystem rs = new RenderingSystem(batch);
        ProjectileSystem ps = new ProjectileSystem();
        GameplaySystem gms = new GameplaySystem();
        AimingSystem ams = new AimingSystem();
        PhysicsSystem phs = new PhysicsSystem();

        engine.addSystem(cs);
        engine.addSystem(rs);
        engine.addSystem(ps);
        engine.addSystem(gms);
        engine.addSystem(ams);
        engine.addSystem(phs);

        //Instantiate player entities
        Entity player1 = new Entity();
        player1.add(new VelocityComponent(1000, 0))
                .add(new SpriteComponent(new Texture("tank.png"), 50f, 50f))
                .add(new PositionComponent(0 + player1.getComponent(SpriteComponent.class).size.x,
                        Gdx.graphics.getHeight() / 2f))
                .add(new HealthComponent(100))
                .add(new RenderableComponent())
                .add(new PlayerComponent())
                .add(new Box2DComponent(
                        player1.getComponent(PositionComponent.class).position,
                        player1.getComponent(SpriteComponent.class).size));

        Entity player2 = new Entity();
        player2.add(new VelocityComponent(1000, 0))
                .add(new SpriteComponent(new Texture("tank.png"), 50f, 50f))
                .add(new PositionComponent(Gdx.graphics.getWidth() - 100f,
                        Gdx.graphics.getHeight() / 2f))
                .add(new HealthComponent(100))
                .add(new RenderableComponent())
                .add(new PlayerComponent())
                .add(new Box2DComponent(
                        player2.getComponent(PositionComponent.class).position,
                        player2.getComponent(SpriteComponent.class).size));

        // Create timer entity
        Entity timer = new Entity();
        timer.add(new FontComponent("Time: 0.0s"))
                .add(new PositionComponent(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 0.97f))
                .add(new RenderableComponent());

        Entity powerbar = new Entity();
        powerbar.add(new SpriteComponent(new Texture("powerbar.png"), 40f, 350f))
                .add(new PositionComponent(Gdx.graphics.getWidth() - 50f, Gdx.graphics.getHeight() / 2f))
                .add(new PowerbarComponent());

        Entity powerArrow = new Entity();
        powerArrow.add(new SpriteComponent(new Texture("right-arrow.png"), 40f, 40f))
                .add(new PositionComponent(
                        Gdx.graphics.getWidth() - 70f,
                        (Gdx.graphics.getHeight() - powerbar.getComponent(SpriteComponent.class).size.y) / 2f))
                .add(new PowerbarComponent());

        Entity test = new Entity();
        test.add(new VelocityComponent(0, 0))
                .add(new SpriteComponent(new Texture("right-arrow.png"), 40f, 40f))
                .add(new PositionComponent(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f))
                .add(new Box2DComponent(
                        test.getComponent(PositionComponent.class).position,
                        test.getComponent(SpriteComponent.class).size))
                .add(new RenderableComponent());

        //Add entities to the engine
        engine.addEntity(player1);
        engine.addEntity(player2);

        engine.addEntity(timer);

        engine.addEntity(powerbar);
        engine.addEntity(powerArrow);

        engine.addEntity(test);
    }

    //On update, call the engines update method
    public void update() {
        engine.update(Gdx.graphics.getDeltaTime());
    }
}
