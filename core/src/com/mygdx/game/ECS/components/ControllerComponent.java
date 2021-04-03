package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Application;
import com.mygdx.game.states.screens.GameScreen;


/**
 * This component is for using the controller UI in the game
 * This will create a new stage, viewport, and camera for listening to the right inputs
 * The public boolean values will be used in the ControllerSystem
 **/
public class ControllerComponent implements Component {
    Stage stage;
    Viewport viewport;
    OrthographicCamera camera;

    public boolean leftPressed, rightPressed, powerPressed;

    public ControllerComponent() {
        this.camera = GameScreen.camera;
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), this.camera);
        this.stage = new Stage(this.viewport, Application.batch);

        Gdx.input.setInputProcessor(this.stage);

        Table table = new Table();
        table.setWidth(Gdx.graphics.getWidth());
        table.bottom().center();


        Image leftImg = new Image(new Texture("button_left.png"));
        leftImg.setSize(50, 50);
        leftImg.addListener(new InputListener() {
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

        Image rightImg = new Image(new Texture("button_right.png"));
        rightImg.setSize(50, 50);
        rightImg.addListener(new InputListener() {
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

        Image powerImg = new Image(new Texture("button_power.png"));
        powerImg.setSize(50, 50);
        powerImg.addListener(new InputListener() {
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

        table.row().pad(5, 5, 70, 5);
        table.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        table.add(powerImg).size(powerImg.getWidth(), powerImg.getHeight());
        table.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight());

        this.stage.addActor(table);
    }

    public void draw() {
        this.stage.draw();
    }

    /* public void resize(int width, int height){
        this.viewport.update(width, height);
    } */
}
