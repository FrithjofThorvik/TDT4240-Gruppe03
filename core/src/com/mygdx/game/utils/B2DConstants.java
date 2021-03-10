package com.mygdx.game.utils;

public final class B2DConstants {

    private B2DConstants() {}

    // Pixel Per Meter
    public static float PPM = 32f;

    // Each bit represents a respective object type. These bits are used when creating the Body
    public static final short BIT_BALL = 2;
    public static final short BIT_PADDLE = 4;
    public static final short BIT_GOAL = 8;
    public static final short BIT_WALL = 16;
}