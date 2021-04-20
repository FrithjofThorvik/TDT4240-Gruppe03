package com.mygdx.game.states.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Application;
import com.mygdx.game.managers.ScreenManager;
import com.sun.org.apache.xerces.internal.xs.ItemPSVI;

import static com.mygdx.game.managers.ScreenManager.SM;

public class LeaderboardScreen extends AbstractScreen {
    BitmapFont font;
    Array<Integer> highscore;
    int numberOfScoresShown = 5;

    public LeaderboardScreen(final Application app) {
        super(app);
    }

    @Override
    public void initScreen() {
        this.font = new BitmapFont();
        this.font.setColor(Color.WHITE);
        this.font.getData().setScale(2);
        Texture backgroundTexture = new Texture("mainmenu.png");

        // Initialise background
        Image background = new Image(backgroundTexture);
        background.setSize(Application.VIRTUAL_WORLD_WIDTH, Application.VIRTUAL_WORLD_HEIGHT);
        background.setPosition(
                (Application.camera.viewportWidth) - (background.getWidth()),
                (Application.camera.viewportHeight) - (background.getHeight())
        );
        Application.stage.addActor(background);
        addReturnButton();
        highscore = new Array<>();
    }

    @Override
    public void endScreen() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        super.render(delta); // Super.render(delta) sets BG_Color and calls update(float delta)
        Application.stage.draw();
        Application.batch.begin();

        highscore = Application._FBIC.GetHighScore();
        highscore.sort();
        highscore.reverse();

        // Print 5 highest player scores
        for (int i = 0; i < numberOfScoresShown; i++) {
            this.font.draw(Application.batch, "Score " + (i + 1) + ": " + highscore.get(i), Application.camera.viewportWidth / 2.4f, Application.camera.viewportHeight / 1.7f - i * 50f);
        }
        Application.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
