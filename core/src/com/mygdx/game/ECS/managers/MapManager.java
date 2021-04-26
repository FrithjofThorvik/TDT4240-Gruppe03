package com.mygdx.game.ECS.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.components.misc.Box2DComponent;
import com.mygdx.game.ECS.components.misc.PositionComponent;
import com.mygdx.game.ECS.components.misc.SpriteComponent;
import com.mygdx.game.ECS.components.flags.RenderComponent;


import static com.mygdx.game.utils.B2DConstants.BIT_GROUND;
import static com.mygdx.game.utils.B2DConstants.BIT_PLAYER;
import static com.mygdx.game.utils.B2DConstants.BIT_POWERUP;
import static com.mygdx.game.utils.B2DConstants.BIT_PROJECTILE;

public class MapManager {
    private Entity map;

    // Creates the map with all ground instances
    public void createMap(String filename, String mapSprite) {
        map = new Entity();
        Texture mapTexture = new Texture(mapSprite);
        // Instantiate map-sprite entity
        getMap().add(new SpriteComponent(
                mapTexture,
                Application.camera.viewportWidth,
                Application.camera.viewportHeight,
                0)
        )
                .add(new PositionComponent(
                        Application.camera.viewportWidth / 2f,
                        Application.camera.viewportHeight / 2f)
                )
                .add(new RenderComponent());
        ECSManager.getInstance().getEngine().addEntity(getMap());

        TiledMap tiledMap = new TmxMapLoader().load(filename);
        MapObjects objects = tiledMap.getLayers().get("ground").getObjects();

        // Loop through all ground objects, and give each object a Box2D body
        for (MapObject object : objects) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            /** Since the map texture scales too the screen -> when we create box2D elements, they will have to scale the same amount **/
            // Calculate scaleUp ratio
            float scaleUpX =  ECSManager.getInstance().spriteMapper.get(getMap()).size.x / mapTexture.getWidth();
            float scaleUpY =  ECSManager.getInstance().spriteMapper.get(getMap()).size.y / mapTexture.getHeight();

            // Scale the mapObject rectangle shape
            rectangle.width *= scaleUpX;
            rectangle.height *= scaleUpY;
            rectangle.x *= scaleUpX;
            rectangle.y *= scaleUpY;

            // Create a new map entity with box2d component equal to rectangle
            Entity mapObject = new Entity();
            mapObject.add(new Box2DComponent(
                    rectangle.getCenter(new Vector2()),
                    new Vector2(rectangle.width, rectangle.height),
                    true,
                    10000f,
                    BIT_GROUND,
                    (short) (BIT_PLAYER | BIT_PROJECTILE | BIT_POWERUP))
            );
            ECSManager.getInstance().getEngine().addEntity(mapObject);
        }
    }

    public Entity getMap() {
        return map;
    }
}
