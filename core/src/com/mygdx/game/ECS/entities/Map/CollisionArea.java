package com.mygdx.game.ECS.entities.Map;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;

public class CollisionArea {
    private Vector2 startLocation;
    private float[] vertices;

    public CollisionArea(final float x, final float y, final float[] vertices) {
        this.startLocation = new Vector2(x, y);
        this.vertices = vertices;
        for (int i = 0; i < vertices.length; i += 2) {
            vertices[i] = vertices[i];
            vertices[i + 1] = vertices[i + 1];
        }
    }

    public Vector2 getStartLocation() {
        return startLocation;
    }

    public float[] getVertices() {
        return vertices;
    }
}
