package com.mygdx.game.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.entities.EntityCreator;

import static com.mygdx.game.managers.EntityManager.EM;


/**
 * This is used for creating a controller
 **/
public class ControlManager {
    public static ControlManager CM;
    public int currentProjectile = 0;
    private int numberOfProjectiles = 0;
    public boolean leftPressed, rightPressed, aimPressed, powerPressed = false;
    private boolean anyTouched = false;
    private final Array<Texture> projectileTextures;

    private float scaler = 1.5f; // Use to quickly change scale of controller images

    private Image aimImg;
    private Image powerImg;
    private Image projectileImg;
    private Image projectileLeftImg;
    private Image projectileRightImg;
    private Image leftImg;
    private Image rightImg;

    // Prepare component mappers

    public ControlManager() {
        CM = this;
        this.projectileTextures = new Array<Texture>();

        // Get number of projectile types
        numberOfProjectiles = EntityCreator.PROJECTILES.values().length;
        for (int i = 0; i < numberOfProjectiles; i++) {
            this.projectileTextures.add(EM.entityCreator.getProjectileClass(EntityCreator.PROJECTILES.values()[i]).getTexture());
        }

        Actor controller = this.createControllerActor();
        Application.stage.addActor(controller); // Add table actor to GameScreen stage
        this.idle();
    }

    // Create controller table
    private Actor createControllerActor() {
        Texture buttonLeft = new Texture("button_left.png");
        Texture buttonRight = new Texture("button_right.png");
        Texture buttonPower = new Texture("button_power.png");
        Texture buttonAim = new Texture("button_aim.png");

        this.leftImg = new Image(buttonLeft);
        this.leftImg.setSize(50f * scaler, 50f * scaler);
        this.leftImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                anyTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                anyTouched = false;
                return super.mouseMoved(event, x, y);
            }
        });

        this.rightImg = new Image(buttonRight);
        this.rightImg.setSize(50f * scaler, 50f * scaler);
        this.rightImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                anyTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                anyTouched = false;
                return super.mouseMoved(event, x, y);
            }
        });

        this.powerImg = new Image(buttonPower);
        this.powerImg.setSize(50f * scaler, 50f * scaler);
        this.powerImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                powerPressed = true;
                anyTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                powerPressed = false;
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                anyTouched = false;
                return super.mouseMoved(event, x, y);
            }
        });

        this.aimImg = new Image(buttonAim);
        this.aimImg.setSize(50f * scaler, 50f * scaler);
        this.aimImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                aimPressed = true;
                anyTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                aimPressed = false;
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                anyTouched = false;
                return super.mouseMoved(event, x, y);
            }
        });

        /* Projectile Image */
        this.projectileImg = new Image(this.projectileTextures.first());
        this.projectileImg.setSize(50f * scaler, 50f * scaler);

        this.projectileLeftImg = new Image(buttonLeft);
        this.projectileLeftImg.setSize(30f * scaler, 30f * scaler);
        this.projectileLeftImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                selectNewProjectile(false);
                anyTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                anyTouched = false;
                return super.mouseMoved(event, x, y);
            }
        });

        this.projectileRightImg = new Image(buttonRight);
        this.projectileRightImg.setSize(30f * scaler, 30f * scaler);
        this.projectileRightImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                selectNewProjectile(true);
                anyTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                anyTouched = false;
                return super.mouseMoved(event, x, y);
            }
        });

        // Create controller table as 1 row
        Table table = new Table();
        table.setName("controller"); // Id for getting this specific actor
        table.setWidth(Application.camera.viewportWidth);
        table.bottom().center();

        table.row().pad(5f * scaler, 5f * scaler, 70f * scaler, 5f * scaler);
        table.add(projectileLeftImg).size(projectileLeftImg.getWidth(), projectileLeftImg.getHeight());
        table.add(projectileImg).size(projectileImg.getWidth(), projectileImg.getHeight());
        table.add(projectileRightImg).size(projectileRightImg.getWidth(), projectileRightImg.getHeight());
        table.add(aimImg).size(aimImg.getWidth(), aimImg.getHeight()).padLeft(50f * scaler).padRight(50f * scaler);
        table.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        table.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight());
        table.add(powerImg).size(powerImg.getWidth(), powerImg.getHeight()).padLeft(100f * scaler).padRight(150f * scaler);

        table.setColor(table.getColor().r, table.getColor().g, table.getColor().b, table.getColor().a - 0.2f);

        return table;
    }

    // Change currently active projectile texture in controller menu
    private void selectNewProjectile(boolean next) {
        // Iterate projectile index, and check if array should restart iteration
        if (next) {
            this.currentProjectile++;
            if (this.currentProjectile >= numberOfProjectiles)
                this.currentProjectile = 0;
        } else {
            this.currentProjectile--;
            if (this.currentProjectile < 0)
                this.currentProjectile = numberOfProjectiles - 1;
        }

        projectileImg.setDrawable(new TextureRegionDrawable(this.projectileTextures.get(this.currentProjectile)));
    }

    // Increase transparency of color when an image should be disabled
    private void disableActor(Image image) {
        if (image.getColor().a > 0.51f)
            image.setColor(image.getColor().r, image.getColor().g, image.getColor().b, image.getColor().a - 0.5f);
    }

    // Revert transparency of color when an image should be disabled
    private void enableActor(Image image) {
        if (image.getColor().a < 0.51)
            image.setColor(image.getColor().r, image.getColor().g, image.getColor().b, image.getColor().a + 0.5f);
    }

    public void idle() {
        setTouchable(false);
        this.disableActor(this.powerImg);
        this.disableActor(this.leftImg);
        this.disableActor(this.rightImg);
        this.disableActor(this.aimImg);
        this.disableActor(this.projectileImg);
        this.disableActor(this.projectileLeftImg);
        this.disableActor(this.projectileRightImg);
    } // TODO: Change

    public void startMoving() {
        setTouchable(true);
        this.enableActor(this.leftImg);
        this.enableActor(this.rightImg);
        this.enableActor(this.aimImg);
        this.enableActor(this.projectileImg);
        this.enableActor(this.projectileLeftImg);
        this.enableActor(this.projectileRightImg);
        this.disableActor(this.powerImg);
    } // TODO: Change

    public void startShooting() {
        this.enableActor(this.powerImg);
        this.disableActor(this.leftImg);
        this.disableActor(this.rightImg);
        this.disableActor(this.aimImg);
    } // TODO: Change

    public void startTraining() {
        setTouchable(true);
        this.enableActor(this.aimImg);
        this.enableActor(this.projectileImg);
        this.enableActor(this.projectileLeftImg);
        this.enableActor(this.projectileRightImg);
        this.disableActor(this.powerImg);
    } // TODO: Change

    public void setVisible(boolean visible) {
        Array<Actor> actors = Application.stage.getActors();

        for (int i = 0; i < actors.size; i++) {
            actors.get(i).setVisible(visible);
        }
    }

    public void setTouchable(boolean touchable) {
        Array<Actor> actors = Application.stage.getActors();

        for (int i = 0; i < actors.size; i++) {
            if (touchable)
                actors.get(i).setTouchable(Touchable.enabled);
            else actors.get(i).setTouchable(Touchable.disabled);
        }
    }

    public boolean checkControllerIsTouched() {
        return anyTouched;
    }
}
