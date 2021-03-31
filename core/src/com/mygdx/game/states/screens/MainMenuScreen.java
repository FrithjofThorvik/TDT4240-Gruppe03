package com.mygdx.game.states.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Application;
import com.mygdx.game.managers.ScreenManager;

public class MainMenuScreen extends AbstractScreen {

    // Texture
    Texture playBtn;

    public MainMenuScreen(final Application app) {
        super(app);

        // Initialise Play Button
        this.playBtn = new Texture("play.png");
    }

    private void handleInput() {
        if (Gdx.input.isTouched())
            app.screenManager.setScreen(ScreenManager.STATE.PLAY);
    }

    @Override
    public void update(float delta) {
        this.handleInput();
    }

    @Override
    public void render(float delta) {
        // Super.render(delta) sets BG_Color and calls update(float delta)
        super.render(delta);

        // Draw Play Button in the middle of the screen
        this.app.batch.begin();
        this.app.batch.draw(
                this.playBtn,
                (Application.V_WIDTH / 2f) - (playBtn.getWidth() / 2f),
                (Application.V_HEIGHT / 2f) - (playBtn.getHeight() / 2f)
        );
        this.app.batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        playBtn.dispose();
    }

    @Override
    public void show() {
    }
}
