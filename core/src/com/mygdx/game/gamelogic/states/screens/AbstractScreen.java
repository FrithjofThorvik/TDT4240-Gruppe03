package com.mygdx.game.gamelogic.states.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.Application;
import com.mygdx.game.gamelogic.states.ScreenManager;

import static com.mygdx.game.gamelogic.states.ScreenManager.SM;

/**
 * Contain the template for how screens are implemented
 * Create new screen types by inheriting from this class
 **/
public abstract class AbstractScreen implements Screen {

    protected final Application app; // Only one instance of app (final)

    public AbstractScreen(final Application app) {
        this.app = app;
    }

    public abstract void initScreen(); // Abstract function: (Template Method)
    public abstract void endScreen(); // Abstract function: (Template Method)
    public abstract void update(float delta); // Abstract function: (Template Method)
    public void addReturnButton(){
        // Initialise return Button
        Texture exitTexture = new Texture("button_exit.png");
        Image returnImg = new Image(exitTexture);
        returnImg.setSize(50f, 50f);
        returnImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                SM.removeAllActors(); // Removes all current actors from Application.stage
                SM.setScreen(ScreenManager.STATE.MAIN_MENU);
            }
        });
        returnImg.setPosition(
                (Application.camera.viewportWidth) - (returnImg.getWidth() + 20f),
                (Application.camera.viewportHeight) - (returnImg.getHeight() + 20f)
        );
        Application.stage.addActor(returnImg); // Add table actor to GameScreen stage
    }

    @Override
    public void render(float delta) {
        // Calls update for our subclasses
        update(delta);

        // Renders background color for all screens
        Gdx.gl.glClearColor(137, 209, 254, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
