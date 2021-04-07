package com.mygdx.game.states.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.Application;
import com.mygdx.game.managers.ScreenManager;

import static com.mygdx.game.managers.ScreenManager.SM;


/**
 * This screen will display buttons for going to different screens
 **/
public class MainMenuScreen extends AbstractScreen {

    public MainMenuScreen(final Application app) {
        super(app);
    }

    @Override
    public void initScreen() {
        Texture playTexture = new Texture("play.png");

        // Initialise Play Button
        Image playImg = new Image(playTexture);
        playImg.setSize(100f, 100f);
        playImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                SM.setScreen(ScreenManager.STATE.PLAY);
            }
        });
        playImg.setPosition(
                (Application.camera.viewportWidth / 2f) - (playImg.getWidth() / 2f),
                (Application.camera.viewportHeight / 2f) - (playImg.getHeight() / 2f)
        );

        Application.stage.addActor(playImg); // Add table actor to GameScreen stage
    }

    @Override
    public void endScreen() {}

    @Override
    public void update(float dt) {}

    @Override
    public void render(float dt) {
        // Super.render(delta) sets BG_Color and calls update(float delta)
        super.render(dt);

        Application.stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void show() {}
}
