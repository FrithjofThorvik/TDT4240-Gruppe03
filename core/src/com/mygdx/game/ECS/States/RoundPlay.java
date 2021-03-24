package com.mygdx.game.ECS.States;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;
// When it is currently a players turn, the game is in this state
public class RoundPlay implements RoundState{
    @Override
    public void doSomething(Context context, float deltaTime, ImmutableArray<Entity> players, Entity timer) {
        // The time variable keeps control of the time spent in this state
        context.time+=deltaTime;

        // Logic for displaying amount of time left in the round
        FontComponent timerFont = timer.getComponent(FontComponent.class);
        timerFont.text = "Timer: " + context.df.format(context.roundLength - context.time) + "s";
        timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);

        // Switch state if time has run out
        if (context.time > context.roundLength){
            context.time=0;
            context.setState(new RoundSwitch());
        }
    }
}
