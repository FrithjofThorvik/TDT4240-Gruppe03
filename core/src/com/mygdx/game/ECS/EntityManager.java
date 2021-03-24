package com.mygdx.game.ECS;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.mygdx.game.ECS.systems.ProjectileSystem;
import com.mygdx.game.ECS.systems.RenderingSystem;

//This class will systems and components, and takes in an engine
public class EntityManager {
    private Engine engine;

    //Takes in an engine from Ashley (instantiate engine in Gamescreen)
    //Takes in batch because the rendering system will draw to screen
    public EntityManager(Engine e, SpriteBatch batch) {
        engine = e;

        //Instantiate systems and add them to engine
        ControllerSystem cs = new ControllerSystem();
        engine.addSystem(cs);
        RenderingSystem rs = new RenderingSystem(batch);
        engine.addSystem(rs);
        ProjectileSystem ps = new ProjectileSystem();
        engine.addSystem(ps);
        GameplaySystem gms = new GameplaySystem();
        engine.addSystem(gms);
        AimingSystem ams = new AimingSystem();
        engine.addSystem(ams);

        //Instantiate player entities
        Entity player1 = new Entity();
        player1.add(new VelocityComponent(2, 0))
                .add(new SpriteComponent(new Texture("tank.png"), 50f, 50f))
                .add(new PositionComponent(0 + player1.getComponent(SpriteComponent.class).size.x,
                        Gdx.graphics.getHeight() / 2f))
                .add(new HealthComponent(100))
                .add(new RenderableComponent())
                .add(new PlayerComponent());

        Entity player2 = new Entity();
        player2.add(new VelocityComponent(2, 0))
                .add(new SpriteComponent(new Texture("tank.png"), 50f, 50f))
                .add(new PositionComponent(Gdx.graphics.getWidth() - 100f,
                        Gdx.graphics.getHeight() / 2f))
                .add(new HealthComponent(100))
                .add(new RenderableComponent())
                .add(new PlayerComponent());

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

        //Add entities to the engine
        engine.addEntity(player1);
        engine.addEntity(player2);

        engine.addEntity(timer);

        engine.addEntity(powerbar);
        engine.addEntity(powerArrow);
    }

    //On update, call the engines update method
    public void update() {
        engine.update(Gdx.graphics.getDeltaTime());
    }
}
