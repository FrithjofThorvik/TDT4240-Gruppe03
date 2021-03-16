package com.mygdx.game.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Application;

import static com.mygdx.game.utils.B2DConstants.PPM;

public class GameScreen extends AbstractScreen {

    // Camera
    public OrthographicCamera camera;
    private final float width;
    private final float height;

    // Box2D
    public World world;
    public Box2DDebugRenderer b2dr;


    public GameScreen(final Application app) {
        super(app); // Passing Application to AbstractScreen

        // Set camera
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Application.V_WIDTH, Application.V_HEIGHT);
        this.width = camera.viewportWidth;
        this.height = camera.viewportHeight;

        // Adjust app batches to the camera view (combined = viewport matrix)
        app.batch.setProjectionMatrix(camera.combined);
        app.shapeBatch.setProjectionMatrix(camera.combined);

        // Create world
        this.b2dr = new Box2DDebugRenderer();
        this.world = new World(new Vector2(0f, 0f), false);
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
        this.b2dr.render(this.world, this.camera.combined.cpy().scl(PPM));
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
    }
}
