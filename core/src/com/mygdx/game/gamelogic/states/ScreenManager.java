package com.mygdx.game.gamelogic.states;

import com.mygdx.game.Application;
import com.mygdx.game.gamelogic.states.screens.AbstractScreen;
import com.mygdx.game.gamelogic.states.screens.EndScreen;
import com.mygdx.game.gamelogic.states.screens.GameScreen;
import com.mygdx.game.gamelogic.states.screens.LeaderboardScreen;
import com.mygdx.game.gamelogic.states.screens.MainMenuScreen;

import java.util.HashMap;


/**
 * This is a manager for screens
 * This will initialize and be used to set the existing screens
 * TODO: Implement Tutorial screen
 **/
public class ScreenManager {
    public static ScreenManager SM; // Singleton instance of GameScreenManager
    public final Application app; // Pass through application
    public static AbstractScreen screen;

    // Create defined screen states
    public enum STATE {
        MAIN_MENU,
        PLAY,
        END_SCREEN,
        LEADERBOARD
    }

    // Store screens in HashMap
    private HashMap<STATE, AbstractScreen> screens;

    // Passes the single instance of the app
    public ScreenManager(final Application app) {
        SM = this;
        this.app = app;

        this.initScreens();
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
    private void initScreens() {
        this.screens = new HashMap<STATE, AbstractScreen>();
        this.screens.put(STATE.MAIN_MENU, new MainMenuScreen(app)); // Creates MainMenuScreen
        this.screens.put(STATE.END_SCREEN, new EndScreen(app)); // Creates EndScreen
        this.screens.put(STATE.PLAY, new GameScreen(app));  // Creates new GameScreen every time we change to the game screen
        this.screens.put(STATE.LEADERBOARD, new LeaderboardScreen(app)); // Displays leaderboard information
    }

    // Set predefined screen
    public void setScreen(STATE nextScreen) {
        this.removeAllActors(); // Removes all current actors from Application.stage

        if (nextScreen == STATE.PLAY) // Creates new GameScreen every time we change to the game screen
            this.screens.put(STATE.PLAY, new GameScreen(app));

        this.app.setScreen(this.screens.get(nextScreen));
        screen = this.screens.get(nextScreen);
        screen.initScreen();
    }

    // Run dispose() on all screens
    public void dispose() {
        for (AbstractScreen screen : this.screens.values()) {
            if (screen != null)
                screen.dispose();
        }
    }

    // Remove all current actors
    public void removeAllActors() {
        Application.stage.getActors().clear();
    }
}
