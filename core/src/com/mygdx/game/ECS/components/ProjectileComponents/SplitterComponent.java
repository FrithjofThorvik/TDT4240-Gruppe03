package com.mygdx.game.ECS.components.ProjectileComponents;

import com.badlogic.ashley.core.Component;

/**
 * Add this to entities that should split into pieces
 **/
public class SplitterComponent implements Component {
    public int numberOfSplits;

    public SplitterComponent(int numberOfSplits) {
        this.numberOfSplits = numberOfSplits;
    }
}
