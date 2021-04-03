package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

	//Batches
	public static SpriteBatch batch;
	public ShapeRenderer shapeBatch;

	// Methods
	@Override
	public void create() {

		// Setup batches
		batch = new SpriteBatch();
		this.shapeBatch = new ShapeRenderer();

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
	public void dispose() {
		super.dispose();
		SM.dispose();
		batch.dispose();
		this.shapeBatch.dispose();
	}
}
