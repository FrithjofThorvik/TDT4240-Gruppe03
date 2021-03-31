package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameStateManager;

import java.text.DecimalFormat;

import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.utils.GameConstants.MAX_SHOOTING_POWER;
import static com.mygdx.game.utils.GameConstants.ROUND_TIME;
import static com.mygdx.game.utils.GameConstants.TIME_BETWEEN_ROUNDS;

public class UISystem extends EntitySystem {
    public DecimalFormat df = new DecimalFormat("0.0"); // Format timer that displays on the time on the screen

    private ImmutableArray<Entity> players; // Array for all player entities that are aiming

    //Using a component mapper is the fastest way to load entities
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

    // Store all entities with respective components to entity arrays
    public void addedToEngine(Engine e) {
        players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float deltaTime) {
        printTimer();
        if (players.size() > 1) {
            Entity currentPlayer = players.get(GSM.currentPlayer);
            PositionComponent position = pm.get(currentPlayer);
            float startPositionArrow = EntityManager.powerBar.getComponent(PositionComponent.class).position.y -
                    EntityManager.powerBar.getComponent(SpriteComponent.class).size.y/2;
            double aimAngleInDegrees = 90f - (float) currentPlayer.getComponent(ShootingComponent.class).angle / (float) Math.PI * 180f;
            float power = currentPlayer.getComponent(ShootingComponent.class).power;

            //Set rotation and position of AimArrow
            EntityManager.aimArrow.getComponent(SpriteComponent.class).sprite.setRotation((float) aimAngleInDegrees);
            EntityManager.aimArrow.getComponent(PositionComponent.class).position.x = position.position.x;
            EntityManager.aimArrow.getComponent(PositionComponent.class).position.y = position.position.y + 25;

            //Set position of powerBarArrow
            EntityManager.powerBarArrow.getComponent(PositionComponent.class).position.y =
                    startPositionArrow + (EntityManager.powerBar.getComponent(SpriteComponent.class).size.y * (power / MAX_SHOOTING_POWER));
        }
    }

    private void printTimer() {
        if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.SWITCH_ROUND)) {
            FontComponent timerFont = EntityManager.timer.getComponent(FontComponent.class);
            timerFont.text = "Switching players in: " + df.format(TIME_BETWEEN_ROUNDS - GSM.time) + "s";
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        } else {
            FontComponent timerFont = EntityManager.timer.getComponent(FontComponent.class);
            timerFont.text = "Timer: " + df.format(ROUND_TIME - GSM.time) + "s";
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        }
    }
}
