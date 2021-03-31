package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;


/**
 * Attach to entity to give it a font
 * The font property is almost the same as a sprite, and will be used to draw the font
 * The text property is used for directly changing or getting the text
 * The layout property is used to calculate the width and height of the text (used for centering)
 **/
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