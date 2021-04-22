package com.mygdx.game.gamelogic.states.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.Application;
import com.mygdx.game.gamelogic.states.GameStateManager;
import com.mygdx.game.gamelogic.states.ScreenManager;
import com.mygdx.game.utils.GameConstants;



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
        Texture leaderboardTexture = new Texture("leaderboard.png");
        Texture singlePlayerTexture = new Texture("singlePlayer.png");
        Texture backgroundTexture = new Texture("mainmenu.png");

        // Initialise background
        Image background = new Image(backgroundTexture);
        background.setSize(GameConstants.VIRTUAL_WORLD_WIDTH, GameConstants.VIRTUAL_WORLD_HEIGHT);
        background.setPosition(
                (Application.camera.viewportWidth) - (background.getWidth()),
                (Application.camera.viewportHeight) - (background.getHeight())
        );

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
                ScreenManager.getInstance(null).removeAllActors(); // Removes all current actors from Application.stage

                GameStateManager.getInstance().setGameMode(GameStateManager.GAMEMODE.LOCAL);
                ScreenManager.getInstance(null).setScreen(com.mygdx.game.gamelogic.states.ScreenManager.STATE.PLAY);
            }
        });
        playImg.setPosition(
                (Application.camera.viewportWidth / 2f) - (playImg.getWidth() / 2f),
                (Application.camera.viewportHeight / 2f) - (playImg.getHeight() / 2f)
        );

        // Initialise single player button
        Image singlePlayerImg = new Image(singlePlayerTexture);
        singlePlayerImg.setSize(300f, 300f);
        singlePlayerImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance(app).removeAllActors(); // Removes all current actors from Application.stage

                GameStateManager.getInstance().setGameMode(GameStateManager.GAMEMODE.TRAINING);
                ScreenManager.getInstance(app).setScreen(com.mygdx.game.gamelogic.states.ScreenManager.STATE.PLAY);
            }
        });
        singlePlayerImg.setPosition(
                (Application.camera.viewportWidth / 2f) - (singlePlayerImg.getWidth() / 2f),
                (Application.camera.viewportHeight / 4f) - (singlePlayerImg.getHeight() / 2f)
        );

        // Initialize Leaderboard button
        Image leaderboardImg = new Image(leaderboardTexture);
        leaderboardImg.setSize(100f, 100f);
        leaderboardImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance(app).removeAllActors(); // Removes all current actors from Application.stage
                ScreenManager.getInstance(app).setScreen(ScreenManager.STATE.LEADERBOARD);
            }
        });
        leaderboardImg.setPosition(
                (Application.camera.viewportWidth) - (leaderboardImg.getWidth()),
                (Application.camera.viewportHeight) - (leaderboardImg.getHeight())
        );

        Application.stage.addActor(background);
        Application.stage.addActor(singlePlayerImg);
        Application.stage.addActor(playImg);
        Application.stage.addActor(leaderboardImg);

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
