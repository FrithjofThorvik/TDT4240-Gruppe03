package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

// Attach to entity to give it a font
public class FontComponent implements Component {

    public BitmapFont font;
    public String text;
    public GlyphLayout layout; // Calculates width and height font-text

    public FontComponent(String text) {
        this.font = new BitmapFont();
        this.font.setColor(Color.BLACK);
        this.text = text;
        this.layout = new GlyphLayout(this.font, this.text);
    }
}