package com.mygdx.game.ECS;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.components.HealthComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.ECS.systems.ControllerSystem;
import com.mygdx.game.ECS.systems.RenderingSystem;

public class EntityManager {//This class will systems and components, and takes in an engine
    private Engine engine;

    //Takes in an engine from Ashley (instantiate engine in Gamescreen)
    //Takes in batch because the rendering system will draw to screen
    public EntityManager(Engine e, SpriteBatch batch) {
        engine = e;

        //Instantiate systems and add them to engine
        ControllerSystem cms = new ControllerSystem();
        engine.addSystem(cms);
        RenderingSystem rs = new RenderingSystem(batch);
        engine.addSystem(rs);

        //Instantiate entities and add them to the engine
        Entity entity = new Entity();
        entity.add(new VelocityComponent(MathUtils.random(-1, 1)))
                .add(new SpriteComponent(new Texture("badlogic.jpg"), 50f))
                .add(new RenderableComponent())
                .add(new PositionComponent(Gdx.graphics.getWidth() / 2f,
                        Gdx.graphics.getHeight() / 2f))
                .add(new HealthComponent(100));

        engine.addEntity(entity);

    }

    //On update, call the engines update method
    public void update() {
        engine.update(Gdx.graphics.getDeltaTime());
    }
}
