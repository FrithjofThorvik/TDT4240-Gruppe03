package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;

import static com.mygdx.game.utils.B2DConstants.PPM;

public class RenderingSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;//will contain all entities this system controls
    private SpriteBatch batch;

    //private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

    public RenderingSystem(SpriteBatch batch) {
        this.batch = batch;
    }

    public void addedToEngine(Engine e) {
        entities = e.getEntitiesFor(Family.all(RenderableComponent.class, SpriteComponent.class, PositionComponent.class).get());
    }

    public void update(float deltaTime) {
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            SpriteComponent scom = entity.getComponent(SpriteComponent.class);
            PositionComponent pcom = entity.getComponent(PositionComponent.class);
            //Draw the sprite, so that the center of its sprite is the position of the given entity
            batch.draw(scom.sprite.getTexture(), (pcom.position.x - scom.size / 2), (pcom.position.y - scom.size / 2),
                    scom.size, scom.size);
        }
    }
}
