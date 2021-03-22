package com.mygdx.game.ECS;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.components.HasControlComponent;
import com.mygdx.game.ECS.components.HealthComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.ECS.systems.AimingSystem;
import com.mygdx.game.ECS.systems.ControllerSystem;
import com.mygdx.game.ECS.systems.GameplaySystem;
import com.mygdx.game.ECS.systems.ProjectileSystem;
import com.mygdx.game.ECS.systems.RenderingSystem;

public class EntityManager {//This class will systems and components, and takes in an engine
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

        //Instantiate entities and add them to the engine
        Entity player1 = new Entity();
        player1.add(new VelocityComponent(0))
                .add(new SpriteComponent(new Texture("badlogic.jpg"), 50f))
                .add(new RenderableComponent())
                .add(new PositionComponent(0+player1.getComponent(SpriteComponent.class).size,
                        Gdx.graphics.getHeight() / 2f))
                .add(new HealthComponent(100))
                .add(new PlayerComponent());

        Entity player2 = new Entity();
        player2.add(new VelocityComponent(0))
                .add(new SpriteComponent(new Texture("badlogic.jpg"), 50f))
                .add(new RenderableComponent())
                .add(new PositionComponent(Gdx.graphics.getWidth()-player2.getComponent(SpriteComponent.class).size,
                        Gdx.graphics.getHeight() / 2f))
                .add(new HealthComponent(100))
                .add(new PlayerComponent());

        engine.addEntity(player1);
        engine.addEntity(player2);
    }

    //On update, call the engines update method
    public void update() {
        engine.update(Gdx.graphics.getDeltaTime());
    }
}
