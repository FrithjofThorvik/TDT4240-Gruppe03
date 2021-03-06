package com.mygdx.game.ECS.components.misc;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;


/**
 * Attach to entity to give it a sprite
 **/
public class SpriteComponent implements Component {
    public Sprite sprite;
    public Vector2 size;
    public int zValue;

    public SpriteComponent(Texture texture, float width, float height, int zValue) {
        this.sprite = new Sprite(texture);
        this.size = new Vector2(width, height);
        this.zValue = zValue;
    }
}
