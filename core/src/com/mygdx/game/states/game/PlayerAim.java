package com.mygdx.game.states.game;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.ECS.components.AimComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;

import static com.mygdx.game.managers.GameStateManager.GSM;
import static com.mygdx.game.utils.GameConstants.MAX_SHOOTING_POWER;

public class PlayerAim extends AbstractGameState {
    float startPositionArrow = 0f;

    PositionComponent arrowPosition;
    SpriteComponent powerBarSprite;
    SpriteComponent aimSprite;
    AimComponent aimAngle;

    ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);
    ComponentMapper<AimComponent> am = ComponentMapper.getFor(AimComponent.class);

    @Override
    public void startGameState() {
        GSM.aim.add(new RenderableComponent()); // Render aim entity
        GSM.player.add(new TakeAimComponent()); // Add aim component to player
        GSM.powerBar.add(new RenderableComponent()); // Render power bar
        GSM.powerBarArrow.add(new RenderableComponent()); // Render power bar arrow

        // Get aim entity components
        PositionComponent playerPosition = pm.get(GSM.player);
        SpriteComponent playerSprite = sm.get(GSM.player);
        PositionComponent aimPosition = pm.get(GSM.aim);
        aimSprite = sm.get(GSM.aim);
        aimAngle = am.get(GSM.aim);

        // Set aim icon above current player
        aimPosition.position.x = playerPosition.position.x;
        aimPosition.position.y = playerPosition.position.y + playerSprite.size.y;

        // Get power bar positions and sprites
        arrowPosition = pm.get(GSM.powerBarArrow);
        powerBarSprite = sm.get(GSM.powerBar);

        // Calculate and apply height of power bar arrow
        startPositionArrow = (Gdx.graphics.getHeight() - powerBarSprite.size.y) / 2f;
    }

    @Override
    public void endGameState() {
        GSM.power = 0;
        arrowPosition.position.y = startPositionArrow; // Reset power bar height

        GSM.player.remove(TakeAimComponent.class); // Remove TakeAim component
        GSM.aim.remove(RenderableComponent.class); // Remove render of aim entity
        GSM.powerBar.remove(RenderableComponent.class); // Remove render of power bar
        GSM.powerBarArrow.remove(RenderableComponent.class); // Remove render of power bar arrow
    }

    @Override
    public void update(float dt) {
        aimSprite.sprite.setRotation(aimAngle.angle);
        arrowPosition.position.y = startPositionArrow + (powerBarSprite.size.y * (GSM.power / MAX_SHOOTING_POWER));
    }


}
