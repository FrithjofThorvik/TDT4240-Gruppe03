package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

//Attach to entity to give it a sprite
public class SpriteComponent implements Component {
    public Sprite sprite;
    public float size;

    public SpriteComponent(Texture texture, float size) {
        this.sprite = new Sprite(texture);
        this.size = size;
    }
}
