package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;


/**
 * This component is for using the controller UI in the game
 * This will create a new stage, viewport, and camera for listening to the right inputs
 * The public boolean values will be used in the ControllerSystem
 **/
public class ControllerComponent implements Component {
    public boolean leftPressed, rightPressed, powerPressed, aimPressed, projectileLeft, projectileRight = false;
}
