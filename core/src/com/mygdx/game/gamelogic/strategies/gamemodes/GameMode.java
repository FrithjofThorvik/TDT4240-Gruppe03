package com.mygdx.game.gamelogic.strategies.gamemodes;

/**
 * This interface is used for controlling game logic for different game modes
 * Most function relate to a state in the game state manager
 **/
public interface GameMode {
    void update(float dt);

    void startGame(); // Called in startgame state

    void endGame(); // Called in endGame state

    void startRound(); // Called in startRound state

    void playerAim(); // Called in playerAim state

    void playerShooting(); // Called in playerShooting state

    void projectileAirborne(); // Called in projectileAirborne state

    void switchRound(); // Called in switchRound state

    void updateUI(); // Update all UI elements

    void initEntities(); // Initialize the entities required for this gamemode
}
