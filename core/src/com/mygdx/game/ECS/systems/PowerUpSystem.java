package com.mygdx.game.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.components.misc.Box2DComponent;
import com.mygdx.game.ECS.components.misc.PositionComponent;
import com.mygdx.game.ECS.EntityUtils.strategies.PowerUp.DefaultPowerUp;
import com.mygdx.game.ECS.EntityUtils.strategies.PowerUp.HealthUP;
import com.mygdx.game.ECS.EntityUtils.strategies.PowerUp.PowerUpEffect;
import com.mygdx.game.ECS.EntityUtils.strategies.PowerUp.SpeedUp;
import com.mygdx.game.ECS.components.misc.SpriteComponent;
import com.mygdx.game.ECS.components.flags.PowerUpComponent;
import com.mygdx.game.ECS.components.flags.RenderComponent;
import com.mygdx.game.ECS.managers.ECSManager;
import com.mygdx.game.utils.GameConstants;

import static com.mygdx.game.utils.B2DConstants.BIT_GROUND;
import static com.mygdx.game.utils.B2DConstants.BIT_PLAYER;
import static com.mygdx.game.utils.B2DConstants.BIT_POWERUP;



/**
 * This system should control the PowerUps
 * TODO: Has not been implemented yet
 **/
public class PowerUpSystem extends EntitySystem {
    PowerUpEffect effect;
    int timeBetweenPowerUps = 60;
    float timer = 0;
    // Prepare entity arrays
    private ImmutableArray<Entity> powerUps;

    // Store entities into arrays
    public void addedToEngine(Engine e) {
        powerUps = e.getEntitiesFor(Family.all(PowerUpComponent.class).get());
    }

    // Will be called by the engine automatically
    public void update(float dt) {
        timer += dt;
        if (timer > timeBetweenPowerUps) {
            spawnNewPowerUp();
            timer = 0;
        }

        if (this.powerUps.size() > 0) {
            // Loop through all effect entities
            for (int i = 0; i < this.powerUps.size(); ++i) {
                // Get entity
                Entity powerUp = this.powerUps.get(i);

                if ( ECSManager.getInstance().collisionMapper.has(powerUp)) {
                    Entity collisionEntity =  ECSManager.getInstance().collisionMapper.get(powerUp).collisionEntity;
                    if ( ECSManager.getInstance().playerMapper.has(collisionEntity)) {
                        getRandomPowerUp();
                        effect.applyEffect(collisionEntity);
                        powerUp.removeAll();
                    }
                }
            }
        }
    }

    private void spawnNewPowerUp() {
        Entity powerUp = new Entity();
        float xPos = (float) Math.floor(Math.random() * GameConstants.VIRTUAL_WORLD_WIDTH);
        powerUp.add(new SpriteComponent(new Texture("powerUp.png"), 25f, 25f, 1))
                .add(new PositionComponent(xPos, GameConstants.VIRTUAL_WORLD_HEIGHT))
                .add(new Box2DComponent( ECSManager.getInstance().positionMapper.get(powerUp).position,  ECSManager.getInstance().spriteMapper.get(powerUp).size, false, 1000f, BIT_POWERUP, (short) (BIT_PLAYER | BIT_GROUND)))
                .add(new PowerUpComponent())
                .add(new RenderComponent());
        ECSManager.getInstance().getEngine().addEntity(powerUp);
    }

    private void getRandomPowerUp() {
        int number = (int) Math.floor(Math.random() * 2);
        switch (number) {
            case 0:
                effect = new HealthUP();
                break;
            case 1:
                effect = new SpeedUp();
                break;
            default:
                effect = new DefaultPowerUp();
                break;
        }
    }
}

