package com.mygdx.game.managers;

import com.mygdx.game.Application;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.MainMenuScreen;

import java.util.HashMap;

public class GameScreenManager {

    // Singleton instance of GameScreenManager
    static GameScreenManager gsm;

    // Pass through application
    public final Application app;

    // Create defined screen states
    public enum STATE {
        MAIN_MENU,
        PLAY,
        SETTINGS
    }

    // Store screens in HashMap
    private HashMap<STATE, AbstractScreen> gameScreens;

    // Passes the single instance of the app
    private GameScreenManager(final Application app) {
        this.app = app;

        initGameScreen();
        setScreen(STATE.MAIN_MENU);
    }

    // Create singleton instance
    public static GameScreenManager getGameScreenManager(Application app) {
        if (gsm == null) {
            gsm = new GameScreenManager(app);
        }
        return gsm;
    }

    // Initialize all screens
    private void initGameScreen() {
        this.gameScreens = new HashMap<STATE, AbstractScreen>();
        this.gameScreens.put(STATE.PLAY, new GameScreen(app));          // Creates GameScreen
        this.gameScreens.put(STATE.MAIN_MENU, new MainMenuScreen(app)); // Creates MainMenuScreen
    }

    // Set predefined screen
    public void setScreen(STATE nextScreen) {
        app.setScreen(gameScreens.get(nextScreen));
    }

    // Run dispose() on all screens
    public void dispose() {
        for (AbstractScreen screen : gameScreens.values()) {
            if (screen != null)
                screen.dispose();
        }
    }
}
