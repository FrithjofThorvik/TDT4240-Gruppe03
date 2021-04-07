package com.mygdx.game.ECS.entities.Projectiles;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ECS.Projectiles.SplitterProjectile;
import com.mygdx.game.ECS.components.Box2DComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.ProjectileComponents.ProjectileComponent;
import com.mygdx.game.ECS.components.RenderComponent;
import com.mygdx.game.ECS.components.SpriteComponent;
import com.mygdx.game.ECS.entities.AbstractEntity;

import static com.mygdx.game.utils.B2DConstants.BIT_GROUND;
import static com.mygdx.game.utils.B2DConstants.BIT_PLAYER;
import static com.mygdx.game.utils.B2DConstants.BIT_PROJECTILE;


/**
 * ...
 **/
public abstract class AbstractProjectile extends AbstractEntity {
    public int damage;
    public float speed;
    public Vector2 size;
    public Vector2 position;
    public Texture texture;

    @Override
    public void setEntityStats() {
        this.damage = setDamage();
        this.speed = setSpeed();
        this.size = setSize();
        this.position = setPosition();
        this.texture = setTexture();
    }

    @Override
    public void addCoreComponents(Entity entity) {
        entity.add(new Box2DComponent(
                    this.position, this.size, false, 10f,
                    BIT_PROJECTILE,
                    (short) (BIT_PLAYER | BIT_GROUND)))
                .add(new SpriteComponent(this.texture, this.size.x, this.size.y))
                .add(new PositionComponent(this.position.x, this.position.y))
                .add(new RenderComponent());
    }

    public abstract int setDamage();
    public abstract float setSpeed();
    public abstract Vector2 setSize();
    public abstract Vector2 setPosition();
    public abstract Texture setTexture();
}
