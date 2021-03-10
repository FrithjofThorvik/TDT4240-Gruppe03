package com.mygdx.game.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Application;
import com.mygdx.game.objects.Ball;
import com.mygdx.game.objects.Wall;
import com.mygdx.game.objects.Paddle;
import com.mygdx.game.utils.B2DContactListener;

import static com.mygdx.game.utils.B2DConstants.PPM;

public class GameScreen extends AbstractScreen {

    // Camera
    public OrthographicCamera camera;
    private final float width;
    private final float height;

    // Box2D
    public World world;
    public Box2DDebugRenderer b2dr;

    // Objects
    Ball ball;
    Paddle paddleLeft;
    Paddle paddleRight;
    Wall playerGoal;
    Wall enemyGoal;
    Wall wallTop;
    Wall wallBottom;


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
        // Create Ball
        this.ball = new Ball(this.world, this.width / 2, this.height / 2, 6f, "ball");

        // Create Paddles
        this.paddleLeft = new Paddle(this.world, 10f, this.height / 2, 10f, 60f, "paddle");
        this.paddleRight = new Paddle(this.world, this.width - 10f, this.height / 2, 10f, 60f, "paddle");

        // Create Goals
        this.playerGoal = new Wall(this.world, 0f, 0f, 0f, this.height, "playerGoal");
        this.enemyGoal = new Wall(this.world, this.width + 1f, 0f, this.width + 1f, this.height, "enemyGoal");

        // Create Walls
        this.wallTop = new Wall(this.world,0f, this.height, this.width, this.height, "wall");
        this.wallBottom = new Wall(this.world,-1f, 0f, this.width, -1f, "wall");

        // Create ContactListener
        this.world.setContactListener(new B2DContactListener(this.ball));

        app.batch.setProjectionMatrix(camera.combined);
        app.shapeBatch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void update(float delta) {
        this.world.step(1f / Application.APP_FPS, 6, 2);
        this.ball.move();
        this.paddleRight.move(this.ball.velocity.y);
        this.paddleLeft.move(this.ball.velocity.y);
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
