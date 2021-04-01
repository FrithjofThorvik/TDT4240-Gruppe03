package com.mygdx.game.ECS.components.ProjectileComponents;

import com.badlogic.ashley.core.Component;

/**
 * This component is added to projectiles which explode on impacts
 **/
public class ExploderComponent implements Component {
    public int blast_radius;

    public ExploderComponent(int blast_radius) {
        this.blast_radius = blast_radius;
    }
}
