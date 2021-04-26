package com.mygdx.game.utils;


/**
 * This is used for storing the game's constants
 **/
public final class GameConstants {
    private GameConstants() {}

    // Application Globals
    public static String APP_TITLE = "Projectile Wars";
    public static int APP_DESKTOP_WIDTH = 1600;    // Scaled
    public static int APP_DESKTOP_HEIGHT = 900;    // Scaled
    public static int APP_FPS = 60;

    // Game Globals
    public static int VIRTUAL_WORLD_WIDTH = 1600;    // Core
    public static int VIRTUAL_WORLD_HEIGHT = 900;    // Core

    public static float START_GAME_TIME = 3; // How much time should pass before first round can start
    public static float TIME_BETWEEN_ROUNDS = 2; // How much time for players to prepare between rounds
    public static float ROUND_TIME = 15; // How much time does the player have to move and shoot
    public static float MAX_SHOOTING_POWER = 2; // How much power can the player use
}
