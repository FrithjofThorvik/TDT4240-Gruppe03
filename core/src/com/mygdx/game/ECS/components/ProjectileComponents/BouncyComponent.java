package com.mygdx.game.ECS.components.ProjectileComponents;

import com.badlogic.ashley.core.Component;

public class BouncyComponent implements Component {
    public int numberOfBounces;

    public BouncyComponent(int numberOfBounces) {
        this.numberOfBounces = numberOfBounces;
    }
}
