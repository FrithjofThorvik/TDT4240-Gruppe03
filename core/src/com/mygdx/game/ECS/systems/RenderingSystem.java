package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.RenderComponent;
import com.mygdx.game.ECS.components.SpriteComponent;


/**
 * This system should draw all components that can render
 * TODO: Implement rendering for ground (map)
 **/
public class RenderingSystem extends EntitySystem {
    // Arrays for storing entity instances for fonts and sprites
    private ImmutableArray<Entity> spriteEntities;
    private ImmutableArray<Entity> fontEntities;

    // Using a component mapper is the fastest way to load entities
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);
    private final ComponentMapper<FontComponent> fm = ComponentMapper.getFor(FontComponent.class);

    private final SpriteBatch batch;

    // RenderingSystem constructor with Application batch instance
    public RenderingSystem(SpriteBatch batch) {
        this.batch = batch;
    }

    // Get entities matching the given components, and store in respective array
    public void addedToEngine(Engine e) {
        // Store sprite entities
        this.spriteEntities = e.getEntitiesFor(
                Family.all(
                        RenderComponent.class,
                        SpriteComponent.class,
                        PositionComponent.class
                ).get()
        );

        // Store font entities
        this.fontEntities = e.getEntitiesFor(
                Family.all(
                        RenderComponent.class,
                        FontComponent.class,
                        PositionComponent.class
                ).get()
        );
    }

    // Update function for RenderingSystem
    public void update(float deltaTime) {
        if (this.spriteEntities.size() > 0 || this.fontEntities.size() > 0) {
            // Loop through all sprite entities, and draw screen
            for (int i = 0; i < this.spriteEntities.size(); i++) {
                // Fetch each component
                Entity entity = this.spriteEntities.get(i);
                SpriteComponent sc = this.sm.get(entity);
                PositionComponent pc = this.pm.get(entity);

                // Draw the sprite, so that the center of its sprite is the position of the given entity
                sc.sprite.setSize(sc.size.x, sc.size.y);
                sc.sprite.setPosition(pc.position.x - sc.size.x / 2f, pc.position.y - sc.size.y / 2);
                sc.sprite.setOrigin(sc.size.x / 2, sc.size.y / 2);
                sc.sprite.draw(batch);
            }

            // Loop through all sprite entities, and draw screen
            for (int i = 0; i < this.fontEntities.size(); i++) {
                // Fetch each component
                Entity entity = this.fontEntities.get(i);
                FontComponent fc = this.fm.get(entity);
                PositionComponent pc = this.pm.get(entity);

                // Draw font components to the given position with respect to center of font
                fc.font.draw(batch, fc.text, pc.position.x - (fc.layout.width / 2f), pc.position.y - (fc.layout.height / 2f));
            }
        }
    }
}