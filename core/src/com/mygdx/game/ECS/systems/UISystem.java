package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.HealthComponent;
import com.mygdx.game.ECS.components.ParentComponent;
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
import static com.mygdx.game.utils.GameConstants.START_GAME_TIME;
import static com.mygdx.game.utils.GameConstants.TIME_BETWEEN_ROUNDS;


/**
 * This system is responsible for updating information regarding UI component
 **/
public class UISystem extends EntitySystem {
    private final DecimalFormat df = new DecimalFormat("0.0"); // Format timer that displays on the time on the screen

    // Array for all player entities that are aiming
    private ImmutableArray<Entity> players;

    //Using a component mapper is the fastest way to load entities
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);
    private final ComponentMapper<FontComponent> fm = ComponentMapper.getFor(FontComponent.class);
    private final ComponentMapper<ParentComponent> pam = ComponentMapper.getFor(ParentComponent.class);
    private final ComponentMapper<ShootingComponent> shm = ComponentMapper.getFor(ShootingComponent.class);

    // Store all entities with respective components to entity arrays
    public void addedToEngine(Engine e) {
        this.players = e.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float dt) {
        // If there are any players initialised
        if (this.players.size() > 1) {
            this.printTimer(); // Print information about how much time is left in a round, etc...

            // Get the player who's turn it is and get its position component
            Entity currentPlayer = this.players.get(GSM.currentPlayer);
            PositionComponent position = this.pm.get(currentPlayer);

            // Calculate the startingPosition of an arrow (this is done here so that if the screen is resized the arrowPosition is updated)
            float startPositionArrow = pm.get(EntityManager.powerBar).position.y - sm.get(EntityManager.powerBar).size.y / 2;

            // Get the angle (in degrees) and power of the currentPlayer's shootingComponent
            double aimAngleInDegrees = 90f - (float) shm.get(currentPlayer).angle / (float) Math.PI * 180f;
            float power = shm.get(currentPlayer).power;

            // Set rotation and position of AimArrow (displayed above the player -> rotated by where the player aims)
            sm.get(EntityManager.aimArrow).sprite.setRotation((float) aimAngleInDegrees);
            pm.get(EntityManager.aimArrow).position.x = position.position.x;
            pm.get(EntityManager.aimArrow).position.y = position.position.y + 25;

            //Set position of powerBarArrow -> given the power of the shootingComponent
            pm.get(EntityManager.powerBarArrow).position.y = startPositionArrow + (sm.get(EntityManager.powerBar).size.y * (power / MAX_SHOOTING_POWER));

            // Update health displays
            fm.get(EntityManager.health1).text = pam.get(EntityManager.health1).parent.getComponent(HealthComponent.class).hp + " hp";
            fm.get(EntityManager.health2).text = pam.get(EntityManager.health2).parent.getComponent(HealthComponent.class).hp + " hp";
        }
    }

    // Print information about how much time is left in a round, etc...
    private void printTimer() {
        // SWITCH_ROUND
        if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.SWITCH_ROUND)) {
            FontComponent timerFont = EntityManager.timer.getComponent(FontComponent.class);
            timerFont.text = "Switching players in: " + this.df.format(TIME_BETWEEN_ROUNDS - GSM.time) + "s";
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        }

        // START_GAME
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.START_GAME)) {
            FontComponent timerFont = EntityManager.timer.getComponent(FontComponent.class);
            timerFont.text = "\n\n\n\n" + ((int) START_GAME_TIME - (int) GSM.time);
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        }

        // OTHER
        else {
            FontComponent timerFont = EntityManager.timer.getComponent(FontComponent.class);
            timerFont.text = "Timer: " + this.df.format(ROUND_TIME - GSM.time) + "s";
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        }
    }
}
