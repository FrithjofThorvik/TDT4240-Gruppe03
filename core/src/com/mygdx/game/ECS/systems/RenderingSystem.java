package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.ECS.components.misc.FontComponent;
import com.mygdx.game.ECS.components.misc.PositionComponent;
import com.mygdx.game.ECS.components.flags.RenderComponent;
import com.mygdx.game.ECS.components.misc.SpriteComponent;

import static com.mygdx.game.ECS.managers.ECSManager.ECSManager;


/**
 * This system should draw all components that can render
 **/
public class RenderingSystem extends EntitySystem {
    // Arrays for storing entity instances for fonts and sprites
    private ImmutableArray<Entity> spriteEntities;
    private ImmutableArray<Entity> fontEntities;

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
            // TODO: Sort by z, rather than brute force
            for (int i = 0; i < this.spriteEntities.size(); i++) {
                SpriteComponent spriteComponent = this.spriteEntities.get(i).getComponent(SpriteComponent.class);
                if(spriteComponent.zValue == 0){
                    // Fetch each component
                    Entity entity = this.spriteEntities.get(i);
                    SpriteComponent sc = ECSManager.spriteMapper.get(entity);
                    PositionComponent pc = ECSManager.positionMapper.get(entity);

                    // Draw the sprite, so that the center of its sprite is the position of the given entity
                    sc.sprite.setSize(sc.size.x, sc.size.y);
                    sc.sprite.setPosition(pc.position.x - sc.size.x / 2f, pc.position.y - sc.size.y / 2f);
                    sc.sprite.setOrigin(sc.size.x / 2f, sc.size.y / 2f);
                    sc.sprite.draw(batch);
                }
            }
            for (int i = 0; i < this.spriteEntities.size(); i++) {
                SpriteComponent spriteComponent = this.spriteEntities.get(i).getComponent(SpriteComponent.class);
                if(spriteComponent.zValue == 1){
                    // Fetch each component
                    Entity entity = this.spriteEntities.get(i);
                    SpriteComponent sc = ECSManager.spriteMapper.get(entity);
                    PositionComponent pc = ECSManager.positionMapper.get(entity);

                    // Draw the sprite, so that the center of its sprite is the position of the given entity
                    sc.sprite.setSize(sc.size.x, sc.size.y);
                    sc.sprite.setPosition(pc.position.x - sc.size.x / 2f, pc.position.y - sc.size.y / 2f);
                    sc.sprite.setOrigin(sc.size.x / 2f, sc.size.y / 2f);
                    sc.sprite.draw(batch);
                }
            }

            // Loop through all sprite entities, and draw screen
            for (int i = 0; i < this.fontEntities.size(); i++) {
                // Fetch each component
                Entity entity = this.fontEntities.get(i);
                FontComponent fc = ECSManager.fontMapper.get(entity);
                PositionComponent pc = ECSManager.positionMapper.get(entity);

                // Draw font components to the given position with respect to center of font
                fc.font.draw(
                        batch,
                        fc.text,
                        pc.position.x - (fc.layout.width / 2f),
                        pc.position.y - (fc.layout.height / 2f)
                );
            }
        }
    }
}