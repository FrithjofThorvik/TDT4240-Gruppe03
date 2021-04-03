package com.mygdx.game.ECS;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.ECS.components.ControllerComponent;
import com.mygdx.game.states.screens.GameScreen;


/**
 * This is used for creating a controller
 **/
public class Controller {
    public ControllerComponent buttonPresses;

    // Prepare component mappers
    private final ComponentMapper<ControllerComponent> cm = ComponentMapper.getFor(ControllerComponent.class);

    public Controller(final Entity controllerEntity) {
        this.buttonPresses = cm.get(controllerEntity);

        Table table = new Table();
        table.setWidth(Gdx.graphics.getWidth());
        table.bottom().center();

        Image leftImg = new Image(new Texture("button_left.png"));
        leftImg.setSize(50, 50);
        leftImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                buttonPresses.leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                buttonPresses.leftPressed = false;

            }
        });

        Image rightImg = new Image(new Texture("button_right.png"));
        rightImg.setSize(50, 50);
        rightImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                buttonPresses.rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                buttonPresses.rightPressed = false;
            }
        });

        Image powerImg = new Image(new Texture("button_power.png"));
        powerImg.setSize(50, 50);
        powerImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                buttonPresses.powerPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                buttonPresses.powerPressed = false;
            }
        });

        Image aimImg = new Image(new Texture("button_aim.png"));
        aimImg.setSize(50, 50);
        aimImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                buttonPresses.aimPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                buttonPresses.aimPressed = false;
            }
        });

        table.row().pad(5, 5, 70, 5);
        table.add(aimImg).size(aimImg.getWidth(), aimImg.getHeight()).padRight(75);
        table.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        table.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight());
        table.add(powerImg).size(powerImg.getWidth(), powerImg.getHeight()).padLeft(75);

        GameScreen.stage.addActor(table); // Add table actor to GameScreen stage
    }
}
