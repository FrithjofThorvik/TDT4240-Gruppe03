package com.mygdx.game.ECS.States;

import java.text.DecimalFormat;

// This class contain logic for switching state, and contains variables that the state is dependent on
public class Context {
    //The state variable
    private RoundState state;

    DecimalFormat df = new DecimalFormat("0.0"); //  Format timer that displays on the time on the screen
    float time = 0; // To keep track of time
    float timeBetweenRounds = 2; // How much time for players to prepare between rounds
    float roundLength = 20; // How much time does the player have to move and shoot
    int playerNr = 0; // Decides which player has the turn

    // Default state is to switch to a new round
    public Context() {
        state = new RoundSwitch();
    }

    // Set new state
    public void setState(RoundState state) {
        this.state = state;
    }

    // Get current state
    public RoundState getState() {
        return state;
    }
}
