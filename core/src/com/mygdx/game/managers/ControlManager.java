package com.mygdx.game.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Application;

import static com.mygdx.game.managers.GameStateManager.GSM;


/**
 * This is used for creating a controller
 **/
public class ControlManager {
    public static ControlManager CM;
    private int currentProjectile = 0;
    public boolean leftPressed, rightPressed, aimPressed, powerPressed = false;
    private final Array<Texture> projectileTextures;

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
        this.projectileTextures.add(new Texture("play.png"));
        this.projectileTextures.add(new Texture("cannonball.png"));
        this.projectileTextures.add(new Texture("badlogic.jpg"));

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
        this.leftImg.setSize(50, 50);
        this.leftImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;

            }
        });

        this.rightImg = new Image(buttonRight);
        this.rightImg.setSize(50, 50);
        this.rightImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        this.powerImg = new Image(buttonPower);
        this.powerImg.setSize(50, 50);
        this.powerImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                powerPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                powerPressed = false;
            }
        });

        this.aimImg = new Image(buttonAim);
        this.aimImg.setSize(50, 50);
        this.aimImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                aimPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                aimPressed = false;
            }
        });

        /* Projectile Image */
        this.projectileImg = new Image(this.projectileTextures.first());
        this.projectileImg.setSize(50, 50);

        this.projectileLeftImg = new Image(buttonLeft);
        this.projectileLeftImg.setSize(30, 30);
        this.projectileLeftImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                selectNewProjectile(false);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {}
        });

        this.projectileRightImg = new Image(buttonRight);
        this.projectileRightImg.setSize(30, 30);
        this.projectileRightImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                selectNewProjectile(true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {}
        });

        // Create controller table as 1 row
        Table table = new Table();
        table.setName("controller"); // Id for getting this specific actor
        table.setWidth(Gdx.graphics.getWidth());
        table.bottom().center();

        table.row().pad(5, 5, 70, 5);
        table.add(projectileLeftImg).size(projectileLeftImg.getWidth(), projectileLeftImg.getHeight());
        table.add(projectileImg).size(projectileImg.getWidth(), projectileImg.getHeight());
        table.add(projectileRightImg).size(projectileRightImg.getWidth(), projectileRightImg.getHeight());
        table.add(aimImg).size(aimImg.getWidth(), aimImg.getHeight()).padLeft(50).padRight(50);
        table.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        table.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight());
        table.add(powerImg).size(powerImg.getWidth(), powerImg.getHeight()).padLeft(100).padRight(150);

        table.setColor(table.getColor().r, table.getColor().g, table.getColor().b, table.getColor().a - 0.2f);

        return table;
    }

    // Change currently active projectile texture in controller menu
    private void selectNewProjectile(boolean next) {
        // Iterate projectile index, and check if array should restart iteration
        if (next) {
            this.currentProjectile++;
            if (this.currentProjectile >= this.projectileTextures.size)
                this.currentProjectile = 0;
        } else {
            this.currentProjectile--;
            if (this.currentProjectile < 0)
                this.currentProjectile = this.projectileTextures.size - 1;
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
        this.disableActor(this.powerImg);
        this.disableActor(this.leftImg);
        this.disableActor(this.rightImg);
        this.disableActor(this.aimImg);
        this.disableActor(this.projectileImg);
        this.disableActor(this.projectileLeftImg);
        this.disableActor(this.projectileRightImg);
    } // TODO: Change

    public void startMoving() {
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
        //this.disableActor(this.projectileImg);
        //this.disableActor(this.projectileLeftImg);
        //this.disableActor(this.projectileRightImg);
    } // TODO: Change
}
