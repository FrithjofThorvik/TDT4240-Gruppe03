package com.mygdx.game.ECS.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.EntityUtils.EntityTemplateMapper;
import com.mygdx.game.ECS.components.misc.Box2DComponent;
import com.mygdx.game.ECS.components.misc.ParentComponent;
import com.mygdx.game.ECS.components.misc.PositionComponent;
import com.mygdx.game.ECS.components.misc.SpriteComponent;
import com.mygdx.game.ECS.components.flags.PlayerComponent;
import com.mygdx.game.ECS.components.flags.RenderComponent;

import static com.mygdx.game.ECS.managers.ECSManager.ECSManager;

import static com.mygdx.game.utils.B2DConstants.BIT_GROUND;
import static com.mygdx.game.utils.B2DConstants.BIT_PLAYER;
import static com.mygdx.game.utils.B2DConstants.BIT_PROJECTILE;

public class GameEntitiesManager {
    // Utility function for spawning players
    public void spawnPlayers(int numberOfPlayers) {
        for (int i = 0; i < numberOfPlayers; i++) {
            if (i % 2 == 0)
                ECSManager.getEntityTemplateMapper().getPlayerClass(EntityTemplateMapper.PLAYERS.DEFAULT).createEntity();
            else
                ECSManager.getEntityTemplateMapper().getPlayerClass(EntityTemplateMapper.PLAYERS.SPEEDY).createEntity();

        }
    }

    // Utility function for creating health displayers
    public void createHealthDisplayers() {
        ImmutableArray<Entity> players = ECSManager.getEngine().getEntitiesFor(Family.one(PlayerComponent.class).get());
        for (int i = 0; i < players.size(); i++) {
            Entity player = players.get(i); // Get a player

            // Create the health displayer and add the player as the parent -> such that the health font is attached to the player
            ECSManager.getEntityTemplateMapper().getHealthFont().createEntity().add(new ParentComponent(player));
        }
    }

    // Utility function for creating a target (used in training mode)
    public Entity spawnTarget() {
        Entity target = new Entity();
        target.add(new SpriteComponent(new Texture("target.png"), 75, 75, 1))
                .add(new Box2DComponent(
                        new Vector2((float) Application.VIRTUAL_WORLD_WIDTH / 2, Application.APP_DESKTOP_HEIGHT / 2 + 25), ECSManager.spriteMapper.get(target).size, false, 10000000f,
                        BIT_PLAYER, (short) (BIT_PROJECTILE | BIT_GROUND))
                )
                .add(new PositionComponent(target.getComponent(Box2DComponent.class).body.getPosition().x, target.getComponent(Box2DComponent.class).body.getPosition().y))
                .add(new RenderComponent());
        ECSManager.getEngine().addEntity(target);
        return target;
    }
}
