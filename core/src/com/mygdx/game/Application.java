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
	public static int APP_DESKTOP_WIDTH = 720; 	// Scaled
	public static int APP_DESKTOP_HEIGHT = 420;	// Scaled
	public static int APP_FPS = 60;

	// Game Globals
	public static int V_WIDTH = 720;	// Core
	public static  int V_HEIGHT = 420;	// Core

	// Batches & Stages
	public static SpriteBatch batch;
	public ShapeRenderer shapeBatch;
	public static OrthographicCamera camera;
	public static Stage stage;
	public static Viewport viewport;

	// Methods
	@Override
	public void create() {
		// Setup batches
		batch = new SpriteBatch();
		this.shapeBatch = new ShapeRenderer();

		// Initialize screen properties
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Application.V_WIDTH, Application.V_HEIGHT);

		batch.setProjectionMatrix(camera.combined);
		shapeBatch.setProjectionMatrix(camera.combined);

		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
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
		viewport.update(width, height);
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
