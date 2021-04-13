package com.mygdx.game.ECS.entities.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Map {
    private static final String TAG = Map.class.getSimpleName();

    private final TiledMap tiledMap;
    private final Array<CollisionArea> collisionAreas;

    public Map(TiledMap tiledMap) {
        final MapProperties mapProps = tiledMap.getProperties();
        this.tiledMap = tiledMap;
        this.collisionAreas = new Array<>();
        parseCollision();
    }

    private void parseCollision() {
        final MapLayer collisionLayer = tiledMap.getLayers().get("ground");
        if (collisionLayer == null) {
            return;
        }

        for (final MapObject mapObj : collisionLayer.getObjects()) {
            if (mapObj instanceof PolylineMapObject) {
                final Polyline polyline = ((PolylineMapObject) mapObj).getPolyline();
                collisionAreas.add(new CollisionArea(polyline.getX(), polyline.getY(), polyline.getVertices()));
            } else if (mapObj instanceof RectangleMapObject) {
                final Rectangle rect = ((RectangleMapObject) mapObj).getRectangle();
                final float[] rectVertices = new float[10];
                // left-bot
                rectVertices[0] = 0;
                rectVertices[1] = 0;
                // left-top
                rectVertices[2] = 0;
                rectVertices[3] = rect.height;
                // right-top
                rectVertices[4] = rect.width;
                rectVertices[5] = rect.height;
                // right-bot
                rectVertices[6] = rect.width;
                rectVertices[7] = 0;
                // left-bot
                rectVertices[8] = 0;
                rectVertices[9] = 0;
                collisionAreas.add(new CollisionArea(rect.x, rect.y, rectVertices));
                //System.out.println(new CollisionArea(rect.x, rect.y, rectVertices));
            } else {
                Gdx.app.log(TAG, "Unsupported mapObject for collision layer: " + mapObj);
            }
        }

    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public Array<CollisionArea> getCollisionAreas() {
        return collisionAreas;
    }
}