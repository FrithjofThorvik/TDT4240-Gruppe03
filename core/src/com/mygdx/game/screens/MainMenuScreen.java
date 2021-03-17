package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Application;
import com.mygdx.game.managers.GameScreenManager;

import static com.mygdx.game.utils.B2DConstants.PPM;

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
            app.gsm.setScreen(GameScreenManager.STATE.PLAY);
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
        this.app.batch.draw(this.playBtn, (Application.V_WIDTH / 2) - (playBtn.getWidth() / 2), (Application.V_HEIGHT / 2) - (playBtn.getHeight() / 2));
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
