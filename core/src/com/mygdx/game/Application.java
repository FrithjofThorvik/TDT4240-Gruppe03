package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ECS.managers.ECSManager;
import com.mygdx.game.gamelogic.states.GameStateManager;
import com.mygdx.game.gamelogic.states.ScreenManager;
import com.mygdx.game.firebase.FirebaseInterface;
import com.mygdx.game.utils.GameConstants;
import com.mygdx.game.utils.GameController;


/**
 * This is used for initializing all core mechanics for starting an application
 **/
public class Application extends Game {
    // Batches & Stages
    public static SpriteBatch batch;
    public ShapeRenderer shapeBatch;
    public static OrthographicCamera camera;
    public static Stage stage;
    public static Viewport viewport;

    //firebase
    public static FirebaseInterface _FBIC;


    public Application(FirebaseInterface FBIC) {
        _FBIC = FBIC;
    }

    // Methods
    @Override
    public void create() {
        // Setup batches
        batch = new SpriteBatch();
        this.shapeBatch = new ShapeRenderer();

        //firebase
        _FBIC.SetOnValueChangedListener();

        // Initialize screen properties
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConstants.VIRTUAL_WORLD_WIDTH, GameConstants.VIRTUAL_WORLD_HEIGHT, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        shapeBatch.setProjectionMatrix(camera.combined);

        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage); // Add input processing for stage (Press of Button, Image, etc)

        ScreenManager.getInstance(this); // Create ScreenManager
        GameStateManager.getInstance(); // Create GameStateManager
        ECSManager.getInstance(); // Manager for generating all ECS functions
        GameController.getInstance(); // Manages all game controls
    }

    @Override
    public void render() {
        super.render();

        // Exit app when pressing ESC button
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            this.dispose();
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void dispose() {
        super.dispose();

        ScreenManager.getInstance(Application.this).dispose();
        batch.dispose();
        stage.dispose();
        this.shapeBatch.dispose();
    }
}
