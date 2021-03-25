package com.mygdx.game.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Application;
import com.mygdx.game.managers.EntityManager;

import static com.mygdx.game.utils.B2DConstants.PPM;

public class GameScreen extends AbstractScreen {

    // Camera
    public static OrthographicCamera camera;

    // Box2D
    public World world;
    public Box2DDebugRenderer b2dr;

    //ECS
    private final EntityManager entityManager;

    public GameScreen(final Application app) {
        super(app); // Passing Application to AbstractScreen

        // Set camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Application.V_WIDTH, Application.V_HEIGHT);

        // Adjust app batches to the camera view (combined = viewport matrix)
        app.batch.setProjectionMatrix(camera.combined);
        app.shapeBatch.setProjectionMatrix(camera.combined);

        // Create world
        this.b2dr = new Box2DDebugRenderer();
        this.world = new World(new Vector2(0f, -0.98f), false);

        //Setup ECS
        Engine engine = new Engine();
        entityManager = new EntityManager(engine, app.batch, this.world);
    }

    @Override
    public void show() {
        app.batch.setProjectionMatrix(camera.combined);
        app.shapeBatch.setProjectionMatrix(camera.combined);
    }
    @Override
    public void update(float delta) {
        this.world.step(1f / Application.APP_FPS, 6, 2);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        this.b2dr.render(this.world, camera.combined.cpy().scl(PPM));

        //Begin the batch and let the entityManager handle the rest :)
        app.batch.begin();
        entityManager.update();
        app.batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
    }
}
