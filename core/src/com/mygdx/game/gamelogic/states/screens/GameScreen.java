package com.mygdx.game.gamelogic.states.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.managers.ECSManager;
import com.mygdx.game.utils.CollisionHandler;
import com.mygdx.game.utils.GameController;
import com.mygdx.game.gamelogic.states.GameStateManager;

import static com.mygdx.game.gamelogic.states.GameStateManager.GSM;
import static com.mygdx.game.ECS.managers.ECSManager.ECSManager;
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
        world = new World(new Vector2(0, -10f), false);
    }

    @Override
    public void initScreen() {
        System.out.println("Initializing GameScreen...");
        addReturnButton();
    }

    @Override
    public void endScreen() {
    }

    @Override
    public void show() {
        //new GameStateManager();
        new ECSManager(Application.batch); // Manager for generating all ECS functions
        new GameController(); // Manages all game controls

        //GSM.setGameMode(GameStateManager.GAMEMODE.LOCAL);

        ECSManager.createModeEntities();
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
        ECSManager.update(dt);
        Application.batch.end();
        Application.stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        //ECSManager.dispose();
        world.dispose();
    }
}
