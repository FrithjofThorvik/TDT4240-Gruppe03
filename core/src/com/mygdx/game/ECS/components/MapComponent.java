package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

public class MapComponent implements Component {
        public TiledMap map;
        public Vector2 size;
        OrthographicCamera camera;

        public MapComponent( float width, float height) {
            this.map = new TmxMapLoader().load("scifi.tmx");
            this.size = new Vector2(width, height);
            OrthogonalTiledMapRenderer mapRenderer = new OrthogonalTiledMapRenderer(this.map);
            camera = new OrthographicCamera();
            camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            mapRenderer.setView(camera);
            mapRenderer.render();
        }
}
