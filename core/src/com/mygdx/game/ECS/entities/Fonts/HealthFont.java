package com.mygdx.game.ECS.entities.Fonts;

import com.mygdx.game.ECS.components.HealthDisplayerComponent;

/**
 * If a font is a health font -> add the health displayer component
 **/
public class HealthFont extends AbstractFont{
    @Override
    public float setPosX() {
        return 0;
    }

    @Override
    public float setPosY() {
        return 0;
    }

    @Override
    public String setText() {
        return "null";
    }

    @Override
    public void addClassComponents() {
        entity.add(new HealthDisplayerComponent());
    }
}
