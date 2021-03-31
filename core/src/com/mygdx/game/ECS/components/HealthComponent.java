package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;

// Every player should have this component, indicating amount of HealthPoints
public class HealthComponent implements Component {
    public int hp;

    public HealthComponent(int hp) {
        this.hp = hp;
    }
}
