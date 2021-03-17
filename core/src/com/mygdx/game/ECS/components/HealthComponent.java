package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {
    int hp;

    public HealthComponent(int hp) {
        this.hp = hp;
    }
}
