package com.mygdx.game.ECS.components.misc;

import com.badlogic.ashley.core.Component;


/**
 * Every player should have this component, indicating amount of HealthPoints
 * This will be used to both get and set current player health
 **/
public class HealthComponent implements Component {
    public int hp;

    public HealthComponent(int hp) {
        this.hp = hp;
    }
}
