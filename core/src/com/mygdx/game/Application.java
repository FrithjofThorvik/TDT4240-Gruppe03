package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.managers.GameScreenManager;


public class Application extends Game {

	// Application Globals
	public static String APP_TITLE = "Projectile Wars";
	public static int APP_DESKTOP_WIDTH = 720; 	// Scaled
	public static int APP_DESKTOP_HEIGHT = 420;	// Scaled
	public static int APP_FPS = 60;

	// Game Globals
	public static int V_WIDTH = 720;	// Core
	public static  int V_HEIGHT = 420;	// Core

	// Managers
	public GameScreenManager gsm;

	//Batches
	public SpriteBatch batch;
	public ShapeRenderer shapeBatch;

	// Methods
	@Override
	public void create() {

		// Setup batches
		batch = new SpriteBatch();
		shapeBatch = new ShapeRenderer();

		// Setup managers
		gsm = GameScreenManager.getGameScreenManager(this);
	}

	@Override
	public void render() {
		super.render();

		// Exit app when pressing ESC button
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
			Gdx.app.exit();
	}

	@Override
	public void dispose() {
		super.dispose();

		// Dispose this class's objects (Remember order of disposing)
		batch.dispose();
		shapeBatch.dispose();
		gsm.dispose();
	}
}
