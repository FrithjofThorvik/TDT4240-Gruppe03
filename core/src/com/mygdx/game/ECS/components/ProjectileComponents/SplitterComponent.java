package com.mygdx.game.ECS.components.ProjectileComponents;

import com.badlogic.ashley.core.Component;

/**
 * This component is attached to projectiles that split into multiple parts
 **/
public class SplitterComponent implements Component {
    public int numberOfSplits;

    public SplitterComponent(int numberOfSplits) {
        this.numberOfSplits = numberOfSplits;
    }
}
