package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.PowerBarComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.managers.EntityManager;

import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.utils.GameConstants.MAX_SHOOTING_POWER;

public class UISystem extends EntitySystem {

    private ImmutableArray<Entity> players; // Array for all player entities that are aiming

    //Using a component mapper is the fastest way to load entities
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

    // Store all entities with respective components to entity arrays
    public void addedToEngine(Engine e) {
        players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float deltaTime) {
        if (players.size() > 1) {
            Entity currentPlayer = players.get(GSM.currentPlayer);
            PositionComponent position = pm.get(currentPlayer);

            float startPositionArrow = (Gdx.graphics.getHeight() - EntityManager.powerBar.getComponent(SpriteComponent.class).size.y) / 2f;
            double aimAngleInDegrees = 90f - (float) currentPlayer.getComponent(ShootingComponent.class).angle / (float) Math.PI * 180f;
            float power = currentPlayer.getComponent(ShootingComponent.class).power;

            //Set rotation and position of AimArrow
            EntityManager.aimArrow.getComponent(SpriteComponent.class).sprite.setRotation((float) aimAngleInDegrees);
            EntityManager.aimArrow.getComponent(PositionComponent.class).position.x = position.position.x;
            EntityManager.aimArrow.getComponent(PositionComponent.class).position.y = position.position.y + 25;

            //Set position of powerBarArrow
            EntityManager.powerArrow.getComponent(PositionComponent.class).position.y =
                    startPositionArrow + (EntityManager.powerBar.getComponent(SpriteComponent.class).size.y * (power / MAX_SHOOTING_POWER));
        }
    }
}
