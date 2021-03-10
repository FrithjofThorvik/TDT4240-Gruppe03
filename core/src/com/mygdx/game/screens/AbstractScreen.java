package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.Application;

public abstract class AbstractScreen implements Screen {

    protected final Application app; // Only one instance of app (final)

    public AbstractScreen(final Application app) {
        this.app = app;
    }

    public abstract void update(float delta); // Abstract function: (Template Method)

    @Override
    public void render(float delta) {
        // Calls update for our subclasses
        update(delta);

        // Renders background color for all screens
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override public void resize(int width, int height) {}

    @Override public void pause() {}

    @Override public void resume() {}

    @Override public void hide() {}

    @Override public void dispose() {}
}
