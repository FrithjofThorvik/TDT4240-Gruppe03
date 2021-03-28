package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

// Attach to entity to give it a sprite
public class SpriteComponent implements Component {
    public Sprite sprite;
    public Vector2 size;
    public Vector2 position;

    public SpriteComponent(Texture texture, Vector2 pos, float width, float height) {
        sprite = new Sprite(texture);
        position = pos;
        size = new Vector2(width, height);
    }
}
