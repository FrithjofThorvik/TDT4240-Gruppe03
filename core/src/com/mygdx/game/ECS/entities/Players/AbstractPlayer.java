package com.mygdx.game.ECS.entities.Players;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.HealthComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.RenderComponent;
import com.mygdx.game.ECS.components.ShootingComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.components.VelocityComponent;
import com.mygdx.game.ECS.entities.AbstractEntity;

import static com.mygdx.game.utils.B2DConstants.*;


/**
 * This class adds the core components that player entities need
 * The variables are overwritten by child classes
 * The child classes can add variable components
 **/
public abstract class AbstractPlayer extends AbstractEntity {
    public int health;
    public Vector2 velocity;
    public Vector2 size;
    public Vector2 position;
    public Texture texture;

    @Override
    public void setEntityStats() {
        this.health = getPlayerHealth();
        this.velocity = getPlayerVelocity();
        this.size = getPlayerSize();
        this.position = getPlayerPosition();
        this.texture = getPlayerTexture();
    }

    @Override
    public void addCoreComponents() {
        this.entity.add(new Box2DComponent(
                    this.position, this.size, false, 100f,
                    BIT_PLAYER, (short) (BIT_GROUND | BIT_PROJECTILE | BIT_POWERUP))
                )
                .add(new SpriteComponent(this.texture, this.size.x, this.size.y, 1))
                .add(new HealthComponent(this.health))
                .add(new PositionComponent(this.position.x, this.position.y))
                .add(new VelocityComponent(this.velocity.x, this.velocity.y))
                .add(new ShootingComponent(0, 0))
                .add(new RenderComponent())
                .add(new PlayerComponent());
    }

    public abstract int getPlayerHealth();
    public abstract Vector2 getPlayerVelocity();
    public abstract Vector2 getPlayerSize();
    public abstract Vector2 getPlayerPosition();
    public abstract Texture getPlayerTexture();
}
