package com.mygdx.game.managers;

import com.mygdx.game.Application;
import com.mygdx.game.states.screens.AbstractScreen;
import com.mygdx.game.states.screens.GameScreen;
import com.mygdx.game.states.screens.MainMenuScreen;

import java.util.HashMap;


/**
 * This is a manager for screens
 * This will initialize and be used to set the existing screens
 * TODO: Implement Tutorial screen
 **/
public class ScreenManager {
    public static ScreenManager SM; // Singleton instance of GameScreenManager
    public final Application app; // Pass through application

    // Create defined screen states
    public enum STATE {
        MAIN_MENU,
        PLAY
        // TODO: Implement TUTORIAL
    }

    // Store screens in HashMap
    private HashMap<STATE, AbstractScreen> screens;

    // Passes the single instance of the app
    public ScreenManager(final Application app) {
        SM = this;
        this.app = app;

        this.initScreen();
        this.setScreen(STATE.MAIN_MENU);
    }

    // Create singleton instance
    public static ScreenManager getScreenManager(Application app) {
        if (SM == null) {
            SM = new ScreenManager(app);
        }
        return SM;
    }

    // Initialize all screens
    private void initScreen() {
        this.screens = new HashMap<STATE, AbstractScreen>();
        this.screens.put(STATE.PLAY, new GameScreen(app));          // Creates GameScreen
        this.screens.put(STATE.MAIN_MENU, new MainMenuScreen(app)); // Creates MainMenuScreen
    }

    // Set predefined screen
    public void setScreen(STATE nextScreen) {
        this.app.setScreen(this.screens.get(nextScreen));
    }

    // Run dispose() on all screens
    public void dispose() {
        for (AbstractScreen screen : this.screens.values()) {
            if (screen != null)
                screen.dispose();
        }
    }
}
