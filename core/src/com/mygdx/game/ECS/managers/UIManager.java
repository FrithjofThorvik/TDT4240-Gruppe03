package com.mygdx.game.ECS.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.components.misc.PositionComponent;
import com.mygdx.game.ECS.components.misc.SpriteComponent;
import com.mygdx.game.ECS.components.flags.RenderComponent;

import static com.mygdx.game.ECS.managers.ECSManager.ECSManager;
import static com.mygdx.game.utils.GameConstants.MAX_SHOOTING_POWER;

public class UIManager {
    // These entities are UI elements and are static in order to be accessible everywhere
    private Entity aimArrow;
    private Entity powerBar;
    private Entity powerBarArrow;
    private Entity timer;

    // Textures
    Texture powerBarTexture = new Texture("powerbar.png");
    Texture rightArrowTexture = new Texture("right-arrow.png");
    Texture aimArrowTexture = new Texture("aim-arrow.png");

    public UIManager() {
        createUIEntities();
    }

    // Create entities with ECS components
    public void createUIEntities() {
        // Instantiate all UI entities
        powerBar = new Entity();
        powerBarArrow = new Entity();
        aimArrow = new Entity();

        timer = ECSManager.getEntityTemplateMapper().getTextFont().createEntity();
        ECSManager.positionMapper.get(getTimer()).position = new Vector2(Application.camera.viewportWidth / 2f,
                Application.camera.viewportHeight * 0.97f);

        getPowerBar().add(new SpriteComponent(
                this.powerBarTexture,
                40f,
                350f, 1)
        ).add(new PositionComponent(
                Application.camera.viewportWidth - 50f,
                Application.camera.viewportHeight / 2f)
        );

        getPowerBarArrow().add(new SpriteComponent(
                this.rightArrowTexture,
                40f,
                40f,
                1)
        )
                .add(new PositionComponent(
                        Application.camera.viewportWidth - 70f,
                        Application.camera.viewportHeight - (ECSManager.spriteMapper.get(getPowerBar()).size.y) / 2f)
                );

        getAimArrow().add(new PositionComponent(
                Application.camera.viewportWidth / 2f,
                Application.camera.viewportHeight / 2f)
        )
                .add(new SpriteComponent(
                        this.aimArrowTexture,
                        40f,
                        20f,
                        1)
                );

        // Add all ECS entities to the engine
        ECSManager.getEngine().addEntity(getPowerBar());
        ECSManager.getEngine().addEntity(getPowerBarArrow());
        ECSManager.getEngine().addEntity(getAimArrow());
    }

    // Dispose everything
    public void dispose() {
        this.rightArrowTexture.dispose();
        this.powerBarTexture.dispose();
        this.aimArrowTexture.dispose();
    }

    public Entity getAimArrow() {
        return aimArrow;
    }

    public Entity getPowerBar() {
        return powerBar;
    }

    public Entity getPowerBarArrow() {
        return powerBarArrow;
    }

    public Entity getTimer() {
        return timer;
    }
}
