package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ECS.entities.Map.CollisionArea;
import com.mygdx.game.ECS.entities.Map.Map;
import static com.mygdx.game.utils.B2DConstants.BIT_PLAYER;

public class MapSystem extends EntitySystem {
    private Map currentMap;
    private TiledMap tiledMap;
    public ImmutableArray<Entity> map;
    private final Array<MapListener> mapListeners;
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;

    public MapSystem() {
        this.currentMap = null;
        this.mapListeners = new Array<>();
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        loadMap(new World(new Vector2(0, -10f), false));
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

        for (final CollisionArea collArea : currentMap.getCollisionAreas()) {
            bodyDef.type = BodyDef.BodyType.StaticBody;
            final Body body = world.createBody(bodyDef);
            final ChainShape shape = new ChainShape();
            shape.createChain(collArea.getVertices());
            fixtureDef.shape = shape;
            fixtureDef.friction = 0;
            fixtureDef.isSensor = false;
            fixtureDef.filter.maskBits = BIT_PLAYER;
            body.createFixture(fixtureDef);
            shape.dispose();
        }
    }

    public interface MapListener {
        void mapChanged(final Map map);
    }
}
