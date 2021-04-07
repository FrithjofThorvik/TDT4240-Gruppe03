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
import com.mygdx.game.managers.ScreenManager;

import static com.mygdx.game.managers.ScreenManager.SM;


/**
 * This is used for initializing all core mechanics for starting an application
 **/
public class Application extends Game {
    // Application Globals
    public static String APP_TITLE = "Projectile Wars";
    public static int APP_DESKTOP_WIDTH = 1600;    // Scaled
    public static int APP_DESKTOP_HEIGHT = 900;    // Scaled
    public static int APP_FPS = 60;

    // Game Globals
    public static int VIRTUAL_WORLD_WIDTH = 1600;    // Core
    public static int VIRTUAL_WORLD_HEIGHT = 900;    // Core

    // Batches & Stages
    public static SpriteBatch batch;
    public ShapeRenderer shapeBatch;
    public static OrthographicCamera camera;
    public static Stage stage;
    public static Viewport viewport;

    // Methods
    @Override
    public void create() {
        System.out.println("create");
        // Setup batches
        batch = new SpriteBatch();
        this.shapeBatch = new ShapeRenderer();

        // Initialize screen properties
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WORLD_WIDTH, VIRTUAL_WORLD_HEIGHT, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        shapeBatch.setProjectionMatrix(camera.combined);

        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage); // Add input processing for stage (Press of Button, Image, etc)

        new ScreenManager(this); // Create ScreenManager
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
        System.out.println("resize");
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
        camera.update();
    }

    @Override
    public void dispose() {
        super.dispose();

        SM.dispose();
        batch.dispose();
        stage.dispose();
        this.shapeBatch.dispose();
    }
}
