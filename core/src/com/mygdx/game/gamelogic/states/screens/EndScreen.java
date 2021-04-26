package com.mygdx.game.gamelogic.states.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.Application;
import com.mygdx.game.gamelogic.states.ScreenManager;



/**
 * This screen will give the options of exiting to the main menu, or restart the game
 **/
public class EndScreen extends AbstractScreen {


    public EndScreen(Application app) {
        super(app);
    }

    @Override
    public void initScreen() {
        Texture restartTexture = new Texture("button_restart.png");
        Texture exitTexture = new Texture("button_exit.png");

        Table table = new Table();
        table.setWidth(Application.camera.viewportWidth);
        table.center();

        // Initialise Restart Button
        Image restartImg = new Image(restartTexture);
        restartImg.setSize(75f, 75f);
        restartImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance(app).setScreen(com.mygdx.game.gamelogic.states.ScreenManager.STATE.PLAY);
            }
        });

        // Initialise Exit Button
        Image exitImg = new Image(exitTexture);
        exitImg.setSize(75f, 75f);
        exitImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance(app).setScreen(ScreenManager.STATE.MAIN_MENU);
            }
        });

        table.row().pad(5, 5, 100, 5);
        table.add(exitImg).size(exitImg.getWidth(), exitImg.getHeight());
        table.add(restartImg).size(restartImg.getWidth(), restartImg.getHeight());

        Application.stage.addActor(table); // Add table actor to GameScreen stage

    }

    @Override
    public void endScreen() {}

    @Override
    public void update(float delta) {}

    @Override
    public void render(float dt) {
        // Super.render(delta) sets BG_Color and calls update(float delta)
        super.render(dt);

        Application.stage.draw();
    }

    @Override
    public void show() {}
}
