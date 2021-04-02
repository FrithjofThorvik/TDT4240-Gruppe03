package com.mygdx.game.states.screens;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameStateManager;

import static com.mygdx.game.managers.EntityManager.EM;
import static com.mygdx.game.managers.GameStateManager.GSM;

public class EndScreen extends AbstractScreen {


    public EndScreen(Application app) {
        super(app);
    }

    @Override
    public void update(float delta) {
        // Handle input
        if (Gdx.input.justTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY(); // Input y is inverted compared to position y

            PositionComponent exitButtonPosition = EntityManager.exitButton.getComponent(PositionComponent.class);
            FontComponent exitButtonFont = EntityManager.exitButton.getComponent(FontComponent.class);

            PositionComponent restartButtonPosition = EntityManager.restartButton.getComponent(PositionComponent.class);
            FontComponent restartButtonFont = EntityManager.restartButton.getComponent(FontComponent.class);

            // Check if exit button is pressed
            if (
                    (int) exitButtonPosition.position.x - (int) exitButtonFont.layout.width / 1f <= x &&
                            (int) exitButtonPosition.position.x + (int) exitButtonFont.layout.width / 1f >= x &&
                            (int) exitButtonPosition.position.y - (int) exitButtonFont.layout.height / 1f <= y &&
                            (int) exitButtonPosition.position.y + (int) exitButtonFont.layout.height / 1f >= y
            ) {
                System.out.println("Exiting to main menu...");
                GSM.setGameState(GameStateManager.STATE.EXIT_GAME);
            }

            // Check if restart button is pressed
            else if (
                    (int) restartButtonPosition.position.x - (int) restartButtonFont.layout.width / 1f <= x &&
                            (int) restartButtonPosition.position.x + (int) restartButtonFont.layout.width / 1f >= x &&
                            (int) restartButtonPosition.position.y - (int) restartButtonFont.layout.height / 1f <= y &&
                            (int) restartButtonPosition.position.y + (int) restartButtonFont.layout.height / 1f >= y
            ) {
                System.out.println("Restarting game...");
                GSM.setGameState(GameStateManager.STATE.RESTART_GAME);
            }

        }
    }

    @Override
    public void render(float dt) {
        // Super.render(delta) sets BG_Color and calls update(float delta)
        super.render(dt);

        //this.render(GameScreen.world, camera.combined.cpy().scl(PPM));

        //Begin the batch and let the entityManager handle the rest :)
        app.batch.begin();
        GSM.update(dt);
        EM.update(dt);
        app.batch.end();
    }

    @Override
    public void show() {
        app.batch.setProjectionMatrix(GameScreen.camera.combined);
        app.shapeBatch.setProjectionMatrix(GameScreen.camera.combined);
    }
}
