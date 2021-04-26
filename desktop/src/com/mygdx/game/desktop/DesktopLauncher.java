package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Application;
import com.mygdx.game.utils.GameConstants;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new Application(new DesktopInterFaceClass()), config);
        config.title = GameConstants.APP_TITLE;
        config.width = GameConstants.APP_DESKTOP_WIDTH;
        config.height = GameConstants.APP_DESKTOP_HEIGHT;
        config.backgroundFPS = GameConstants.APP_FPS;
        config.foregroundFPS = GameConstants.APP_FPS;
    }
}