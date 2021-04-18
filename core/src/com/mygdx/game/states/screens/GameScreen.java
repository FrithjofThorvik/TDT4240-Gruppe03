package com.mygdx.game.states.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.CollisionHandler;
import com.mygdx.game.managers.ControlManager;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameStateManager;

import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.utils.B2DConstants.PPM;


/**
 * This screen is used for running the Projectile Wars game
 **/
public class GameScreen extends AbstractScreen {

    public static World world;
    public Box2DDebugRenderer b2dr;

    public GameScreen(final Application app) {
        super(app); // Passing Application to AbstractScreen

        // Create Box2D world with physics
        this.b2dr = new Box2DDebugRenderer();
        this.world = new World(new Vector2(0, -10f), false);
    }

    @Override
    public void initScreen() {
        System.out.println("Initializing GameScreen...");
    }

    @Override
    public void endScreen() {
    }

    @Override
    public void show() {
        new EntityManager(Application.batch); // Manager for generating all ECS functions
        new GameStateManager(); // Manager for handling all game states
        new ControlManager(); // Manages all game controls

        GSM.setGameMode(GameStateManager.GAMEMODE.LOCAL);

        EM.createModeEntities();
        GSM.setGameState(GameStateManager.STATE.START_GAME);

        world.setContactListener(new CollisionHandler()); // Set contact listener for world
    }

    @Override
    public void update(float dt) {
        world.step(1f / Application.APP_FPS, 6, 2);
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        this.b2dr.render(world, Application.camera.combined.cpy().scl(PPM));

        // Draw to screen
        Application.batch.begin();
        GSM.update(dt);
        EM.update(dt);
        Application.batch.end();
        Application.stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        EM.dispose();
        world.dispose();
    }
}
