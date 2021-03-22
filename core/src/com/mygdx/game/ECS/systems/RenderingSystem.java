package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;

//This system should draw all components that are Renderable
public class RenderingSystem extends EntitySystem {
    // Arrays for storing entity instances for fonts and sprites
    private ImmutableArray<Entity> spriteEntities;
    private ImmutableArray<Entity> fontEntities;
    // NOTE: For performance purposes, ComponentMapper<Entity> can be used

    private final SpriteBatch batch;

    // RenderingSystem constructor with Application batch instance
    public RenderingSystem(SpriteBatch batch) {
        this.batch = batch;
    }

    // Get entities matching the given components, and store in respective array
    public void addedToEngine(Engine e) {
        // Store sprite entities
        spriteEntities = e.getEntitiesFor(
                Family.all(
                        RenderableComponent.class,
                        SpriteComponent.class,
                        PositionComponent.class
                ).get()
        );

        // Store font entities
        fontEntities = e.getEntitiesFor(
                Family.all(
                        RenderableComponent.class,
                        FontComponent.class,
                        PositionComponent.class
                ).get()
        );
    }

    // Update function for
    public void update(float deltaTime) {
        // Loop through all sprite entities, and draw screen
        for (int i = 0; i < spriteEntities.size(); ++i) {
            // Fetch each component
            Entity entity = spriteEntities.get(i);
            SpriteComponent sc = entity.getComponent(SpriteComponent.class);
            PositionComponent pc = entity.getComponent(PositionComponent.class);

            // Draw the sprite, so that the center of its sprite is the position of the given entity
            batch.draw(sc.sprite.getTexture(), (pc.position.x - sc.size / 2), (pc.position.y - sc.size / 2),
                    sc.size, sc.size);
        }

        // Loop through all sprite entities, and draw screen
        for (int i = 0; i < fontEntities.size(); ++i) {
            // Fetch each component
            Entity entity = fontEntities.get(i);
            FontComponent fc = entity.getComponent(FontComponent.class);
            PositionComponent pc = entity.getComponent(PositionComponent.class);

            // Draw font components to the given position with respect to center of font
            fc.font.draw(batch, fc.text, pc.position.x - (fc.layout.width / 2f), pc.position.y - (fc.layout.height / 2f));
        }
    }
}