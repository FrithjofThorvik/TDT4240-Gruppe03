package com.mygdx.game.ECS.EntityUtils.templates.Fonts;

import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.flags.RenderComponent;
import com.mygdx.game.ECS.EntityUtils.templates.AbstractEntity;
/**
 * This abstract font class add the components most Fonts need
 **/
public abstract class AbstractFont extends AbstractEntity {
    public float posX;
    public float posY;
    public String text;

    @Override
    public void setEntityStats() {
        this.posX = setPosX();
        this.posY = setPosY();
        this.text = setText();
    }

    @Override
    public void addCoreComponents() {
        entity.add(new PositionComponent(
                posX,
                posY)
        )
                .add(new FontComponent(text))
                .add(new RenderComponent());
    }

    public abstract float setPosX();
    public abstract  float setPosY();
    public abstract String setText();
}
