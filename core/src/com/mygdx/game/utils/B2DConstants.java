package com.mygdx.game.utils;


/**
 * This is used for storing Box2D constants
 **/
public final class B2DConstants {

    private B2DConstants() {
    }

    // Pixel Per Meter
    public static float PPM = 32f;

    // Each bit represents a respective object type. These bits are used when creating the Body
    public static final short BIT_GROUND = 2;
    public static final short BIT_PLAYER = 4;
    public static final short BIT_PROJECTILE = 8;
    public static final short BIT_POWERUP = 16;
}