package com.mygdx.game.ECS.EntityUtils.templates.Projectiles;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.components.misc.Box2DComponent;
import com.mygdx.game.ECS.components.misc.PositionComponent;
import com.mygdx.game.ECS.EntityUtils.strategies.Projectile.ProjectileType;
import com.mygdx.game.ECS.components.projectiles.ProjectileComponent;
import com.mygdx.game.ECS.components.flags.RenderComponent;
import com.mygdx.game.ECS.components.misc.SpriteComponent;
import com.mygdx.game.ECS.EntityUtils.templates.AbstractEntity;

import static com.mygdx.game.utils.B2DConstants.BIT_GROUND;
import static com.mygdx.game.utils.B2DConstants.BIT_PLAYER;
import static com.mygdx.game.utils.B2DConstants.BIT_PROJECTILE;


/**
 * Adds the core components that all projectiles need
 * The variables are overwritten by child classes
 * The child classes can add variable components
 **/
public abstract class AbstractProjectile extends AbstractEntity {
    public int damage;
    public float speed;
    public Vector2 size;
    public Vector2 position;
    public Texture texture;
    public ProjectileType type;

    @Override
    public void setEntityStats() {
        this.damage = getDamage();
        this.speed = getSpeed();
        this.size = getSize();
        this.position = getPosition();
        this.texture = getTexture();
        this.type = getType();
    }

    @Override
    public void addCoreComponents() {
        this.entity.add(new ProjectileComponent(this.damage, this.speed,this.type))
                .add(new Box2DComponent(
                        this.position, this.size, false, 0.2f,
                        BIT_PROJECTILE,
                        (short) (BIT_PLAYER | BIT_GROUND)))
                .add(new SpriteComponent(this.texture, this.size.x, this.size.y, 1))
                .add(new PositionComponent(this.position.x, this.position.y))
                .add(new RenderComponent());
    }

    public abstract int getDamage();

    public abstract float getSpeed();

    public abstract Vector2 getSize();

    public abstract Vector2 getPosition();

    public abstract Texture getTexture();

    public abstract ProjectileType getType();
}
