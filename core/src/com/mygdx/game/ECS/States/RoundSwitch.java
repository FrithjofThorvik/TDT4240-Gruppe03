package com.mygdx.game.ECS.States;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;

//This state handles logic for what happens when you switch between rounds (from player 1 to player 2)
public class RoundSwitch implements RoundState {
    @Override
    public void doSomething(Context context, float deltaTime, ImmutableArray<Entity> players, Entity timer) {
        // The time variable keeps control of the time spent in this state
        context.time += deltaTime;
        Entity player = players.get(context.playerNr);

        // Since we are switching between players, we want to remove the ability for the current player to move and aim
        player.remove(TakeAimComponent.class);
        player.remove(MovementControlComponent.class);

        // Logic for displaying switch time
        FontComponent timerFont = timer.getComponent(FontComponent.class);
        timerFont.text = "Switching players in: " + context.df.format(context.timeBetweenRounds - context.time) + "s";
        timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);

        // When the switch time has passed, switch to the next player and give that player movement control
        if (context.time > context.timeBetweenRounds) {
            // Increment the playerNr
            context.playerNr += 1;
            if (context.playerNr >= players.size()) {
                context.playerNr = 0;
            }

            // Fetch the new player and give that player movement control
            player = players.get(context.playerNr);
            player.add(new MovementControlComponent());

            // Switch state
            context.time = 0;
            context.setState(new RoundPlay());
        }
    }
}
