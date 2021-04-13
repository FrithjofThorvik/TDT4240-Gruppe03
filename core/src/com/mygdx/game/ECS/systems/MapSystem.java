package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.entities.Map.CollisionArea;
import com.mygdx.game.ECS.entities.Map.Map;
import com.mygdx.game.states.screens.GameScreen;

import static com.mygdx.game.states.screens.GameScreen.world;
import static com.mygdx.game.utils.B2DConstants.BIT_GROUND;
import static com.mygdx.game.utils.B2DConstants.BIT_PLAYER;
import static com.mygdx.game.utils.B2DConstants.BIT_PROJECTILE;
import static com.mygdx.game.utils.B2DConstants.PPM;

public class MapSystem extends EntitySystem {
    private Map currentMap;
    private TiledMap tiledMap;
    public ImmutableArray<Entity> map;
    private final Array<MapListener> mapListeners;

    public MapSystem() {
        this.currentMap = null;
        this.mapListeners = new Array<>();
        loadMap(world);
    }

    public void addedToEngine(Engine e) {
    }

    // Will be called by the engine automatically
    public void update(float dt) {
    }

    public void addMapListener(final MapListener mapListener) {
        mapListeners.add(mapListener);
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public void loadMap(final World world) {
        tiledMap = new TmxMapLoader().load("scifi.tmx");
        if (currentMap == null) {
            currentMap = new Map(tiledMap);
        }
        for (final MapListener mapListener : mapListeners) {
            mapListener.mapChanged(currentMap);
        }
        spawnCollisionAreas(world);
    }

    private void spawnCollisionAreas(final World world) {
        if (currentMap == null) {
            return;
        }

        MapObjects objects = tiledMap.getLayers().get("ground").getObjects();

        for (MapObject object : objects) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            float scale = Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
            System.out.println(scale);
            rectangle.height *= 1.9;
            rectangle.width *= 2;
            rectangle.x *= 2;
            rectangle.y *= 1.9;

            //create a dynamic within the world body (also can be KinematicBody or StaticBody

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            Body body = world.createBody(bodyDef);

            //create a fixture for each body from the shape
            // Create FixtureDef representing properties such as density, restitution, etc
            FixtureDef fixtureDef = new FixtureDef();

            fixtureDef.shape = getShapeFromRectangle(rectangle); // Add the box shape to fixture
            fixtureDef.density = 10000; // Add density to fixture (increases mass)
            fixtureDef.friction = 0.1f;
            fixtureDef.filter.categoryBits = BIT_GROUND; // Is this category of bit
            fixtureDef.filter.maskBits = (short) (BIT_PLAYER | BIT_PROJECTILE); // Will collide with these bits
            body.createFixture(fixtureDef); // Add FixtureDef and id to body

            //setting the position of the body's origin. In this case with zero rotation
            body.setTransform(getTransformedCenterForRectangle(rectangle), 0);
        }
    }

    public interface MapListener {
        void mapChanged(final Map map);
    }


    public static Shape getShapeFromRectangle(Rectangle rectangle) {
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(rectangle.width * 0.5f / PPM, rectangle.height * 0.5f / PPM);
        return polygonShape;
    }

    public static Vector2 getTransformedCenterForRectangle(Rectangle rectangle) {
        Vector2 center = new Vector2();
        rectangle.getCenter(center);
        return center.scl(1 / PPM);
    }
}
